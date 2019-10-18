package ro.webdata.lido.mapping.processing.timespan.ro;

import ro.webdata.lido.mapping.common.constants.Constants;

public class TimeUtils {
    // Labels
    public static final String CHRISTUM_AD_LABEL = "AD";
    public static final String CHRISTUM_BC_LABEL = "BC";

    // Placeholders
    public static final String AFTER_PLACEHOLDER = "POST";
    public static final String APPROXIMATE_PLACEHOLDER = "APPROXIMATE";
    public static final String BEFORE_PLACEHOLDER = "ANTE";
    public static final String CHRISTUM_AD_PLACEHOLDER = "__AD__";
    public static final String CHRISTUM_BC_PLACEHOLDER = "__BC__";
    public static final String INTERVAL_SEPARATOR = " __TO__ ";
    /** From date */
    public static String START_PLACEHOLDER = "START";
    /** To date */
    public static String END_PLACEHOLDER = "END";
    /** Year, Month, Day order */
    public static String YMD_PLACEHOLDER = "YMD";
    /** Day, Month, Year order */
    public static String DMY_PLACEHOLDER = "DMY";
    /** Year, Month order */
    public static String YM_PLACEHOLDER = "YM";
    /** Month, Year order */
    public static String MY_PLACEHOLDER = "MY";


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
        return value.contains(CHRISTUM_BC_PLACEHOLDER)
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
