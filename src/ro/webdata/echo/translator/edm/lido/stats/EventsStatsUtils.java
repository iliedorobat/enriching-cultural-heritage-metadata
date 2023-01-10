package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.translator.commons.FileConst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class EventsStatsUtils {
    protected static final String EVENT_TYPE_OTHERS = "others";
    protected static final String PREFIX_TIMESPAN_ALL = "timespan_all_";
    protected static final String PREFIX_TIMESPAN_UNIQUE = "timespan_unique_";

    protected static String getFilePath(String filePrefix, String eventType) {
        String fileName = eventType != null
                ? filePrefix + eventType
                : filePrefix.substring(0, filePrefix.lastIndexOf("_"));

        return FileConst.PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + fileName + File.EXTENSION_SEPARATOR + File.EXTENSION_CSV;
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

    protected static HashMap<String, Integer> getTimeOccurrences(String fullPath) {
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();

        try {
            br = new BufferedReader(new FileReader(fullPath));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String[] values = readLine.split("\\|");
                    String timePeriodsStr = values[values.length - 2];
                    String typesStr = values[values.length - 1];

//                    if (typesStr.split(",").length > 1) {
//                        System.out.println();
//                    }

                    addCsvCells(map, timePeriodsStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    protected static void addCsvCells(HashMap<String, Integer> map, String line) {
        List<String> cells = csvLineToList(line);

        for (String cell : cells) {
            String key = cell;
            // "epoch" or "unknown"
            if (cell.length() == 0) {
                key = EVENT_TYPE_OTHERS;
            }

            if (!map.containsKey(key)) {
                map.put(key, 1);
            } else {
                map.put(key, map.get(key) + 1);
            }
        }
    }

    protected static List<String> csvLineToList(String line) {
        return Arrays.stream(line
                        .substring(1, line.length() - 1)
                        .split(",")
                )
                .map(String::strip)
                .collect(Collectors.toList());
    }
}
