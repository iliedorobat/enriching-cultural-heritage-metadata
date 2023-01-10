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
    public static void printEventStats() {
        EventsStats.printOccurrences(FileConst.PATH_OUTPUT_LIDO_DIR);
        System.out.println();
        EventsStats.printTimeOccurrences(FileConst.PATH_OUTPUT_LIDO_DIR, 5);
    }

    public static void printTimeOccurrences(String path, int top) {
        ArrayList<String> subdirectoryNames = File.getSubDirectoryNames(path);
        ArrayList<String> fileNames = File.getFileNames(path, File.EXTENSION_CSV);
        EventsStats.printTimeOccurrences(null, fileNames, top);
        System.out.println();

        for (String choType : subdirectoryNames) {
            String newPath = FileConst.PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + choType;
            fileNames = File.getFileNames(newPath, File.EXTENSION_CSV);
            EventsStats.printTimeOccurrences(choType, fileNames, top);
            System.out.println();
        }
    }

    // Display how many collecting, finding, production events have been identified
    public static void printTimeOccurrences(String choType, ArrayList<String> fileNames, int top) {
        String category = choType != null ? choType.toUpperCase() : "ALL";

        if (top > 0) {
            System.out.println("[" + category + "] Top " + top + " time periods:");
        } else {
            System.out.println("[" + category + "] Last " + -top + " time periods:");
        }

        TimeOccurrences.printAllTimeOccurrences(choType, null, top);
        TimeOccurrences.printUniqueTimeOccurrences(choType, null, top);

        for (String fileName : fileNames) {
            if (fileName.startsWith(PREFIX_TIMESPAN_ALL)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_ALL);
                TimeOccurrences.printAllTimeOccurrences(choType, eventType, top);
            } else if (fileName.startsWith(PREFIX_TIMESPAN_UNIQUE)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_UNIQUE);
                TimeOccurrences.printUniqueTimeOccurrences(choType, eventType, top);
            }
        }
    }

    // Display how many date, centuries, millenniums, years, others time periods have been identified
    public static void printOccurrences(String path) {
        System.out.println("Incidence of Time Period Types:");

        ArrayList<String> fileNames = File.getFileNames(path, File.EXTENSION_CSV);
        TypeOccurrences.printAllOccurrences(null);
        TypeOccurrences.printUniqueOccurrences(null);

        for (String fileName : fileNames) {
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
    protected static void printAllTimeOccurrences(String choType, String eventType, int top) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_ALL, choType, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        List<HashMap<String, Object>> topOccurrences = getTopElements(fullPath, top);
        System.out.println("\t\tTOTAL Time Occurrences: " + topOccurrences);
    }

    protected static void printUniqueTimeOccurrences(String choType, String eventType, int top) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_UNIQUE, choType, eventType);
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
        String fullPath = getFilePath(PREFIX_TIMESPAN_ALL, null, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        HashMap<String, Integer> allTypesOccurrences = getTypesOccurrences(fullPath);
        System.out.println("\t\tTOTAL Occurrences: " + allTypesOccurrences);
    }

    protected static void printUniqueOccurrences(String eventType) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_UNIQUE, null, eventType);
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
