package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.translator.commons.FileConst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ro.webdata.echo.translator.edm.lido.stats.EventsStatsUtils.*;

public class EventsStats {
    private static final ArrayList<String> FILE_NAME_LIST = File.getFileNames(FileConst.PATH_OUTPUT_LIDO_DIR, File.EXTENSION_CSV);

    public static void printTimeOccurrences(int top) {
        if (top > 0) {
            System.out.println("Top " + top + " time periods:");
        } else {
            System.out.println("Last " + -top + " time periods:");
        }

        TimeOccurrences.printAllTimeOccurrences(null, top);
        TimeOccurrences.printUniqueTimeOccurrences(null, top);

        for (String fileName : FILE_NAME_LIST) {
            if (fileName.startsWith(PREFIX_TIMESPAN_ALL)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_ALL);
                TimeOccurrences.printAllTimeOccurrences(eventType, top);
            } else if (fileName.startsWith(PREFIX_TIMESPAN_UNIQUE)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_UNIQUE);
                TimeOccurrences.printUniqueTimeOccurrences(eventType, top);
            }
        }
    }

    public static void printOccurrences() {
        System.out.println("Incidence of Time Period Types:");

        TypeOccurrences.printAllOccurrences(null);
        TypeOccurrences.printUniqueOccurrences(null);

        for (String fileName : FILE_NAME_LIST) {
            if (fileName.startsWith(PREFIX_TIMESPAN_ALL)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_ALL);
                TypeOccurrences.printAllOccurrences(eventType);
            } else if (fileName.startsWith(PREFIX_TIMESPAN_UNIQUE)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_UNIQUE);
                TypeOccurrences.printUniqueOccurrences(eventType);
            }
        }
    }
}

class TimeOccurrences {
    protected static void printAllTimeOccurrences(String eventType, int top) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_ALL, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        List<HashMap<String, Object>> topOccurrences = getTopElements(fullPath, top);
        System.out.println("\t\tTOTAL Time Occurrences: " + topOccurrences);
    }

    protected static void printUniqueTimeOccurrences(String eventType, int top) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_UNIQUE, eventType);
        String title = eventType == null
                ? "UNIQUE event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");


        List<HashMap<String, Object>> topOccurrences = getTopElements(fullPath, top);
        System.out.println("\t\tUNIQUE Time Occurrences: " + topOccurrences);
    }

    private static List<HashMap<String, Object>> getTopElements(String fullPath, int top) {
        HashMap<String, Integer> occurrences = getTimeOccurrences(fullPath);
        List<HashMap<String, Object>> ordered = getOrderedOccurrences(occurrences);

        if (top < 0) {
            if (-top > ordered.size()) {
                return ordered.subList(0, ordered.size());
            }
            return ordered.subList(0, -top);
        }

        return ordered.subList(ordered.size() - top, ordered.size());
    }
}

class TypeOccurrences {
    protected static void printAllOccurrences(String eventType) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_ALL, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        HashMap<String, Integer> allTypesOccurrences = getTypesOccurrences(fullPath);
        System.out.println("\t\tTOTAL Occurrences: " + allTypesOccurrences);
    }

    protected static void printUniqueOccurrences(String eventType) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_UNIQUE, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        HashMap<String, Integer> uniqueTypesOccurrences = getTypesOccurrences(fullPath);
        System.out.println("\t\tUNIQUE Occurrences: " + uniqueTypesOccurrences);
    }

    private static HashMap<String, Integer> getTypesOccurrences(String fullPath) {
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();

        try {
            br = new BufferedReader(new FileReader(fullPath));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String[] values = readLine.split("\\|");
                    String typesStr = values[values.length - 1];
                    addCsvCells(map, typesStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
