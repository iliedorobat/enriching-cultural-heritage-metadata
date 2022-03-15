package ro.webdata.translator.edm.approach.event.cimec.commons;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.echo.commons.File;

public class FilePath {
    public static String getCimecJsonPath(String lang) {
        return File.PATH_INPUT_DIR
                + File.FILE_SEPARATOR
                + "cimec"
                + File.FILE_SEPARATOR
                + "merged"
                + StringUtils.capitalize(lang)
                + "PreparedData"
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }
}
