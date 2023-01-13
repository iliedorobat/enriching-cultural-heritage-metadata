package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.translator.commons.FileConst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class StatsUtils {
    protected static final String EVENT_TYPE_OTHERS = "others";
    protected static final String PREFIX_TIMESPAN_ALL = "timespan_all_";
    protected static final String PREFIX_TIMESPAN_UNIQUE = "timespan_unique_";

    protected static String getFilePath(String filePrefix, String choType, String eventType) {
        String middlePath = choType != null
                ? choType + File.FILE_SEPARATOR
                : "";
        String fileName = eventType != null
                ? filePrefix + eventType
                : filePrefix.substring(0, filePrefix.lastIndexOf("_"));

        return FileConst.PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + middlePath + fileName + File.EXTENSION_SEPARATOR + File.EXTENSION_CSV;
    }

    protected static String getEventType(String fileName, String prefix) {
        return fileName.substring(prefix.length(), fileName.indexOf("."));
    }

    protected static ArrayList<HashMap<String, Object>> getOrderedOccurrences(HashMap<String, Integer> occurrences) {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            arrayList.add(new HashMap<>() {{
                put("key", entry.getKey());
                put("value", entry.getValue());
            }});
        }

        arrayList.sort(new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                return Integer.compare((int) o1.get("value"), (int) o2.get("value"));
            }
        });

        return arrayList;
    }

    // Count how many times each normalized time expression appears in the data set
    protected static HashMap<String, Integer> getTimeOccurrences(String fullPath) {
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();

        try {
            br = new BufferedReader(new FileReader(fullPath));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String[] values = readLine.split("\\|");
                    String urisStr = values[values.length - 2];
                    updateTimeTypesOccurrences(map, urisStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    protected static void updateTimeTypesOccurrences(HashMap<String, Integer> map, String line) {
        List<String> entries = csvEntryToList(line);

        for (String value : entries) {
            String key = value;
            // "epoch" or "unknown"
            if (value.length() == 0) {
                key = EVENT_TYPE_OTHERS;
            }

            updateMap(map, key);
        }
    }

    protected static void updateMap(HashMap<String, Integer> map, String key) {
        if (!map.containsKey(key)) {
            map.put(key, 1);
        } else {
            map.put(key, map.get(key) + 1);
        }
    }

    protected static List<String> csvEntryToList(String line) {
        return Arrays.stream(
                line
                        .substring(1, line.length() - 1)
                        .split(",")
                )
                .map(String::strip)
                .collect(Collectors.toList());
    }

    /* E.g.:
    map = {
        "century": 42003,
        "year": 173655,
        "millennium": 38768,
        "others": 1458
    }
    counter = 255884
     */
    protected static long countAllOccurrences(HashMap<String, Integer> occurrencesMap) {
        long counter = 0;

        for (Map.Entry<String, Integer> entry : occurrencesMap.entrySet()) {
            counter += entry.getValue();
        }

        return counter;
    }
}
