package ro.webdata.common.constants;

import org.apache.commons.text.CaseUtils;
import ro.webdata.translator.edm.approach.event.lido.common.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.common.constants.EnvConst;

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
}
