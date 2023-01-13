package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.translator.commons.FileConst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import static ro.webdata.echo.translator.edm.lido.stats.StatsUtils.*;

public class EventTypesStats {
    // Display how many date, centuries, millenniums, years, others time periods
    // have been identified in the target dataset
    public static void printOccurrences(String path) {
        ArrayList<String> subdirectoryNames = File.getSubDirectoryNames(path);
        ArrayList<String> fileNames = File.getFileNames(path, File.EXTENSION_CSV);
        printOccurrences(null, fileNames);
        System.out.println();

        for (String choType : subdirectoryNames) {
            String newPath = FileConst.PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + choType;
            fileNames = File.getFileNames(newPath, File.EXTENSION_CSV);
            printOccurrences(choType, fileNames);
            System.out.println();
        }
    }

    private static void printOccurrences(String choType, ArrayList<String> fileNames) {
        String category = choType != null ? choType.toUpperCase() : "ALL";

        System.out.println("[" + category + "] Incidence of Time Period Types:");

        printAllOccurrences(choType, null);
        printUniqueOccurrences(choType, null);

        for (String fileName : fileNames) {
            if (fileName.startsWith(PREFIX_TIMESPAN_ALL)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_ALL);
                printAllOccurrences(choType, eventType);
            } else if (fileName.startsWith(PREFIX_TIMESPAN_UNIQUE)) {
                String eventType = getEventType(fileName, PREFIX_TIMESPAN_UNIQUE);
                printUniqueOccurrences(choType, eventType);
            }
        }
    }

    private static void printAllOccurrences(String choType, String eventType) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_ALL, choType, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        HashMap<String, Integer> allTypesOccurrences = getOccurrences(fullPath);
        System.out.println("\t\tTOTAL Occurrences: " + allTypesOccurrences);

        System.out.println("\t\tCOUNT: " + StatsUtils.countAllOccurrences(allTypesOccurrences));
    }

    private static void printUniqueOccurrences(String choType, String eventType) {
        String fullPath = getFilePath(PREFIX_TIMESPAN_UNIQUE, choType, eventType);
        String title = eventType == null
                ? "ALL event types"
                : eventType.toUpperCase() + " event type";

        System.out.println("\t" + title + ":");

        HashMap<String, Integer> uniqueTypesOccurrences = getOccurrences(fullPath);
        System.out.println("\t\tUNIQUE Occurrences: " + uniqueTypesOccurrences);

        System.out.println("\t\tCOUNT: " + StatsUtils.countAllOccurrences(uniqueTypesOccurrences));
    }

    private static HashMap<String, Integer> getOccurrences(String fullPath) {
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();

        try {
            br = new BufferedReader(new FileReader(fullPath));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String[] values = readLine.split("\\|");
                    String typesStr = values[values.length - 1];
                    StatsUtils.updateTimeTypesOccurrences(map, typesStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
