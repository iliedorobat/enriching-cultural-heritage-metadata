package ro.webdata.echo.fetcher.museums.core.cimec.parser;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Thread;
import ro.webdata.echo.commons.accessor.CimecAccessors;
import ro.webdata.echo.fetcher.museums.commons.FilePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SupervisorRow extends Row {
    private static final String[] SUBORDINATED_UNITS_KEYS = {
            CimecAccessors.EN_MUSEUM_CODE,
            CimecAccessors.EN_MUSEUM_NAME,
            CimecAccessors.RO_MUSEUM_CODE,
            CimecAccessors.RO_MUSEUM_NAME
    };

    /**
     * Get the list of key-value pairs of units subordinate to a museum. If fails, get an empty array.
     * @param mainDoc The museum page
     * @param lang The target language
     * @param errorCounter The number of errors encountered
     * @return The list which contains key-value pairs related to units subordinate to a museum
     */
    public static ArrayList<TreeMap<String, Object>> getEntryPairs(Document mainDoc, String lang, int errorCounter) {
        ArrayList<TreeMap<String, Object>> subordinatePairsList = new ArrayList<>();
        List<String> uris = Anchor.getSubordinatesLinks(mainDoc, lang);

        for (int index = 0; index < uris.size(); index++) {
            String uri = uris.get(index);

            try {
                Document doc = Jsoup.connect(uri).get();
                Elements containers = doc.select("body > div.container");

                for (Element container : containers) {
                    TreeMap<String, Object> subordinatePairs = new TreeMap<>();
                    Elements contents = container.children();

                    for (Element content : contents) {
                        addRows(content, subordinatePairs);
                    }

                    subordinatePairs.put(CimecAccessors.CIMEC_URI, uri);
                    subordinatePairsList.add(subordinatePairs);
                }
            } catch (IOException e) {
                System.err.println("Trying again to get the 'Supervised For' record no. " + index);

                if (errorCounter < Thread.ERROR_THRESHOLD) {
                    Thread.sleep(Thread.ERROR_SLEEP_TIME);
                    getEntryPairs(mainDoc, lang, ++errorCounter);
                } else {
                    System.err.println("Parsing ERROR (index = " + index + ")\n" + uri);

                    String path = FilePath.getErrorFilePath();
                    String output = "lang = " + lang + "\tindex = " + index;
                    File.write(output, path, true);

                    return subordinatePairsList;
                }
            }
        }

        return subordinatePairsList;
    }

    /**
     * Add key-value pairs of units subordinate to a museum
     * @param content The museum page content
     * @param subordinatePairs Map which contains key-value pairs of units subordinate to a museum
     */
    private static void addRows(Element content, TreeMap<String, Object> subordinatePairs) {
        Elements rows = getRows(content);

        for (Element row : rows) {
            Elements subordinateCells = row.children();
            String key = getEntryKey(subordinateCells);

            if (ArrayUtils.contains(SUBORDINATED_UNITS_KEYS, key)) {
                addRow(subordinateCells, subordinatePairs);
            }
        }
    }
}
