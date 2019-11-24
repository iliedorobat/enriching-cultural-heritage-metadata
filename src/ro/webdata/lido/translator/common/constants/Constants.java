package ro.webdata.lido.translator.common.constants;

public class Constants {
    public static final char[] CHAR_DELIMITER = new char[]{' '};
    public static final String EMPTY_VALUE_PLACEHOLDER = "";
    public static final String UNDERSCORE_PLACEHOLDER = "_";
    public static final String CENTURY_PLACEHOLDER = "century";
    public static final String MILLENNIUM_PLACEHOLDER = "millennium";
    public static final String DBPEDIA_CENTURY_PLACEHOLDER = UNDERSCORE_PLACEHOLDER + CENTURY_PLACEHOLDER;
    public static final String DBPEDIA_MILLENNIUM_PLACEHOLDER = UNDERSCORE_PLACEHOLDER + MILLENNIUM_PLACEHOLDER;

    public static final String LABEL = "label";
    public static final String LANGUAGE = "language";
    public static final String PREF = "pref";
    public static final String TEXT = "text";

    public static final String LANG_EN = "en";
    public static final String LANG_RO = "ro";
    /**
     * The native dataset language
     * FIXME: change the main language if you need
     */
    public static final String LANG_MAIN = LANG_RO;
    public static final int LAST_UPDATE_CENTURY = 21;
    public static final int LAST_UPDATE_MILLENNIUM = 3;
    public static final int LAST_UPDATE_YEAR = 2014;
    public static final String UNKNOWN_MONTH = "Unknown";

    public static final String CIMEC_LINK_EN = "http://ghidulmuzeelor.cimec.ro/muzeeen.asp?ctext=";
    public static final String CIMEC_LINK_RO = "http://ghidulmuzeelor.cimec.ro/muzee.asp?ctext=";
    public static final String DATA_GOV_LINK_EN = "http://data.gov.ro/en/";
    public static final String DATA_GOV_LINK_RO = "http://data.gov.ro";

    public static final String INP_NAME = "Institutul Național al Patrimoniului";
}
