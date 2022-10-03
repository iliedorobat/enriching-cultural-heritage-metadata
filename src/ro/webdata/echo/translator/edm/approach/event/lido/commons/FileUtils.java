package ro.webdata.echo.translator.edm.approach.event.lido.commons;

import ro.webdata.echo.translator.commons.FileConst;

import static ro.webdata.echo.commons.File.*;
import static ro.webdata.echo.commons.File.EXTENSION_RDF;

public class FileUtils {
    public static String getInputFilePath(String fileName) {
        return FileConst.PATH_INPUT_LIDO_DIR
                + FILE_SEPARATOR + fileName
                + EXTENSION_SEPARATOR + EXTENSION_XML;
    }

    public static String getOutputFilePath(String fileName) {
        return FileConst.PATH_OUTPUT_LIDO_DIR
                + FILE_SEPARATOR + fileName
                + EXTENSION_SEPARATOR + EXTENSION_RDF;
    }
}
