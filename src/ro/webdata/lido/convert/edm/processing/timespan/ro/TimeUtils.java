package ro.webdata.lido.convert.edm.processing.timespan.ro;

import ro.webdata.lido.convert.edm.common.constants.Constants;

public class TimeUtils {
    public static final String CHRISTUM_AD_LABEL = "AD";
    public static final String CHRISTUM_BC_LABEL = "BC";
    public static final String CHRISTUM_AD_NAME = "__AD__";
    public static final String CHRISTUM_BC_NAME = "__BC__";
    public static final String INTERVAL_SEPARATOR = " __TO__ ";
    /** From date */
    public static String START = "START";
    /** To date */
    public static String END = "END";

    /**
     * Map the prepared era value to simple name.
     * The prepared era value is the original era value which
     * has been processed as following:
     * <ul>
     *     <li>era that is matching with TimespanRegex.CHRISTUM_AD will be mapped to "AD"</li>
     *     <li>era that is matching with TimespanRegex.CHRISTUM_BC will be mapped to "BC"</li>
     * </ul>
     * @param value The input value
     * @return "AD" or "BC"
     */
    public static String getEraLabel(String value) {
        return value.contains(CHRISTUM_BC_NAME)
                ? CHRISTUM_BC_LABEL
                : CHRISTUM_AD_LABEL;
    }

    /**
     * Extract the era from the prepared value.
     * The prepared value is the original value for which
     * era has been processed as following:
     * <ul>
     *     <li>all values that are matching with TimespanRegex.AGE_AD will be mapped to TimespanRegex.CHRISTUM_AD</li>
     *     <li>all values that are matching with TimespanRegex.AGE_BC will be mapped to TimespanRegex.CHRISTUM_BC</li>
     * </ul>
     * @param value The input value
     * @return "__AD__" or "__BC__"
     */
    public static String getEraName(String value) {
        return value.contains(CHRISTUM_BC_NAME)
                ? CHRISTUM_BC_NAME
                : CHRISTUM_AD_NAME;
    }

    /**
     * Remove the Christum notation ("__AD__" and "__BC__")
     * @param value The input value
     * @return The value without Christum notation
     */
    public static String clearChristumNotation(String value) {
        return value
                .replaceAll(CHRISTUM_BC_NAME, Constants.EMPTY_VALUE_PLACEHOLDER)
                .replaceAll(CHRISTUM_AD_NAME, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
    }
}
