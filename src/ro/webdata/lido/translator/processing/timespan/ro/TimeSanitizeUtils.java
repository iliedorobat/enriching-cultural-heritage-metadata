package ro.webdata.lido.translator.processing.timespan.ro;

import ro.webdata.lido.translator.common.constants.Constants;
import ro.webdata.lido.translator.processing.timespan.ro.regex.YearRegex;

public class TimeSanitizeUtils {
    /**
     * Sanitize some values that appear rarely and a regex operation
     * would be time consuming
     * @param value The original value
     * @param regex The related regular expression
     * @return The sanitized value
     */
    public static String sanitizeValue(String value, String regex) {
        String sanitized = clearJunks(value, regex);
        sanitized = sanitizeDateTime(sanitized);
        sanitized = sanitizeAges(sanitized);
        sanitized = sanitizeTimePeriods(sanitized);

        return sanitized;
    }

    /**
     * Clean value by junks<br/>
     * E.g.: "anul 13=1800/1801" will lead to "anul 13=" junk value.
     * This junk value could be interpreted by another regex as being
     * a year, which is wrong.
     * @param value The original value
     * @param regex The related regular expression
     * @return The cleaned value
     */
    //TODO: check all the regexes to find if they lead to some junk values
    private static String clearJunks(String value, String regex) {
        // Avoid adding junks that could be interpreted by other regexes.
        // E.g.: "anul 13=1800/1801" will lead to "anul 13=" junk value
        if (regex.equals(YearRegex.YEAR_3_4_DIGITS_SPECIAL_INTERVAL)) {
            return value.replaceAll(
                    YearRegex.YEAR_3_4_DIGITS_SPECIAL_PREFIX,
                    Constants.EMPTY_VALUE_PLACEHOLDER
            );
        }

        return value;
    }

    /**
     * Sanitize date-like values that appear rarely and a regex operation
     * would be time consuming
     * @param value The original value
     * @return The sanitized value
     */
    private static String sanitizeDateTime(String value) {
        switch (value) {
            case "17 nov. 375-9 aug. 378 __BC__":
                return "17 nov. 375 - 9 aug. 378 __BC__";
            case "[11-13 martie] 1528":
                return "11 martie 1528 - 13 martie 1528";
            case "6 octombrie1904":
                return "6 octombrie 1904";
            case "30 mai si 5 august 1796":
                return "30 mai 1796; 5 august 1796";
            case "1861septembrie 25":
                return "25 septembrie 1861";
            case "1908 martie 27-28":
                return "27 martie 1908 - 28 martie 1908";
            case "1834, 1 - 10 aprilie":
                return "1 aprilie 1834 - 10 aprilie 1834";
            default:
                return value;
        }
    }

    /**
     * Sanitize year-like values that appear rarely and a regex operation
     * would be time consuming
     * @param value The original value
     * @return The sanitized value
     */
    private static String sanitizeAges(String value) {
        if (value.equals("1880 (proprietarul avea 90 de ani in 1970)"))
            return "1880";

        return value;
    }

    /**
     * Sanitize centuries and millenniums values that appear rarely
     * and a regex operation would be time consuming
     * @param value The original value
     * @return The sanitized value
     */
    private static String sanitizeTimePeriods(String value) {
        switch (value) {
            case "i p. chr.":
                return "sec. i p.ch.";
            case "i a. chr.-i p. chr.":
                return "sec. i a.ch. - sec. i p.ch.";
            case "ii-i a.chr.":
                return "sec. ii - sec. i a.ch.";
            case "iii i. hr.":
                return "sec. iii a.ch.";
            case "iv - ii i. hr.":
                return "sec. iv - sec. ii a.ch.";
            case "xiii i.e.n":
                return "sec. xiii i.e.n";
            case "xvii":
            case "s: xvii":
                return "sec. xvii";
            case "prima jum. se. xix":
                return "prima jum. sec. xix";
            case "s:20; 1/4":
            case "s: xx; a: 1/4":
            case "s: xx; 1/4":
                return "1/4 sec. xx";
            case "s: xviii; 1/4":
                return "1/4 sec. xviii";
            case "se. iv-vp.ch.":
                return "sec. iv - sec. v p.ch.";
            case "sec. va. chr.":
                return "sec. v a.chr.";
            case "sec.vp.ch.":
                return "sec. v p.ch.";
            case "secolele xix/xx":
                return "sec. xix - sec. xx";
            case "secolul iv/v":
                return "sec. iv - sec. v";
            case "sex. xvii - a ii jumatate":
                return "a doua jumatate a sec. xvii";
            case "xix":
            case "sec: xix":
            case "sex. xix":
            case "secolul al-xix -lea":
                return "sec. xix";
            case "sec. xix/2/2":
                return "2/2 sec. xix";
            case "xi - ix a.hr.":
                return "sec. xi - sec. ix a.hr.";
            case "inc. sex. xx":
                return "inceputul sec. xx";
            case "3/4 ec. xix":
                return "3/4 sec. xix";
            case "s:19; 4/4":
            case "4/4ec. xix":
            case "4/4 secolul al xix/lea":
                return "4/4 sec. xix";
            case "sfarsitul sexc. xix":
                return "sfarsitul sec. xix";
            case "xx":
            case "anii 30-40 secolul xx":
            case "anii 30 ai secolului xx":
            case "prima jumatate a anului '30":
            case "mijlocul anilor '20":
            case "a doua jumatate a anilor '20":
            case "a doua jumatate a anilor '30":
                return "sec. xx";
            case "mijlocul secolului al doilea a.chr.":
                return "mijlocul sec. ii a.ch.";
            default:
                return value;
        }
    }
}
