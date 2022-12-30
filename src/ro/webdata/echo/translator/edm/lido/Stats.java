package ro.webdata.echo.translator.edm.lido;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.translator.commons.Env;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.edm.lido.stats.MissingPlaces;
import ro.webdata.normalization.timespan.ro.LidoXmlTimespanAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static ro.webdata.echo.commons.File.EXTENSION_SEPARATOR;
import static ro.webdata.echo.commons.File.EXTENSION_XML;

public class Stats {
    private static final ArrayList<String> EXCLUDED_FILES = getExcludedFiles();

    public static void run() {
        // 1. Write the timespan values & statistics to disc
        LidoXmlTimespanAnalysis.writeAll(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_OUTPUT_TIMESPAN_FILE, EXCLUDED_FILES);
        LidoXmlTimespanAnalysis.writeUnique(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_OUTPUT_UNIQUE_TIMESPAN_FILE, EXCLUDED_FILES);

        // 2. Write the missing places to disc
        MissingPlaces.writeAll(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_MISSING_COUNTRY_REGION);
        MissingPlaces.writeUnique(FileConst.PATH_INPUT_LIDO_DIR, FileConst.PATH_UNIQUE_MISSING_COUNTRY_REGION);

        // 3. Print the statistics of the new created properties
        printNewPropertiesStats();
    }

    private static ArrayList<String> getExcludedFiles() {
        ArrayList<String> excludedFiles = new ArrayList<>();
        java.io.File lidoDirectory = new java.io.File(FileConst.PATH_INPUT_LIDO_DIR);
        java.io.File[] subDirectories = lidoDirectory.listFiles();

        if (subDirectories != null) {
            for (java.io.File file : subDirectories) {
                String fullName = file.getName();
                int dotIndex = fullName.lastIndexOf(".");
                String fileName = fullName.substring(0, dotIndex);

                if (fileName.startsWith("demo")) {
                    excludedFiles.add(fileName + EXTENSION_SEPARATOR + EXTENSION_XML);
                }
            }
        } else {
            System.err.println(FileConst.PATH_INPUT_LIDO_DIR + " does not contain any directories!");
        }

        return excludedFiles;
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
