package ro.webdata.echo.fetcher.museums.core.cimec.parser;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Thread;
import ro.webdata.echo.commons.accessor.CimecAccessors;
import ro.webdata.echo.fetcher.museums.commons.CollectionsUtils;
import ro.webdata.echo.fetcher.museums.commons.DataFormatter;
import ro.webdata.echo.fetcher.museums.commons.FilePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static ro.webdata.echo.commons.Const.LANG_EN;

public class Row {
    private static final String[] ARRAYS = {
            CimecAccessors.EN_ACCREDITATION,
            CimecAccessors.RO_ACCREDITATION,
            CimecAccessors.EN_EMAIL,
            CimecAccessors.RO_EMAIL,
            CimecAccessors.FAX,
            CimecAccessors.EN_PHONE,
            CimecAccessors.RO_PHONE,
            CimecAccessors.EN_SOCIAL_MEDIA,
            CimecAccessors.RO_SOCIAL_MEDIA,
            CimecAccessors.EN_TIME_TABLE,
            CimecAccessors.RO_TIME_TABLE,
            CimecAccessors.EN_VIRTUAL_TOUR,
            CimecAccessors.RO_VIRTUAL_TOUR,
            CimecAccessors.EN_WEB,
            CimecAccessors.RO_WEB
//            CimecAccessors.RO_PUBLICATIONS
    };
    private static final String[] SUBORDINATED_UNITS = {
            CimecAccessors.EN_SUBORDINATE_UNITS,
            CimecAccessors.RO_SUBORDINATE_UNITS
    };

    /**
     * Get the key-value pairs of a museum. If fails, get an empty map.
     * @param uri The museum link
     * @param lang The language used
     * @param index The index of the anchor
     * @param errorCounter The number of errors encountered
     * @return Map which contains key-value pairs related to a museum
     */
    public static TreeMap<String, Object> getEntryPairs(
            String uri,
            String lang,
            int index,
            int errorCounter
    ) {
        TreeMap<String, Object> pairs = new TreeMap<>();

        try {
            System.out.println("Getting record no. " + index + "...");

            Document doc = Jsoup.connect(uri).get();
            Elements containers = doc.select("body > div.container");
            String picturesAccessor = lang.equals(LANG_EN)
                    ? CimecAccessors.EN_PICTURES
                    : CimecAccessors.RO_PICTURES;
            String subordinateAccessor = lang.equals(LANG_EN)
                    ? CimecAccessors.EN_SUBORDINATE_UNITS
                    : CimecAccessors.RO_SUBORDINATE_UNITS;

            for (Element container : containers) {
                Elements contents = container.children();
                ArrayList<String> picturesLinks = new ArrayList<>();
                ArrayList<TreeMap<String, Object>> supervisorForPairs = SupervisorRow.getEntryPairs(doc, lang, errorCounter);

                for (Element content : contents) {
                    Row.addRows(content, pairs);
                    picturesLinks.addAll(Gallery.getPicturesLinks(content));
                }

                if (picturesLinks.size() > 0)
                    pairs.put(picturesAccessor, picturesLinks);
                if (supervisorForPairs.size() > 0)
                    pairs.put(subordinateAccessor, supervisorForPairs);
                pairs.put(CimecAccessors.CIMEC_URI, uri);
            }
        } catch (IOException e) {
            System.err.println("Trying again to get the record no. " + index);

            if (errorCounter < Thread.ERROR_THRESHOLD) {
                Thread.sleep(Thread.ERROR_SLEEP_TIME);
                getEntryPairs(uri, lang, index, ++errorCounter);
            } else {
                System.err.println("Parsing ERROR (index = " + index + ")\n" + uri);

                String path = FilePath.getErrorFilePath();
                String output = "lang = " + lang + "\tindex = " + index;
                File.write(output, path, true);

                return pairs;
            }
        }

        return pairs;
    }

    /**
     * Add key-value pairs of a museum
     * @param content The museum page content
     * @param pairs Map which contains key-value pairs of a museum
     */
    private static void addRows(Element content, TreeMap<String, Object> pairs) {
        Elements rows = getRows(content);

        for (Element row : rows) {
            Elements cells = row.children();
            addRow(cells, pairs);
        }
    }

    /**
     * Add key-value pair of a museum
     * @param cells The cells from the museum page which contains the key and the value
     * @param pairs Map which contains key-value pairs of a museum
     */
    public static void addRow(Elements cells, TreeMap<String, Object> pairs) {
        String key = getEntryKey(cells);
        String value = getEntryValue(cells);
        String separator = "([;,]+)";

        if (key.equals(CimecAccessors.EN_TIME_TABLE) || key.equals(CimecAccessors.RO_TIME_TABLE)) {
            separator = ";";
        }

        if (value.length() > 0) {
            if (ArrayUtils.contains(ARRAYS, key)) {
                List<String> list = CollectionsUtils.splitString(value, separator);
                pairs.put(key, list);
            } else {
                pairs.put(key, value);
            }
        }
    }

    /**
     * Get the list of rows from a page
     * @param content The museum page content
     * @return The list of rows on the museum page
     */
    public static Elements getRows(Element content) {
        Elements elements = new Elements();

        if (content.attr("class").contains("container")) {
            // inner container
            elements = content.children();
        } else if (content.attr("class").contains("row")) {
            elements = content.children().get(1).children();
        }

        return CollectionsUtils.filterListByAttr(elements, "class", "row", true);
    }

    /**
     * Extract the key from the museum page
     * @param cells The cells from the museum page which contains the key and the value
     * @return The key of an entry
     */
    public static String getEntryKey(Elements cells) {
        String html = cells.get(0).text().replace(":", "");
        return format(html);
    }

    /**
     * Extract the value from the museum page
     * @param cells The cells from the museum page which contains the key and the value
     * @return The value of an entry
     */
    public static String getEntryValue(Elements cells) {
        String html = cells.get(1).getElementsByTag("p").size() > 0
                ? StringEscapeUtils.unescapeHtml4(              // Collection(s)
                        cells.get(1).getElementsByTag("p").html()
                ) : cells.get(1).text();                          // The rest of the fields
        return format(html);
    }

    private static String format(String string) {
        return DataFormatter.formatDiacritics(string)
                .replaceAll("<span id=\"dots\">...</span><span id=\"more\">", "")
                .replaceAll("</span>", "")
                .replaceAll("[\r\n\b\u00A0\u0002\u0003\u0004\u0014\u0016\u0017\u0019\u200e]*", "")
                // https://stackoverflow.com/questions/8203852/the-correct-regex-for-replacing-em-dash-with-a-basic-in-java
                // http://unicode.org/reports/tr18/
                .replaceAll("\\p{Pd}", "-")
                .trim();
    }
}
