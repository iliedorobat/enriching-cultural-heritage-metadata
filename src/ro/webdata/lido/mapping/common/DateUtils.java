package ro.webdata.lido.mapping.common;

import ro.webdata.lido.mapping.common.constants.Constants;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class DateUtils {
    private DateUtils() {}

    /**
     * Trim the value and replace all commas (",") with an empty placeholder ("").<br/>
     * This pre-processing is used for cases similar with "1914, aprilie 3".
     * @param value The original value
     * @return The prepared value
     */
    public static String prepareDate(String value) {
        return value.replaceAll(",", Constants.EMPTY_VALUE_PLACEHOLDER).trim();
    }

    /**
     * Map a month expressed as a number or as a non-English month name
     * to the corresponding English month name<br/>
     *      E.g.: "1" => "January"<br/>
     *      E.g.: "01" => "January"<br/>
     *      E.g.: "ianuarie" => "January"
     * @param month The number of the month ("1" to "12") or the name of the month
     *              ("ianuarie", "februarie" etc.)
     * @return The name of the month
     */
    public static String getMonthName(String month) {
        int monthNumber = -1;

        try {
            // Treat the case when the month is a number written as a string ("01", "1" etc.)
            monthNumber = Integer.parseInt(month);
        } catch (NumberFormatException ignored) {
            monthNumber = mapMonthToNumber(month);
        }

        return mapNumberToMonth(monthNumber, month);
    }

    /**
     * Map a number to the corresponding English month name<br/>
     * E.g.: 1 => "January"
     * @param monthNumber The number of the month (1 to 12)
     * @param monthName The name of the month ("ianuarie", "februarie" etc.)
     * @return The name of the month
     */
    private static String mapNumberToMonth(int monthNumber, String monthName) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.ENGLISH);
        String month = Constants.UNKNOWN_MONTH;

        try {
            // Exception treated for the case of "1877 en 20" (using TimespanRegex.DATE_TEXT)
            month = dateFormatSymbols.getMonths()[monthNumber - 1];
        } catch (Exception ignored) {
            PrintMessages.printUnknownMonth(monthNumber, monthName);
        }

        return month;
    }

    /**
     * Map a non-English month name to the corresponding number<br/>
     * E.g.: "ianuarie" => 1
     * @param month The month name
     * @return The month number
     */
    private static int mapMonthToNumber(String month) {
        String value = month
                .replaceAll("\\.", Constants.EMPTY_VALUE_PLACEHOLDER)
                .toLowerCase()
                .trim();

        switch (value) {
            case "ianuarie":
            case "ian":
                return 1;
            case "februarie":
            case "fevruarie":
            case "feb":
                return 2;
            case "martie":
            case "mart":
                return 3;
            case "aprilie":
            case "apr":
                return 4;
            case "mai":
                return 5;
            case "iunie":
            case "iumie":
            case "iun":
                return 6;
            case "iulie":
            case "iul":
                return 7;
            case "august":
            case "aug":
                return 8;
            case "septembrie":
            case "sept":
                return 9;
            case "octombrie":
            case "0ctombrie":
            case "oct":
                return 10;
            case "noiembrie":
            case "noimbrie":
            case "nov":
                return 11;
            case "decembrie":
            case "decembre":
            case "dec":
                return 12;
            default:
                return -1;
        }
    }
}
