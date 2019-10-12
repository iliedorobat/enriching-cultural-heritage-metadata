package ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date;

import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;

public class FullDateRegex {
    public static final String REGEX_DATE_INTERVAL_SEPARATOR = TimespanRegex.REGEX_DATE_INTERVAL_SEPARATOR;
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String MONTHS_RO = TimespanRegex.MONTHS_RO;
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;

    //TODO: add ### + CHRISTUM_NOTATION + "*"###
    //TODO: d{4} => d{3,} ???
    private static final String DATE_DMY_DOT = "(\\d{1,2}[\\.]{1}\\d{2}[\\.]{1}\\d{4})";        // E.g.: 01.01.1911
    private static final String DATE_DMY_SLASH = "(\\d{1,2}[/]{1}\\d{2}[/]{1}\\d{4})";          // E.g.:
    private static final String DATE_DMY_TEXT = "(\\d{1,2}[, ]+" + MONTHS_RO + "[, ]+\\d{4})";  // E.g.: 9 iulie 1807
    private static final String DATE_YMD_DASH = "(\\d{4}[-]{1}\\d{2}[-]{1}\\d{1,2})";           // E.g.: 1698-10-15
    private static final String DATE_YMD_TEXT = "(\\d{4}[, ]+" + MONTHS_RO + "[, ]+\\d{1,2})";  // E.g.: 1752 aprilie 25

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

    public static final String DATE_DMY_OPTIONS = "("
                + "(" + TEXT_START + DATE_DMY_DOT + TEXT_END + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_DMY_SLASH + TEXT_END + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_DMY_TEXT + TEXT_END + ")"
            + ")";
    public static final String DATE_YMD_OPTIONS = "("
                + "(" + TEXT_START + DATE_YMD_DASH + TEXT_END + ")" + REGEX_OR
                + "(" + TEXT_START + DATE_YMD_TEXT + TEXT_END + ")"
            + ")";

    public static final String DATE_DMY_INTERVAL = "("
                + DATE_DMY_START_OPTIONS
                + REGEX_DATE_INTERVAL_SEPARATOR
                + DATE_DMY_END_OPTIONS
            + ")";
    public static final String DATE_YMD_INTERVAL = "("
                + DATE_YMD_START_OPTIONS
                + REGEX_DATE_INTERVAL_SEPARATOR
                + DATE_YMD_END_OPTIONS
            + ")";
}
