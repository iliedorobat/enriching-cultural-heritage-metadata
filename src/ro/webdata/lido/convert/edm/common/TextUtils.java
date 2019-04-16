package ro.webdata.lido.convert.edm.common;

import org.apache.commons.text.CaseUtils;
import ro.webdata.lido.convert.edm.common.constants.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

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
            return CaseUtils.toCamelCase(text, Constants.CAPITALIZE_FIRST_LETTER, Constants.CHAR_DELIMITER);
        }

        return CaseUtils.toCamelCase(input, Constants.CAPITALIZE_FIRST_LETTER, Constants.CHAR_DELIMITER);
    }

    /**
     * Transform the value into a camel-case string
     * @param value The original input
     * @return The camel-case transformed value
     */
    public static String toCamelCase(String value) {
        return CaseUtils.toCamelCase(value, Constants.CAPITALIZE_FIRST_LETTER, Constants.CHAR_DELIMITER);
    }

    /**
     * Replace all multiple spaces and brackets and dots with an underscore "_"
     * @param value The input value
     * @return The formatted value
     */
    //TODO: replace all special characters
    public static String sanitizeString(String value) {
        String regex = "[ \\.\\[\\]]+";
        String replacement = "_";
        return value.replaceAll(regex, replacement);
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
}
