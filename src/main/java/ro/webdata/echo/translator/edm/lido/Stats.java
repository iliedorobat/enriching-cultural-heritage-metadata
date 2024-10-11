package ro.webdata.echo.translator.edm.lido;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.translator.commons.Env;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.edm.lido.stats.PlacesStats;
import ro.webdata.echo.translator.edm.lido.stats.TimePeriodsStats;
import ro.webdata.echo.translator.edm.lido.stats.EventTypesStats;
import ro.webdata.echo.translator.edm.lido.stats.NormalizedEventsStats;
import ro.webdata.normalization.timespan.ro.TimespanType;
import ro.webdata.normalization.timespan.ro.analysis.TimespanAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Stats {
    public static void run() {
        // 1. Write the timespan values & statistics to disc
        TimespanAnalysis.write(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_OUTPUT_TIMESPAN_FILE, true, false);
        TimespanAnalysis.write(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_OUTPUT_UNIQUE_TIMESPAN_FILE, true, true);

        TimespanAnalysis.writeDetails(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_OUTPUT_TIMESPAN_FILE, true, false);
        TimespanAnalysis.writeDetails(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_OUTPUT_UNIQUE_TIMESPAN_FILE, true, true);

        // 2. Write the missing places to disc
        PlacesStats.writeAll(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_MISSING_COUNTRY_REGION);
        PlacesStats.writeUnique(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_UNIQUE_MISSING_COUNTRY_REGION);

        // 3. Print the statistics of the new created properties
        printNewPropertiesStats();

        // 4. Print statistics of events & time expressions
        EventTypesStats.printOccurrences(FileConst.PATH_OUTPUT_LIDO_DIR);
        NormalizedEventsStats.printOccurrences(FileConst.PATH_OUTPUT_LIDO_DIR, false); // all occurrences
        NormalizedEventsStats.printOccurrences(FileConst.PATH_OUTPUT_LIDO_DIR, true);  // only edges occurrences
        TimePeriodsStats.printOccurrences(FileConst.PATH_OUTPUT_LIDO_DIR, 10, TimespanType.CENTURY);
    }

    private static void printNewPropertiesStats() {
        Print.operation(Const.OPERATION_START, Env.IS_PRINT_ENABLED);

        HashSet<String> values = new HashSet<>();
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();
        ArrayList<String> array = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(FileConst.PATH_OUTPUT_PROPERTIES_FILE));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    values.add(readLine);
                    array.add(readLine);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file " + FileConst.PATH_OUTPUT_PROPERTIES_FILE + " have not been found."
                    + "\nError: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>(values);
        Collections.sort(list);

        System.out.println("------------------------------------");
        int total = 0;
        for (String entry : list) {
            int count = 0;
            for (int i = 0; i < array.size(); i++) {
                if (entry.equals(array.get(i)))
                    count++;
            }

            total = total + count;
            System.out.println(entry + ":\t\t" + count);
        }
        System.out.println(total);
        System.out.println("------------------------------------");

        Print.operation(Const.OPERATION_END, Env.IS_PRINT_ENABLED);
    }
}
