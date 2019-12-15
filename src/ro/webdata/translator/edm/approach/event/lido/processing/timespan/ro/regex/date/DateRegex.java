package ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.date;

import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.TimespanRegex;

/**
 * Regular expressions for those time intervals that are stored
 * as a date (having a year, a month and a day)
 */
public class DateRegex {
    // REGEX_DATE_INTERVAL_SEPARATOR needs to be "([ ]+-[ ]+)"
    public static final String REGEX_DATE_INTERVAL_SEPARATOR = TimespanRegex.REGEX_DATE_INTERVAL_SEPARATOR;
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;

    // d{3,} allows us to avoid the month-day pattern (E.g.: "noiembrie 22")
    private static final String DATE_DMY_DOT = "("
                + "\\d{1,2}[\\.]{1}\\d{2}[\\.]{1}\\d{3,}"
                + TimespanRegex.AD_BC_OPTIONAL
            + ")";        // E.g.: "01.01.1911"
    private static final String DATE_DMY_SLASH = "("
                + "\\d{1,2}[/]{1}\\d{2}[/]{1}\\d{3,}"
                + TimespanRegex.AD_BC_OPTIONAL
            + ")";          // E.g.:
    private static final String DATE_DMY_TEXT = "("
                + "\\d{1,2}[, ]+"
                + TimespanRegex.MONTHS_RO
                + "[, ]+\\d{3,}"
                + TimespanRegex.AD_BC_OPTIONAL
            + ")";  // E.g.: "9 iulie 1807"
    private static final String DATE_YMD_DASH = "("
                + "\\d{3,}[-]{1}\\d{2}[-]{1}\\d{1,2}"
                + TimespanRegex.AD_BC_OPTIONAL
            + ")";           // E.g.: "1698-10-15"
    private static final String DATE_YMD_TEXT = "("
                + "\\d{3,}[, ]+"
                + TimespanRegex.MONTHS_RO
                + "[, ]+\\d{1,2}"
                + TimespanRegex.AD_BC_OPTIONAL
            + ")";  // E.g.: "1752 aprilie 25"

    private static final String DATE_DMY_START_OPTIONS = "("
                + "(" + TEXT_START + DATE_DMY_DOT + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_DMY_SLASH + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_DMY_TEXT + ")"
            + ")";
    private static final String DATE_YMD_START_OPTIONS = "("
                + "(" + TEXT_START + DATE_YMD_DASH + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_YMD_TEXT + ")"
            + ")";
    private static final String DATE_DMY_END_OPTIONS = "("
                + "(" + DATE_DMY_DOT + TEXT_END + ")" + REGEX_OR
                + "(" + DATE_DMY_SLASH + TEXT_END + ")" + REGEX_OR
                + "(" + DATE_DMY_TEXT + TEXT_END + ")"
            + ")";
    private static final String DATE_YMD_END_OPTIONS = "("
                + "(" + DATE_YMD_DASH + TEXT_END + ")" + REGEX_OR
                + "(" + DATE_YMD_TEXT + TEXT_END + ")"
            + ")";

    public static final String DATE_DMY_OPTIONS = TimespanRegex.CASE_INSENSITIVE
            + "("
                + "(" + TEXT_START + DATE_DMY_DOT + TEXT_END + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_DMY_SLASH + TEXT_END + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_DMY_TEXT + TEXT_END + ")"
            + ")";
    public static final String DATE_YMD_OPTIONS = TimespanRegex.CASE_INSENSITIVE
            + "("
                + "(" + TEXT_START + DATE_YMD_DASH + TEXT_END + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_YMD_TEXT + TEXT_END + ")"
            + ")";

    public static final String DATE_DMY_INTERVAL = TimespanRegex.CASE_INSENSITIVE
            + "("
                + DATE_DMY_START_OPTIONS
                + REGEX_DATE_INTERVAL_SEPARATOR
                + DATE_DMY_END_OPTIONS
            + ")";
    public static final String DATE_YMD_INTERVAL = TimespanRegex.CASE_INSENSITIVE
            + "("
                + DATE_YMD_START_OPTIONS
                + REGEX_DATE_INTERVAL_SEPARATOR
                + DATE_YMD_END_OPTIONS
            + ")";
}
