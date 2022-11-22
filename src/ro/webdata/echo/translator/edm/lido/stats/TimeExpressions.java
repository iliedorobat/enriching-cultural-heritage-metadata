package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.translator.edm.lido.commons.FileUtils;
import ro.webdata.echo.translator.edm.lido.commons.StatsUtils;
import ro.webdata.normalization.timespan.ro.TimeExpression;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class TimeExpressions {
    public static void writeAll(String inputPath, String outputFullPath) {
        ArrayList<String> timeExpressions = getTimeExpressions(inputPath, outputFullPath);
        File.write(timeExpressions, outputFullPath, false);
    }

    public static void writeUnique(String inputPath, String outputFullPath) {
        ArrayList<String> timeExpressions = getTimeExpressions(inputPath, outputFullPath);
        Set<String> set = new TreeSet<>(timeExpressions);
        ArrayList<String> uniqueTimeExpressions = new ArrayList<>(set);
        File.write(uniqueTimeExpressions, outputFullPath, false);
    }

    public static ArrayList<String> getTimeExpressions(String inputPath, String outputFullPath) {
        ArrayList<String> timeExpressions = new ArrayList<>();

        // Add the header
        if (!File.exists(outputFullPath)) {
            timeExpressions.add(
                    StatsUtils.prepareLine("raw value", "normalized value", "dbpedia value")
            );
        }

        java.io.File file = new java.io.File(inputPath);
        String fullName = file.getName();
        int dotIndex = fullName.lastIndexOf(".");
        String fileName = fullName.substring(0, dotIndex);

        String inputFilePath = FileUtils.getOutputFilePath(fileName).replace(".rdf", ".txt");
        timeExpressions.addAll(
                toTimeExpression(inputFilePath)
        );

        return timeExpressions;
    }

    private static ArrayList<String> toTimeExpression(String fileName) {
        ArrayList<String> timeExpressions = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String value = readLine.toLowerCase();
                    TimeExpression timeExpression = new TimeExpression(value, "|");
                    timeExpressions.add(timeExpression.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timeExpressions;
    }
}
