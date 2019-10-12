package ro.webdata.lido.convert.edm.processing.timespan.ro.regex;

import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.FullDateRegex;

public class InaccurateRegex {
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String REGEX_INTERVAL_DELIMITER = TimespanRegex.REGEX_INTERVAL_DELIMITER;
    private static final String REGEX_PUNCTUATION_UNLIMITED = TimespanRegex.REGEX_PUNCTUATION_UNLIMITED;

    private static final String CHRISTUM_NOTATION = TimespanRegex.CHRISTUM_NOTATION;
    private static final String CHRISTUM_SUFFIX_NOTATION = TimespanRegex.CHRISTUM_SUFFIX_NOTATION;
    //TODO: check if the additional "[" affects the TEXT_START from TimespanRegex
    private static final String TEXT_START =
            "("
                + "?<=" + "("
                    + "^" + REGEX_OR + "\\A" + REGEX_OR + "[\\.,;\\?!\\-(\\[ ]+"
                + ")"
            + ")";
    //TODO: check if the additional "]" affects the TEXT_END from TimespanRegex
    private static final String TEXT_END =
            "("
                + "?=" + "("
                    + "$" + REGEX_OR + "\\z" + REGEX_OR + "[\\.,;\\?!\\-)\\] ]+"
                + ")"
            + ")";

    private static final String APPROX_AGES_GROUP =
            "("
                + "(" + "(catre|probabil|aprox[\\.]*|aproximativ([ ]anii)*|c[a]{0,1}[\\.]{0,1}|cca[\\.]*|circa)[ ]*\\d+" + ")"
                + "(" + REGEX_PUNCTUATION_UNLIMITED + CHRISTUM_NOTATION + "{0,1}" + ")*"
            + ")";

    public static final String APPROX_AGES_OPTIONS = TEXT_START + APPROX_AGES_GROUP + TEXT_END;
    public static final String APPROX_AGES_INTERVAL = TEXT_START + APPROX_AGES_OPTIONS + REGEX_INTERVAL_DELIMITER
            + "("
                + APPROX_AGES_OPTIONS   // usual cases as "[c. 250-c. 225 a.chr.]"
                + REGEX_OR + "("        // cases as "[c. 260-230]"
                    + "\\d{0,4}" + REGEX_PUNCTUATION_UNLIMITED + CHRISTUM_NOTATION + "{0,1}"
                + ")"
            + ")"
            + TEXT_END;

    public static final String AFTER = TEXT_START + "("
                + "(dupa|post|postum)[ ]*("
                    + FullDateRegex.DATE_DMY_OPTIONS
                    + REGEX_OR + FullDateRegex.DATE_YMD_OPTIONS
                    + REGEX_OR + "("
                        + TEXT_START + "\\d{1,4}" + CHRISTUM_SUFFIX_NOTATION + TEXT_END
                    + ")"
                + ")"
            + ")" + TEXT_END;   // E.g.: "după 1628"; "dupa 29 aprilie 1616"
    public static final String BEFORE = TEXT_START + "("
                + "(ante|anterior[ ]*lui|inainte[ ]*de)[ ]*[\\d]{1,4}" + CHRISTUM_SUFFIX_NOTATION
            + ")" + TEXT_END;   // E.g.: "inainte de 211 a.chr."; "ante 1940"; "anterior lui 1890"

    private static final String MODEL_X = "(" + TEXT_START
                + "model[ ]*\\d{4}"
            + ")" + TEXT_END;   // E.g.: "model 1850"
    private static final String UNDATED = "("
                + ".*("
                    + "nedatat"+ REGEX_OR + "nedatabil" + REGEX_OR + "nu[ ]*are"
                + ").*"
            + ")";  // E.g.: "nedatat (1897)"; "1910 (nedatat)"; "nedatabil"; "nu are"
    private static final String WITHOUT_AGE = "("
                + ".*("
                    + "(fara[ ]*an)" + REGEX_OR + "(f\\.[ ]*an)" + REGEX_OR + "(f\\.a)"
                + ")"
            + ")";  // "f.a. octombrie 29"; "f.an"
    private static final String WITHOUT_DATE = "("
                + "("
                    + "(fara[ ]*data)" + REGEX_OR + "(f\\.[ ]*data)" + REGEX_OR + "(f\\.d)"
                + ")"
            + ").*";    // "fara data"; "1861 f.d"
    public static final String DATELESS = "("
                + MODEL_X
                + REGEX_OR + UNDATED
                + REGEX_OR + WITHOUT_AGE
                + REGEX_OR + WITHOUT_DATE
            + ")";
}
