package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.normalization.timespan.ro.TimespanType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static ro.webdata.echo.translator.edm.lido.stats.StatsUtils.*;

public class NormalizedEventsStats {
    // Display how many centuries, millenniums, years, other time periods
    // have been created after the normalization process
    // PS: Date type is transformed to year type
    public static void printOccurrences(String path, boolean isEdge) {
        ArrayList<String> subdirectoryNames = File.getSubDirectoryNames(path);
        List<String> fileNames = getFilteredFileNames(path);
        printOccurrences(null, fileNames, isEdge);
        System.out.println();

        for (String choType : subdirectoryNames) {
            String newPath = FileConst.PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + choType;
            fileNames = getFilteredFileNames(newPath);
            printOccurrences(choType, fileNames, isEdge);
            System.out.println();
        }
    }

    private static List<String> getFilteredFileNames(String path) {
        return File.getFileNames(path, File.EXTENSION_CSV)
                .stream()
                .filter(fileName -> fileName.startsWith(StatsUtils.PREFIX_TIMESPAN_ALL))
                .collect(Collectors.toList());
    }

    private static void printOccurrences(String choType, List<String> fileNames, boolean isEdge) {
        String category = choType != null ? choType.toUpperCase() : "ALL";

        System.out.println("[" + category + "] Incidence of NEW Time Period Types:");

        printAllOccurrences(choType, null, isEdge);
        printUniqueOccurrences(choType, null, isEdge);

        for (String fileName : fileNames) {
            if (fileName.startsWith(PREFIX_TIMESPAN_ALL)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_ALL);
                printAllOccurrences(choType, eventType, isEdge);
            } else if (fileName.startsWith(PREFIX_TIMESPAN_UNIQUE)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_UNIQUE);
                printUniqueOccurrences(choType, eventType, isEdge);
            }
        }
    }

    private static void printAllOccurrences(String choType, String eventType, boolean isEdge) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_ALL, choType, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        HashMap<String, Integer> allTypesOccurrences = getAllOccurrences(fullPath, isEdge);
        System.out.println("\t\tTOTAL Occurrences: " + allTypesOccurrences);

        System.out.println("\t\tCOUNT: " + StatsUtils.countAllOccurrences(allTypesOccurrences));
    }

    private static void printUniqueOccurrences(String choType, String eventType, boolean isEdge) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_UNIQUE, choType, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        HashMap<String, Integer> uniqueTypesOccurrences = getUniqueOccurrences(fullPath, isEdge);
        System.out.println("\t\tUNIQUE Occurrences: " + uniqueTypesOccurrences);

        System.out.println("\t\tCOUNT: " + StatsUtils.countAllOccurrences(uniqueTypesOccurrences));
    }

    private static HashMap<String, Integer> getAllOccurrences(String fullPath, boolean isEdge) {
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();

        try {
            br = new BufferedReader(new FileReader(fullPath));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String[] values = readLine.split("\\|");
                    int index = isEdge
                            ? values.length - 1
                            : values.length - 3;
                    String typesStr = values[index];
                    updateOccurrencesMap(map, typesStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    // Count how many times each type of normalized time expressions appears in the data set
    private static HashMap<String, Integer> getUniqueOccurrences(String fullPath, boolean isEdge) {
        HashMap<String, Integer> eventTypeMap = new HashMap<>();
        HashMap<String, Integer> timeOccurrences = StatsUtils.getTimeOccurrences(fullPath, isEdge);

        for (Map.Entry<String, Integer> entry : timeOccurrences.entrySet()) {
            String eventTime = entry.getKey();
            String key = extractTimeCategory(eventTime);

            if (eventTime.equals(StatsUtils.EVENT_TYPE_OTHERS)) {
                // it makes more sense to get the occurrences of EVENT_TYPE_OTHERS time
                // instead of compress it to unique entries (e.g.: 240 EVENT_TYPE_OTHERS
                // instead of compress to 1 unique EVENT_TYPE_OTHERS)
                eventTypeMap.put(key, entry.getValue());
            } else {
                // count the occurrences of normalized time expressions based on their type
                if (!eventTypeMap.containsKey(key)) {
                    eventTypeMap.put(key, 1);
                } else {
                    eventTypeMap.put(key, eventTypeMap.get(key) + 1);
                }
            }
        }

        return eventTypeMap;
    }

    private static void updateOccurrencesMap(HashMap<String, Integer> map, String line) {
        HashSet<String> keys = new HashSet<>();
        List<String> entries = csvEntryToList(line);

        // Avoid adding two types of time expressions which depict the same thing (E.g.: century)
        // E.g.: [https://dbpedia.org/page/2nd_century_BC, https://dbpedia.org/page/3rd_century_BC]
        for (String value : entries) {
            String key = extractTimeCategory(value);
            keys.add(key);
        }

        for (String key : keys) {
            StatsUtils.updateMap(map, key);
        }
    }

    // E.g.:
    // * EVENT_TYPE_OTHERS: ""
    // * TimespanType.CENTURY: https://dbpedia.org/page/1st_century
    // * TimespanType.MILLENNIUM: https://dbpedia.org/page/1st_millennium
    // * TimespanType.YEAR: https://dbpedia.org/page/1990
    private static String extractTimeCategory(String uri) {
        // "epoch" or "unknown"
        if (uri == null || uri.length() == 0 || uri.equals(StatsUtils.EVENT_TYPE_OTHERS)) {
            return StatsUtils.EVENT_TYPE_OTHERS;
        } else if (uri.contains("century")) {
            return TimespanType.CENTURY;
        } else if (uri.contains("millennium")) {
            return TimespanType.MILLENNIUM;
        } else {
            return TimespanType.YEAR;
        }
    }
}
