package ro.webdata.translator.edm.approach.event.lido;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Print;
import ro.webdata.normalization.timespan.ro.LidoXmlTimespanAnalysis;
import ro.webdata.normalization.timespan.ro.TimeExpression;
import ro.webdata.normalization.timespan.ro.TimespanUtils;
import ro.webdata.translator.commons.EnvConstants;
import ro.webdata.translator.commons.FileConstants;

import java.io.*;
import java.util.*;

public class Stats {
    private static final ArrayList<String> EXCLUDED_FILES = new ArrayList<>(
            Arrays.asList("demo.xml", "demo2.xml", "demo3.xml")
    );

    public static void main(String[] args) {
//        //TODO: remove
//        boolean PLAY = true;
//        if (PLAY)     TimespanUtils.getTimespanSet(FileConstants.PATH_OUTPUT_TIMESPAN_FILE);
//        else          LidoXmlTimespanAnalysis.check(FileConstants.PATH_OUTPUT_TIMESPAN_FILE);

        // 1. Write to disc all unique timespan values
        LidoXmlTimespanAnalysis.writeAll(FileConstants.PATH_INPUT_LIDO_DIR, FileConstants.PATH_OUTPUT_TIMESPAN_FILE, EXCLUDED_FILES);
        LidoXmlTimespanAnalysis.writeUnique(FileConstants.PATH_INPUT_LIDO_DIR, FileConstants.PATH_OUTPUT_UNIQUE_TIMESPAN_FILE, EXCLUDED_FILES);

        // 2. Print the statistics of the new created properties
        printNewPropertiesStats();

        // 3. Prepare the timespan statistics
        cleaningTimespanAnalysis();
    }

    private static void cleaningTimespanAnalysis() {
        String fileName = FileConstants.PATH_OUTPUT_UNIQUE_TIMESPAN_FILE;
        BufferedReader br = null;
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(fileName));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String value = readLine.toLowerCase();
                    TimeExpression timeExpression = new TimeExpression(value, "|");
                    arrayList.add(timeExpression.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<String> set = new HashSet<>(arrayList);
        arrayList.clear();
        arrayList.addAll(set);
        Collections.sort(arrayList);

        StringBuilder sb = new StringBuilder();
        for (String string : arrayList) {
            sb.append(string).append("\n");
        }

        StringWriter writer = new StringWriter();
        writer.write(sb.toString());
        File.write(writer, FileConstants.PATH_OUTPUT_TIMESPAN_ANALYSIS_FILE, false);
    }

    private static void printNewPropertiesStats() {
        Print.operation(Const.OPERATION_START, EnvConstants.IS_PRINT_ENABLED);

        HashSet<String> values = new HashSet<>();
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();
        ArrayList<String> array = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(FileConstants.PATH_OUTPUT_PROPERTIES_FILE));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    values.add(readLine);
                    array.add(readLine);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file " + FileConstants.PATH_OUTPUT_PROPERTIES_FILE + " have not been found."
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

        Print.operation(Const.OPERATION_END, EnvConstants.IS_PRINT_ENABLED);
    }
}
