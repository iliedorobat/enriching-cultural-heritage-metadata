package ro.webdata.lido.mapping.processing.timespan.ro.regex;

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
}
