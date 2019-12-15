package ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex;

/**
 * Regular expressions for those time intervals that are stored
 * as an unknown time period
 */
public class UnknownRegex {
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;

    public static final String UNKNOWN = TimespanRegex.CASE_INSENSITIVE
            + "^("
                + "-"
                + REGEX_OR + "4/4 sec\\."
                + REGEX_OR + "17 nov\\. 375\\-9 aug\\. 378 a\\.chr\\."
                + REGEX_OR + "1884 martie 28/aprilie 09"
                + REGEX_OR + "189-45"
                + REGEX_OR + "al doile afert al s1900"
                + REGEX_OR + "an[ ]{0,1}\\d{1,2}[\\?]{0,1}"
                + REGEX_OR + "cultura inca sau chachapoya\\?"
                + REGEX_OR + "datat"
                + REGEX_OR + "dinastia xxv"
                + REGEX_OR + "dinastia xxvi \\(dinastia saita\\)"
                + REGEX_OR + "dinastia xxvi \\(perioada saita\\)"
                + REGEX_OR + "dinastia xxvii \\(prima stapanire persana\\)"
                + REGEX_OR + "nesemnat"
                + REGEX_OR + "disparuta din uz si din zona; existenta in muzee"
                + REGEX_OR + "epoca severica tarzie"
                + REGEX_OR + "grupa a iv-a"
                + REGEX_OR + "grupa a v-a"
                + REGEX_OR + "grupa iii"
                + REGEX_OR + "grupa iv"
                + REGEX_OR + "grupa v"
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
                + REGEX_OR + "oct\\. 10 dni, leat 1730"
                + REGEX_OR + "octombrie 23, 1777"
                + REGEX_OR + "noiembrie 22"
                + REGEX_OR + "noiembrie 24"
                + REGEX_OR + "perioada lui carol x al frantei"
                + REGEX_OR + "perioada domniei regelui carol al ii-lea"
                + REGEX_OR + "perioada regelui carol al ii-lea"
                + REGEX_OR + "regatul nou\\?"
                + REGEX_OR + "21/2, sc\\.i, a\\.c\\."
                + REGEX_OR + "21/2; sc i a\\.c\\."
                + REGEX_OR + "286/5-282/1"
                + REGEX_OR + "286/5-282/1 a\\. chr"
                + REGEX_OR + "15\\(…\\)4"
                + REGEX_OR + "154\\(…\\)"
                + REGEX_OR + "158\\(…\\)"
                + REGEX_OR + "162\\(\\?\\)"
                + REGEX_OR + "16\\[ \\]"
            + ")$";
}
