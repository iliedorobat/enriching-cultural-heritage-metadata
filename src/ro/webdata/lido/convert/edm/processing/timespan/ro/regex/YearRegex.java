package ro.webdata.lido.convert.edm.processing.timespan.ro.regex;

public class YearRegex {
    private static final String BRACKETS_START = "("
                + "?<=[\\[\\(]"
            + ")";
    private static final String BRACKETS_END = "("
                + "?=[\\]\\)]"
            + ")";

    private static final String REGEX_INTERVAL_DELIMITER = TimespanRegex.REGEX_INTERVAL_DELIMITER;
    private static final String AD_BC_OPTIONAL = TimespanRegex.AD_BC_OPTIONAL;

    public static final String YEAR_4_BRACKETS_INTERVAL = "("
//                + BRACKETS_START
                + "("
                    + "\\d{4}" + AD_BC_OPTIONAL
                    + REGEX_INTERVAL_DELIMITER
                    + "\\d{4}" + AD_BC_OPTIONAL
                + ")"
//                + BRACKETS_END
            + ")";
}
