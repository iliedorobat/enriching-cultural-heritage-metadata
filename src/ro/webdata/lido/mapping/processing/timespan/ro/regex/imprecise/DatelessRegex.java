package ro.webdata.lido.mapping.processing.timespan.ro.regex.imprecise;

import ro.webdata.lido.mapping.processing.timespan.ro.regex.TimespanRegex;

public class DatelessRegex {
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;

    /**
     * E.g.:
     * <ul>
     *     <li>"model 1850"</li>
     * </ul>
     */
    private static final String MODEL_X = "(" + TEXT_START
                + "model[ ]*\\d{4}"
            + ")" + TEXT_END;

    /**
     * E.g.:
     * <ul>
     *     <li>"nedatat (1897)"</li>
     *     <li>"1910 (nedatat)"</li>
     *     <li>"nedatabil"</li>
     *     <li>"nu are"</li>
     * </ul>
     */
    private static final String UNDATED = "("
                + ".*("
                    + "nedatat"+ REGEX_OR
                    + "nedatabil" + REGEX_OR
                    + "nu[ ]*are"
                + ").*"
            + ")";

    /**
     * E.g.:
     * <ul>
     *     <li>"f.a. octombrie 29"; "f.an"</li>
     * </ul>
     */
    private static final String WITHOUT_AGE = "("
                + ".*("
                    + "(fara[ ]*an)" + REGEX_OR
                    + "(f\\.[ ]*an)" + REGEX_OR
                    + "(f\\.a)"
                + ")"
            + ")";

    /**
     * E.g.:
     * <ul>
     *     <li>"fara data"</li>
     *     <li>"1861 f.d"</li>
     * </ul>
     */
    private static final String WITHOUT_DATE = "("
                + "("
                    + "(fara[ ]*data)" + REGEX_OR
                    + "(f\\.[ ]*data)" + REGEX_OR
                    + "(f\\.d)"
                + ")"
            + ").*";

    /**
     * E.g.:
     * <ul>
     *     <li>"model 1850"</li>
     *     <li>"nedatat (1897)"</li>
     *     <li>"1910 (nedatat)"</li>
     *     <li>"nedatabil"</li>
     *     <li>"nu are"</li>
     *     <li>"f.a. octombrie 29"; "f.an"</li>
     *     <li>"fara data"</li>
     *     <li>"1861 f.d"</li>
     * </ul>
     */
    public static final String DATELESS = TimespanRegex.CASE_INSENSITIVE
            + "("
                + MODEL_X + REGEX_OR
                + UNDATED + REGEX_OR
                + WITHOUT_AGE + REGEX_OR
                + WITHOUT_DATE
            + ")";
}
