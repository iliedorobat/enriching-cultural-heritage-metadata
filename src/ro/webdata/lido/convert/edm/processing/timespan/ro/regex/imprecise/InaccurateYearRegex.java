package ro.webdata.lido.convert.edm.processing.timespan.ro.regex.imprecise;

import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.DateRegex;

public class InaccurateYearRegex {
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String REGEX_INTERVAL_DELIMITER = TimespanRegex.REGEX_INTERVAL_DELIMITER;
    private static final String REGEX_PUNCTUATION_UNLIMITED = TimespanRegex.REGEX_PUNCTUATION_UNLIMITED;

    private static final String AD_BC_OPTIONAL = TimespanRegex.AD_BC_OPTIONAL;
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;

    private static final String APPROX_AGES_GROUP =
            "("
                + "(" + "(catre|probabil|aprox[\\.]*|aproximativ([ ]anii)*|c[a]{0,1}[\\.]{0,1}|cca[\\.]*|circa)[ ]*\\d+" + ")"
                + "(" + REGEX_PUNCTUATION_UNLIMITED + AD_BC_OPTIONAL + ")*"
            + ")";

    public static final String APPROX_AGES_OPTIONS = TEXT_START + APPROX_AGES_GROUP + TEXT_END;
    public static final String APPROX_AGES_INTERVAL = TEXT_START + APPROX_AGES_OPTIONS + REGEX_INTERVAL_DELIMITER
            + "("
                + APPROX_AGES_OPTIONS   // usual cases as "[c. 250-c. 225 a.chr.]"
                + REGEX_OR + "("        // cases as "[c. 260-230]"
                    + "\\d{0,4}" + AD_BC_OPTIONAL
                + ")"
            + ")"
            + TEXT_END;

    private static final String DATE = "("
                + DateRegex.DATE_DMY_OPTIONS
                + REGEX_OR + DateRegex.DATE_YMD_OPTIONS
                + REGEX_OR + "("
                    + TEXT_START + "\\d{1,4}" + AD_BC_OPTIONAL + TEXT_END
                + ")"
            + ")";

    /**
     * E.g.:
     * <ul>
     *     <li>"după 1628"</li>
     *     <li>"dupa 29 aprilie 1616"</li>
     *     <li>"post 330-320 a.chr."</li>
     *     <li>"postum 161 p.chr."</li>
     * </ul>
     */
    public static final String AFTER = TEXT_START + "("
                + "("
                    + "dupa" + REGEX_OR + "post" + REGEX_OR + "postum"
                + ")" + "[ ]*"
                + DATE
            + ")" + TEXT_END;

    public static final String AFTER_INTERVAL =
            AFTER + REGEX_INTERVAL_DELIMITER + DATE;

    /**
     * E.g.:
     * <ul>
     *     <li>"ante 1801"</li>
     *     <li>"anterior lui 1890 (data mortii mesterului)"</li>
     *     <li>"inainte de 211 a.chr."</li>
     * </ul>
     */
    public static final String BEFORE = TEXT_START + "("
                + "("
                    + "ante" + REGEX_OR + "anterior[ ]*lui" + REGEX_OR + "inainte[ ]*de"
                + ")" + "[ ]*"
                + DATE
            + ")" + TEXT_END;

    public static final String BEFORE_INTERVAL =
            BEFORE + REGEX_INTERVAL_DELIMITER + DATE;
}
