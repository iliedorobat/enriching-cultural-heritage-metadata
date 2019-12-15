package ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.date;

import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.TimespanRegex;

/**
 * Regular expressions for those time intervals that are stored
 * as a date (having just a year and a month)
 */
public class ShortDateRegex {
    // REGEX_DATE_INTERVAL_SEPARATOR needs to be "([ ]*-[ ]*)" !!!
    public static final String REGEX_DATE_INTERVAL_SEPARATOR = TimespanRegex.REGEX_INTERVAL_DELIMITER;
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;

    // d{3,} allows us to avoid the month-day pattern (E.g.: "noiembrie 22")
    private static final String SHORT_DATE_MY_TEXT = "("
                + TimespanRegex.MONTHS_RO
                + "[ ]+\\d{3,}"
                + TimespanRegex.AD_BC_OPTIONAL
            + ")";

    private static final String DATE_SHORT_MY_START_OPTIONS = "("
                + TEXT_START + SHORT_DATE_MY_TEXT
            + ")";
    private static final String DATE_MY_END_OPTIONS = "("
                + SHORT_DATE_MY_TEXT + TEXT_END
            + ")";

    public static final String DATE_MY_OPTIONS = TimespanRegex.CASE_INSENSITIVE
            + "("
                + TEXT_START + SHORT_DATE_MY_TEXT + TEXT_END
            + ")";
    public static final String DATE_MY_INTERVAL = TimespanRegex.CASE_INSENSITIVE
            + "("
                + "("
                    + DATE_SHORT_MY_START_OPTIONS
                    + REGEX_OR + TimespanRegex.MONTHS_RO
                + ")"
                + REGEX_DATE_INTERVAL_SEPARATOR
                + DATE_MY_END_OPTIONS
            + ")";
}
