package ro.webdata.common.constants;

public class FileConstants {
    public static final String FILE_EXTENSION_CSV = "csv";
    public static final String FILE_EXTENSION_RDF = "rdf";
    public static final String FILE_EXTENSION_TXT = "txt";
    public static final String FILE_EXTENSION_XML = "xml";
    public static final String FILE_EXTENSION_SEPARATOR = ".";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String WORKSPACE_DIR = System.getProperty("user.dir");

    public static final String PATH_FILES_DIR = WORKSPACE_DIR + FILE_SEPARATOR + "files";
    public static final String PATH_DATA_PROCESSING_DIR = PATH_FILES_DIR + FILE_SEPARATOR + "data-processing";
    public static final String PATH_INPUT_DIR = PATH_FILES_DIR + FILE_SEPARATOR + "input";
    public static final String PATH_OUTPUT_DIR = PATH_FILES_DIR + FILE_SEPARATOR + "output";
}
