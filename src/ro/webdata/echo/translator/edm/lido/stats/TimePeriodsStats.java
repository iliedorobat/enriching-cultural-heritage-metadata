package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.translator.commons.FileConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ro.webdata.echo.translator.edm.lido.stats.StatsUtils.*;

public class TimePeriodsStats {
    public static void printOccurrences(String path, int top, String timespanType) {
        ArrayList<String> subdirectoryNames = File.getSubDirectoryNames(path);
        ArrayList<String> fileNames = File.getFileNames(path, File.EXTENSION_CSV);
        printOccurrences(null, fileNames, top, timespanType);
        System.out.println();

        for (String choType : subdirectoryNames) {
            String newPath = FileConst.PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + choType;
            fileNames = File.getFileNames(newPath, File.EXTENSION_CSV);
            printOccurrences(choType, fileNames, top,  timespanType);
            System.out.println();
        }
    }

    // Display how many collecting, finding, production events have been identified
    private static void printOccurrences(String choType, ArrayList<String> fileNames, int top, String timespanType) {
        String category = choType != null ? choType.toUpperCase() : "ALL";

        if (top > 0) {
            System.out.println("[" + category + "] Top " + top + " time periods:");
        } else {
            System.out.println("[" + category + "] Last " + -top + " time periods:");
        }

        printAllOccurrences(choType, null, top, timespanType);
        printUniqueOccurrences(choType, null, top, timespanType);

        for (String fileName : fileNames) {
            if (fileName.startsWith(PREFIX_TIMESPAN_ALL)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_ALL);
                printAllOccurrences(choType, eventType, top, timespanType);
            } else if (fileName.startsWith(PREFIX_TIMESPAN_UNIQUE)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_UNIQUE);
                printUniqueOccurrences(choType, eventType, top, timespanType);
            }
        }
    }

    private static void printAllOccurrences(String choType, String eventType, int top, String timespanType) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_ALL, choType, eventType);
        String title = eventType == null
                ? "ALL time expressions"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        List<HashMap<String, Object>> topOccurrences = getTopElements(fullPath, top, timespanType);
        System.out.println("\t\tTOTAL Time Occurrences: " + topOccurrences);
    }

    private static void printUniqueOccurrences(String choType, String eventType, int top, String timespanType) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_UNIQUE, choType, eventType);
        String title = eventType == null
                ? "UNIQUE time expressions"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        List<HashMap<String, Object>> topOccurrences = getTopElements(fullPath, top, timespanType);
        System.out.println("\t\tUNIQUE Time Occurrences: " + topOccurrences);
    }

    private static List<HashMap<String, Object>> getTopElements(String fullPath, int top, String timespanType) {
        HashMap<String, Integer> occurrences = getTimeOccurrences(fullPath, false);
        HashMap<String, Integer> filteredOccurrences = filterOccurrences(occurrences, timespanType);
        List<HashMap<String, Object>> ordered = getOrderedOccurrences(filteredOccurrences);

        if (top < 0) {
            if (-top > ordered.size()) {
                return ordered.subList(0, ordered.size());
            }
            return ordered.subList(0, -top);
        } else if (top > ordered.size()) {
            return ordered;
        }

        return ordered.subList(ordered.size() - top, ordered.size());
    }

    private static HashMap<String, Integer> filterOccurrences(HashMap<String, Integer> map, String timespanType) {
        if (timespanType == null) {
            return map;
        }

        HashMap<String, Integer> filteredMap = new HashMap<>();

        for (Map.Entry<String, Integer> item : map.entrySet()) {
            String key = item.getKey();
            key = key.substring(key.lastIndexOf("/") + 1);

            if (key.contains(timespanType)) {
                filteredMap.put(item.getKey(), item.getValue());
            }
        }

        return filteredMap;
    }
}
