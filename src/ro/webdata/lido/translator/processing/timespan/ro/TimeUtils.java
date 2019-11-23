package ro.webdata.lido.translator.processing.timespan.ro;

import ro.webdata.lido.translator.common.constants.Constants;

public class TimeUtils {
    /** "Anno Domini" label */
    public static final String CHRISTUM_AD_LABEL = "AD";
    /** "Before Christ" label */
    public static final String CHRISTUM_BC_LABEL = "BC";

    /** Approximate date placeholder */
    public static final String APPROXIMATE_PLACEHOLDER = "APPROXIMATE";
    /** Unknown date placeholder */
    public static final String UNKNOWN_DATE_PLACEHOLDER = "UNKNOWN";
    /** "Anno Domini" placeholder */
    public static final String CHRISTUM_AD_PLACEHOLDER = "__AD__";
    /** "Before Christ" placeholder */
    public static final String CHRISTUM_BC_PLACEHOLDER = "__BC__";
    /** Placeholder used to separate two dates from an interval */
    public static final String INTERVAL_SEPARATOR_PLACEHOLDER = " __TO__ ";
    /** "from date" placeholder */
    public static String START_PLACEHOLDER = "START";
    /** "to date" placeholder */
    public static String END_PLACEHOLDER = "END";
    /** "year, month, day" pattern placeholder */
    public static String YMD_PLACEHOLDER = "YMD";
    /** "day, month, year" pattern placeholder */
    public static String DMY_PLACEHOLDER = "DMY";
    /** "year, month" pattern placeholder */
    public static String YM_PLACEHOLDER = "YM";
    /** "month, year" pattern placeholder */
    public static String MY_PLACEHOLDER = "MY";


    /**
     * Map the era name to era label.<br/>
     * The era name is the original era value which has been
     * processed by using <b>getEraName</b> method
     * @param value The input value
     * @return "AD" or "BC"
     */
    public static String getEraLabel(String value) {
        return value.contains(CHRISTUM_BC_PLACEHOLDER)
                ? CHRISTUM_BC_LABEL
                : CHRISTUM_AD_LABEL;
    }

    /**
     * Extract the era from the prepared value.<br/>
     * The prepared value is the original value for which
     * era has been processed as following:
     * <ul>
     *     <li>era which are matching to TimespanRegex.AGE_BC will be mapped to CHRISTUM_BC_PLACEHOLDER</li>
     *     <li>era which are matching to TimespanRegex.AGE_AD will be mapped to CHRISTUM_AD_PLACEHOLDER</li>
     * </ul>
     * @param value The input value
     * @return "__AD__" or "__BC__"
     */
    public static String getEraName(String value) {
        return value.contains(CHRISTUM_BC_PLACEHOLDER)
                ? CHRISTUM_BC_PLACEHOLDER
                : CHRISTUM_AD_PLACEHOLDER;
    }

    /**
     * Remove the Christum notation ("__AD__" and "__BC__")
     * @param value The input value
     * @return The value without Christum notation
     */
    public static String clearChristumNotation(String value) {
        return value
                .replaceAll(CHRISTUM_BC_PLACEHOLDER, Constants.EMPTY_VALUE_PLACEHOLDER)
                .replaceAll(CHRISTUM_AD_PLACEHOLDER, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
    }
}
