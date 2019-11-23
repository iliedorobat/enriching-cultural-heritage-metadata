package ro.webdata.lido.translator.processing.timespan.ro.regex;

public class YearRegex {
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;
    private static final String BRACKETS_START = "("
                + "?<=[\\[\\(]"
            + ")";
    private static final String BRACKETS_END = "("
                + "?=[\\]\\)]"
            + ")";

    private static final String REGEX_INTERVAL_DELIMITER = TimespanRegex.REGEX_INTERVAL_DELIMITER;
    private static final String AD_BC_OPTIONAL = TimespanRegex.AD_BC_OPTIONAL;

    public static final String YEAR_2_DIGITS_OPTIONS = "("
                + TEXT_START + "\\d{2}" + AD_BC_OPTIONAL + TEXT_END
            + ")";
    public static final String YEAR_2_DIGITS_INTERVAL = "("
                + "("
                    + YEAR_2_DIGITS_OPTIONS
                    + REGEX_INTERVAL_DELIMITER
                    + YEAR_2_DIGITS_OPTIONS
                + ")"
            + ")";

    public static final String YEAR_3_4_DIGITS_OPTIONS = "("
                + TEXT_START + "\\d{3,4}" + AD_BC_OPTIONAL + TEXT_END
            + ")";
    public static final String YEAR_3_4_DIGITS_INTERVAL = "("
                + "("
                    + YEAR_3_4_DIGITS_OPTIONS
                    + REGEX_INTERVAL_DELIMITER
                    + YEAR_3_4_DIGITS_OPTIONS
                + ")"
            + ")";

    public static final String YEAR_3_4_DIGITS_SPECIAL_PREFIX = "("
                + "anul[ ]*\\d{1,2}="
            + ")";
    // "anul 13=1800/1801"; "110/109 a. chr."
    public static final String YEAR_3_4_DIGITS_SPECIAL_INTERVAL = TEXT_START + "("
                + "\\d{3,4}" + AD_BC_OPTIONAL
                + "/"
                + "\\d{3,4}" + AD_BC_OPTIONAL
            + ")" + TEXT_END;

    // "(1)838"; "15(6)3"; "173[1]"; "184(5)"; "1700(?!)"; "(15…)"
    public static final String UNKNOWN_YEARS = "("
                + "(" + "[\\[\\(\\]\\)\\?\\!\\d\\…]{5,}" + ")"
            + ")";
}
