package ro.webdata.echo.fetcher.museums.core.cimec;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Thread;
import ro.webdata.echo.fetcher.museums.core.cimec.parser.Anchor;
import ro.webdata.echo.fetcher.museums.core.cimec.parser.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CimecFetcher {
    /**
     * Prepare the list of key-value pairs from the CIMEC dataset
     * @param lang The language used
     * @return The list of key-value pairs
     */
    public static ArrayList<TreeMap<String, Object>> getPairs(String lang) {
        ArrayList<TreeMap<String, Object>> list = new ArrayList<>();
        List<String> links = Anchor.getMuseumsLinks(lang);

        System.out.println("Starting to fetch CIMEC data (lang = " + lang + ")...");

        for (int index = 0; index < links.size(); index++) {
            Thread.sleep(index, Thread.DEFAULT_SLEEP_TIME);
            String link = links.get(index);
            int errorCounter = 0;

            TreeMap<String, Object> pairs = Row.getEntryPairs(link, lang, index, errorCounter);
            if (!pairs.isEmpty()) {
                list.add(pairs);
            }
        }

        System.out.println("CIMEC data fetching is finished!");

        return list;
    }

    public static ArrayList<TreeMap<String, Object>> getTestPairs(String lang) {
        ArrayList<TreeMap<String, Object>> list = new ArrayList<>();

        String uri = lang.equals(Const.LANG_EN)
                ? "https://ghidulmuzeelor.cimec.ro/idEN.asp?k=14&-Muzeul-National-George-Enescu-BUCURESTI"
                : "https://ghidulmuzeelor.cimec.ro/id.asp?k=14&-Muzeul-National-George-Enescu-BUCURESTI";

        TreeMap<String, Object> pairs = Row.getEntryPairs(uri, lang, 280, 0);
        if (!pairs.isEmpty()) {
            list.add(pairs);
        }

        return list;
    }
}
