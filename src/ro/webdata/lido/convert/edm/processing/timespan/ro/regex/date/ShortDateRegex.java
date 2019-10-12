package ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date;

import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;

public class ShortDateRegex {
    // REGEX_DATE_INTERVAL_SEPARATOR needs to be "([ ]*-[ ]*)" !!!
    public static final String REGEX_DATE_INTERVAL_SEPARATOR = TimespanRegex.REGEX_INTERVAL_DELIMITER;
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String MONTHS_RO = TimespanRegex.MONTHS_RO;
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;

    // d{3,} allows us to avoid taking the month-day pattern (E.g.: "noiembrie 22")
    private static final String SHORT_DATE_MY_TEXT = "(" + MONTHS_RO + "[ ]+\\d{3,}" + ")";

    private static final String DATE_SHORT_MY_START_OPTIONS = "(" + TEXT_START + SHORT_DATE_MY_TEXT + ")";
    private static final String DATE_MY_END_OPTIONS = "(" + SHORT_DATE_MY_TEXT + TEXT_END + ")";

    public static final String DATE_MY_OPTIONS = "(" + TEXT_START + SHORT_DATE_MY_TEXT + TEXT_END + ")";
    public static final String DATE_MY_INTERVAL =
            "("
                + "("
                    + DATE_SHORT_MY_START_OPTIONS
                    + REGEX_OR + MONTHS_RO
                + ")"
                + REGEX_DATE_INTERVAL_SEPARATOR
                + DATE_MY_END_OPTIONS
            + ")";
}
