package ro.webdata.translator.edm.approach.event.lido;

import ro.webdata.common.utils.FileUtils;
import ro.webdata.translator.edm.approach.event.lido.common.PrintMessages;
import ro.webdata.translator.edm.approach.event.lido.common.constants.EnvConst;
import ro.webdata.translator.edm.approach.event.lido.common.constants.FileConstants;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.TimespanAnalysis;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.TimespanUtils;

import java.io.*;
import java.util.*;

public class Stats {
    private static String[] fileNames = {
            FileConstants.FILE_NAME_ARHEOLOGIE,
            FileConstants.FILE_NAME_ARTA,
            FileConstants.FILE_NAME_ARTE_DECO,
            FileConstants.FILE_NAME_DOC,
            FileConstants.FILE_NAME_ETNO,
            FileConstants.FILE_NAME_ST_TEH,
            FileConstants.FILE_NAME_ISTORIE,
            FileConstants.FILE_NAME_MEDALISTICA,
            FileConstants.FILE_NAME_NUMISMATICA,
            FileConstants.FILE_NAME_ST_NAT
    };

    public static void main(String[] args) {
//        //TODO: remove
//        boolean PLAY = true;
//        if (PLAY)     TimespanUtils.getTimespanSet(FileConstants.PATH_OUTPUT_TIMESPAN_FILE);
//        else          TimespanAnalysis.check(FileConstants.PATH_OUTPUT_TIMESPAN_FILE);
//
//        // 1. Write to disc all unique timespan values
//        TimespanAnalysis.write(fileNames, FileConstants.PATH_OUTPUT_TIMESPAN_FILE);
//
//        // 2. Print the statistics of the new created properties
//        printNewPropertiesStats();
//
//        // 3. Prepare the timespan statistics
//        cleaningTimespanAnalysis();
    }

    private static void cleaningTimespanAnalysis() {
        String fileName = FileConstants.PATH_OUTPUT_TIMESPAN_ANALYSIS_FILE;
        BufferedReader br = null;
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(fileName));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String value = readLine.toLowerCase();
                    arrayList.add(value);
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

        String fileWriterName = FileConstants.PATH_DATA_PROCESSING_DIR + FileConstants.FILE_SEPARATOR + "timespan-analysis-cleaned.csv";
        StringWriter writer = new StringWriter();
        writer.write(sb.toString());
        FileUtils.write(writer, fileWriterName);
    }

    private static void printNewPropertiesStats() {
        System.out.println(EnvConst.OPERATION_START);

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

        PrintMessages.printOperation(EnvConst.OPERATION_FINISH);
    }
}
