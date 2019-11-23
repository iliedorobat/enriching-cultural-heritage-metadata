package ro.webdata.lido.translator.common;

import org.apache.commons.text.CaseUtils;
import ro.webdata.lido.translator.common.constants.Constants;
import ro.webdata.lido.translator.common.constants.EnvConst;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TextUtils {
    /**
     * Transform a literal value (e.g.: "wall@en") into a camel-case string
     * @param input The original input
     * @return The camel-case transformed value
     */
    public static String prepareCamelCaseText(String input) {
        String langSeparator = input.substring(input.length() - 3, input.length() - 2);

        if (langSeparator.equals("@")) {
            String text = input.substring(0, input.length() - 3);
            return CaseUtils.toCamelCase(text, EnvConst.CAPITALIZE_FIRST_LETTER, Constants.CHAR_DELIMITER);
        }

        return CaseUtils.toCamelCase(input, EnvConst.CAPITALIZE_FIRST_LETTER, Constants.CHAR_DELIMITER);
    }

    /**
     * Transform the value into a camel-case string
     * @param value The original input
     * @return The camel-case transformed value
     */
    public static String toCamelCase(String value) {
        return CaseUtils.toCamelCase(value, EnvConst.CAPITALIZE_FIRST_LETTER, Constants.CHAR_DELIMITER);
    }

    /**
     * Replace all non-alphanumeric characters with underscore ("_").
     * @param value The input value
     * @return The formatted value
     */
    public static String sanitizeString(String value) {
        String regex = "(?U)[^\\p{Alnum}]+";
        String replacement = Constants.UNDERSCORE_PLACEHOLDER;
        return value.replaceAll(regex, replacement);
    }

    /**
     * Encode the URI
     * @param uri The URI
     * @return The encoded URI
     */
    public static String encodeURI(String uri) {
        return URLEncoder.encode(uri, StandardCharsets.UTF_8);
    }

    /**
     * Write the processed data on the disc
     * @param sw The writer input
     * @param filePath The full path where the file will be written
     */
    public static void write(StringWriter sw, String filePath) {
        FileWriter fw = null;

        try {
            fw = new FileWriter(filePath);
            fw.write(sw.toString());
            System.out.println("The records have been written on the disc.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                System.err.println("The 'FileWriter' could not be closed."
                        + "\nError: " + e.getMessage());
            }
        }
    }

    public static StringBuilder read(String fileName) {
        BufferedReader br = null;
        StringBuilder sb = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            sb = new StringBuilder();
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    sb.append(readLine + "\n");
                }
            }

            // Remove the last "Enter"
            sb.delete(sb.lastIndexOf("\n"), sb.length());
        } catch (FileNotFoundException e) {
            System.err.println("The file " + fileName + " have not been found."
                    + "\nError: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.err.println("The file 'BufferedReader' could not be closed."
                        + "\nError: " + e.getMessage());
            }
        }

        return sb;
    }
}
