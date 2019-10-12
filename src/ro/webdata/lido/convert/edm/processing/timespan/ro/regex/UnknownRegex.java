package ro.webdata.lido.convert.edm.processing.timespan.ro.regex;

/**
 * Regular expressions for timespan stored as an unknown time period
 */
public class UnknownRegex {
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;

    public static final String UNKNOWN = "^("
                + "an[ ]{0,1}\\d{1,2}[\\?]{0,1}"
                + REGEX_OR + "cultura inca sau chachapoya\\?"
                + REGEX_OR + "datat"
                + REGEX_OR + "disparuta din uz si din zona; existenta in muzee"
                + REGEX_OR + "epoca severica tarzie"
                + REGEX_OR + "la tene"
                + REGEX_OR + "lama de secol xvi/xvii, garda si manerul ulterioare"
                + REGEX_OR + "leat 7208 \\(1699-1700\\)"
                + REGEX_OR + "mai, dni 15, leat 7232"
                + REGEX_OR + "mesiata.*"
                + REGEX_OR + "fev\\. dni 2 leat 7157"
                + REGEX_OR + "iulie 28"
                + REGEX_OR + "iunie 01"
                + REGEX_OR + "iunie 20, leat 7173 \\(1665\\)"
                + REGEX_OR + "iunie, dni 2, leat 7235"
                + REGEX_OR + "august 04"
                + REGEX_OR + "oct. 10 dni, leat 1730"
                + REGEX_OR + "octombrie 23, 1777"
                + REGEX_OR + "noiembrie 22"
                + REGEX_OR + "noiembrie 24"
            + ")$";
}
