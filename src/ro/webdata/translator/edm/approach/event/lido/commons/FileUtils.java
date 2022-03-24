package ro.webdata.translator.edm.approach.event.lido.commons;

import ro.webdata.translator.commons.FileConstants;

import static ro.webdata.echo.commons.File.*;
import static ro.webdata.echo.commons.File.EXTENSION_RDF;

public class FileUtils {
    public static String getInputFilePath(String fileName) {
        return FileConstants.PATH_INPUT_LIDO_DIR
                + FILE_SEPARATOR + fileName
                + EXTENSION_SEPARATOR + EXTENSION_XML;
    }

    public static String getOutputFilePath(String fileName) {
        return FileConstants.PATH_OUTPUT_LIDO_DIR
                + FILE_SEPARATOR + fileName
                + EXTENSION_SEPARATOR + EXTENSION_RDF;
    }
}
