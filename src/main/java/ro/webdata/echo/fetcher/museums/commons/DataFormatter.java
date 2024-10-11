package ro.webdata.echo.fetcher.museums.commons;

import org.apache.commons.lang3.text.WordUtils;
import ro.webdata.echo.commons.accessor.MuseumAccessors;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataFormatter {
    private static final String BLANK_SPACES = "\\p{Blank}+";
    private static final String EMPTY_STRING = "";
    private static final String PHONE_SEPARATOR = ".";
    private static final List<String> NAME_FORMAT_LIST = Arrays.asList(
            MuseumAccessors.CONTACT_DIRECTOR,
            MuseumAccessors.CONTACT_PERSON_NAME,
            MuseumAccessors.CONTACT_PERSON_POSITION,
            MuseumAccessors.LOCATION_GEO_TARGET,
            MuseumAccessors.LOCATION_ADM_UNIT,
            MuseumAccessors.LOCATION_COMMUNE,
            MuseumAccessors.LOCATION_COUNTY,
            MuseumAccessors.LOCATION_LOCALITY_NAME,
            MuseumAccessors.MUSEUM_COLLECTION_PART_OF,
            MuseumAccessors.MUSEUM_NAME,
            MuseumAccessors.MUSEUM_SUPERVISED_BY,
            MuseumAccessors.MUSEUM_SUPERVISOR_FOR
    );
    private static final List<String> CATEGORY_LIST = Arrays.asList(
            MuseumAccessors.COLLECTION_GENERAL_PROFILE,
            MuseumAccessors.COLLECTION_IMPORTANCE,
            MuseumAccessors.COLLECTION_MAIN_PROFILE
    );
    private static final List<String> GEO_LIST = Arrays.asList(
            MuseumAccessors.LOCATION_GEO_LATITUDE,
            MuseumAccessors.LOCATION_GEO_LONGITUDE
    );
    private static final List<String> PHONE_LIST = Arrays.asList(
            MuseumAccessors.CONTACT_FAX,
            MuseumAccessors.CONTACT_PHONE
    );

    public static String format(String key, String value) {
        if (NAME_FORMAT_LIST.contains(key))
            return formatName(value);
        else if (CATEGORY_LIST.contains(key))
            return formatCategory(value);
        else if (PHONE_LIST.contains(key))
            return formatPhone(value);
        else if (GEO_LIST.contains(key))
            return formatGeoCoord(value);

        return value;
    }

    /**
     * Replace the characters "s" and "t" written with cedillas
     * with characters "s" and "t" written with comma below.<br/>
     * See ISO 8859
     * @param str The original string
     * @return The string after data curation
     */
    public static String formatDiacritics(String str) {
        return str.replace('Ş', 'Ș')
                .replace('ş', 'ș')
                .replace('Ţ', 'Ț')
                .replace('ţ', 'ț');
    }

    private static String formatCategory(String str) {
        return formatName(str)
                .replace("-", " - ")
                .replaceAll(BLANK_SPACES," ");
    }

    private static String formatGeoCoord(String coord) {
        return coord.replace(",", ".");
    }

    private static String formatName(String str) {
        char[] delimiters = { ' ', '-', '"' };

        str = str//.replace("–", "-")
                .replaceAll("[\"„“”]", "\\\"")
                .replaceAll("-\\s+", "- ")
                .replaceAll("\\s+-", " -")
                .replaceAll(BLANK_SPACES," ");

        return WordUtils.capitalizeFully(str, delimiters);
    }

    // TODO: review
    private static String formatPhone(String phone) {
        String formattedPhone = "";
        String[] phoneValue;
        String patternStr = "[a-zA-z]";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(phone);

        String replace = "(\\s*–\\s*)|(\\s*-\\s*)";
        replace += "|(\\+4)|(\\.)|(/)";

        //String replaceBlank = "\\p{Blank}+";
        String BLANK_BETWEEN_DIGITS = "(?<=\\d)\\p{Blank}+(?=\\d)";
        String COMMA_BETWEEN_ALPHA = "(?<=\\p{Alpha})\\p{Blank}*,\\p{Blank}*(?=\\p{Alpha})";
        String replaceCommaTwoDigit = "(?<=\\d)\\p{Blank}*,\\p{Blank}*(?=\\d)";
        String CHAR_BEFORE_DIGIT = "^.*?(?=\\d)";
        String TEMP_CHAR = "###";

        if (!matcher.find()) {
            phoneValue = phone.split("[,;]");

            for (int i = 0; i < phoneValue.length; i++) {
                if (phoneValue[i].length() > 0) {
                    phoneValue[i] = phoneValue[i].replaceAll(replace, EMPTY_STRING).trim();

                    if (phoneValue[i].length() <= 12)
                        phoneValue[i] = phoneValue[i].replace(" ", EMPTY_STRING);
                    if (phoneValue[i].length() == 9 && !phoneValue[i].substring(0, 1).equals("0"))
                        phoneValue[i] = "0" + phoneValue[i];

                    if (phoneValue[i].length() == 10) {
                        formattedPhone += i > 0 ? "\n" : EMPTY_STRING;
                        formattedPhone += standardizePhone(phoneValue[i]);
                    }
                }
            }
            //if (formatedTelFax.length() > 0) System.out.println("id " + objId + "\n" + formatedTelFax + "\n");
        } else {
            //TODO: StringUtils vs Normalizer.normalize !!!
            //value = Normalizer.normalize(value, Normalizer.Form.NFD);
            //value = value.replaceAll("[\\p{InCombiningDiacriticalMarks}]", EMPTY_STRING);
            //value = StringUtils.stripAccents(value);
            phone = replaceConjunction(phone, ";")
                    .replace("+4", EMPTY_STRING);
            phone = removeIntPlaceholder(phone)
                    .replaceAll(COMMA_BETWEEN_ALPHA, TEMP_CHAR);
            phoneValue = phone.split("[,;]");

            for (int i = 0; i < phoneValue.length; i++) {
                if (phoneValue[i].length() > 0) {
                    phoneValue[i] = phoneValue[i]
                            .replaceAll(BLANK_SPACES, " ")
                            .replaceAll("(?<!int)[-/\\.]*", EMPTY_STRING)
                            .replaceAll(BLANK_BETWEEN_DIGITS, EMPTY_STRING)
                            .replaceAll(CHAR_BEFORE_DIGIT, EMPTY_STRING)
                            .replace(TEMP_CHAR, ", ")
                            .trim();

                    String tel = "", det = "";
                    if (phoneValue[i].contains(" ")) {
                        tel = phoneValue[i].substring(0, phoneValue[i].indexOf(" "));
                        det = phoneValue[i].substring(phoneValue[i].indexOf(" ")+1).replaceAll("[\\(\\)]", EMPTY_STRING);
                    } else {
                        tel = phoneValue[i];
                        det = "";
                    }

                    if (tel.length() == 9 && !tel.substring(0, 1).equals("0"))
                        tel = "0" + tel;
                    if (tel.length() == 10) {
                        tel = standardizePhone(tel);
                        if (det.length() > 0) {
                            if (i == 0) formattedPhone = tel + " " + det;
                            else formattedPhone += "\n" + tel + " " + det;
                        } else {
                            if (i == 0) formattedPhone = tel;
                            else formattedPhone += "\n" + tel;
                        }
                    } else {
                        tel = "";
                    }
                }
            }
            formattedPhone = WordUtils.capitalizeFully(formattedPhone).trim();
            //if (formatedTelFax.length() > 0) System.out.println("id " + objId + "\n" + formatedTelFax + "\n");
        }

        //TODO: .split(System.lineSeparator())
        phoneValue = formattedPhone.split("[\r\n]");
        formattedPhone = "";

        for (int i = 0; i < phoneValue.length; i++) {
            formattedPhone += phoneValue[i];

            if (i < phoneValue.length - 1)
                formattedPhone += "; ";
        }

        return formattedPhone;
    }

    private static String replaceConjunction(String phone, String separator) {
        String regex = "\\p{Blank}sau|si|și|şi\\p{Blank}";
        return phone.replaceAll(regex, separator);
    }

    private static String removeIntPlaceholder(String phone) {
        // Standardizeaza numerele de telefon care au in componenta "int"
        // E.g.: "0244.367.461,  int:. 107"
        // E.g.: "0244.367.461,  (  int:  . 107"
        return phone
                .replaceAll("[.,:]*\\p{Blank}*\\(?\\p{Blank}*(int)\\p{Blank}*[.:]*\\p{Blank}*", " int. ")
                // Remove the closed parentheses after the number ')'
                // E.g.: "0244.367.461,  (  int:  . 107  )   "
                .replaceAll("(?<=\\d)\\p{Blank}*\\)", EMPTY_STRING);
    }

    /**
     * Format the phone number using a unique pattern
     * @param phone The original phone number
     * @return The formatted phone number
     */
    private static String standardizePhone(String phone) {
        boolean hasBucharestPrefix = phone.substring(0, 3).equals("021") || phone.substring(0, 3).equals("031");

        if (hasBucharestPrefix)
            return phone.substring(0, 3) + PHONE_SEPARATOR
                    + phone.substring(3, 6) + PHONE_SEPARATOR
                    + phone.substring(6, 8) + PHONE_SEPARATOR
                    + phone.substring(8, 10);

        return phone.substring(0, 4) + PHONE_SEPARATOR
                + phone.substring(4, 7) + PHONE_SEPARATOR
                + phone.substring(7, 10);
    }
}
