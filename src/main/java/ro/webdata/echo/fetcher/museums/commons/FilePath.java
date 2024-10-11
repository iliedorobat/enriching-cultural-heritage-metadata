package ro.webdata.echo.fetcher.museums.commons;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.echo.commons.File;

public class FilePath {
    public static final String SEPARATOR_PIPE = "\\|";

    private static final String MAIN_INPUT_PATH = "files/input/web/";
    private static final String MAIN_OUTPUT_PATH = "files/output/web/";

    private static final String DIRECTORY_RAW_DATA = "raw-data/";
    private static final String DIRECTORY_PREPARED_DATA = "prepared-data/";

    private static final String PREFIX_CIMEC = "cimec";
    private static final String PREFIX_INP = "inp";
    private static final String PREFIX_GEO_LOCATION = "geoLocation";
    private static final String PREFIX_MERGED = "merged";
    private static final String FILE_NAME_INP_GUIDE = "inp-ghidul-muzelor-2017-05-18";
    private static final String FILE_NAME_ERROR = "Error";
    private static final String FILE_NAME_PREPARED_DATA = "PreparedData";
    private static final String FILE_NAME_RAW_DATA = "RawData";

    public static String getErrorFilePath() {
        return FilePath.MAIN_OUTPUT_PATH
                + FilePath.FILE_NAME_ERROR
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_TXT;
    }

    public static String getInpGuidePath() {
        return MAIN_INPUT_PATH
                + FILE_NAME_INP_GUIDE
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_CSV;
    }

    public static String getCimecPreparedJsonPath(String lang) {
        return MAIN_OUTPUT_PATH
                + DIRECTORY_PREPARED_DATA
                + PREFIX_CIMEC
                + StringUtils.capitalize(lang)
                + FILE_NAME_PREPARED_DATA
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }

    public static String getCimecRawJsonPath(String lang) {
        return MAIN_OUTPUT_PATH
                + DIRECTORY_RAW_DATA
                + PREFIX_CIMEC
                + StringUtils.capitalize(lang)
                + FILE_NAME_RAW_DATA
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }

    public static String getInpRawJsonPath(String lang) {
        return MAIN_OUTPUT_PATH
                + DIRECTORY_RAW_DATA
                + PREFIX_INP
                + StringUtils.capitalize(lang)
                + FILE_NAME_RAW_DATA
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }

    public static String getInpPreparedJsonPath(String lang) {
        return MAIN_OUTPUT_PATH
                + DIRECTORY_PREPARED_DATA
                + PREFIX_INP
                + StringUtils.capitalize(lang)
                + FILE_NAME_PREPARED_DATA
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }

    public static String getFinalJsonPath(String lang) {
        return MAIN_OUTPUT_PATH
                + DIRECTORY_PREPARED_DATA
                + PREFIX_MERGED
                + StringUtils.capitalize(lang)
                + FILE_NAME_PREPARED_DATA
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }

    public static String getGeoLocationJsonPath(String lang) {
        return MAIN_OUTPUT_PATH
                + DIRECTORY_PREPARED_DATA
                + PREFIX_GEO_LOCATION
                + StringUtils.capitalize(lang)
                + FILE_NAME_PREPARED_DATA
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }
}
