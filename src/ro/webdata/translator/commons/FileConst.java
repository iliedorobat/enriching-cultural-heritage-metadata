package ro.webdata.translator.commons;

import ro.webdata.echo.commons.File;

public class FileConst {
    public static final String FILE_NAME_DEMO = "demo3";

    public static final String PATH_INPUT_CIMEC_DIR = File.PATH_INPUT_DIR + File.FILE_SEPARATOR + "cimec";
    public static final String PATH_OUTPUT_CIMEC_DIR = File.PATH_OUTPUT_DIR + File.FILE_SEPARATOR + "cimec2edm";
    public static final String PATH_OUTPUT_CIMEC_DATASET = PATH_OUTPUT_CIMEC_DIR + File.FILE_SEPARATOR + "dataset" + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;
    public static final String PATH_OUTPUT_CIMEC_DEMO = PATH_OUTPUT_CIMEC_DIR + File.FILE_SEPARATOR + "demo" + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;

    public static final String PATH_INPUT_DSPACE_DIR = File.PATH_INPUT_DIR + File.FILE_SEPARATOR + "dspace";
    public static final String PATH_OUTPUT_DSPACE_DIR = File.PATH_OUTPUT_DIR + File.FILE_SEPARATOR + "dspace2edm";
    public static final String PATH_OUTPUT_DSPACE_DEMO = PATH_OUTPUT_DSPACE_DIR + File.FILE_SEPARATOR + "demo" + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;

    public static final String PATH_INPUT_LIDO_DIR = File.PATH_INPUT_DIR + File.FILE_SEPARATOR + "lido";
    public static final String PATH_OUTPUT_LIDO_DIR = File.PATH_OUTPUT_DIR + File.FILE_SEPARATOR + "lido2edm";

    public static final String PATH_OUTPUT_DATASET_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + "dataset" + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;
    public static final String PATH_OUTPUT_DEMO_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + FILE_NAME_DEMO + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;
    public static final String PATH_OUTPUT_PROPERTIES_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + "properties" + File.EXTENSION_SEPARATOR + File.EXTENSION_CSV;

    public static final String PATH_OUTPUT_TIMESPAN_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + "timespan_all" + File.EXTENSION_SEPARATOR + File.EXTENSION_TXT;
    public static final String PATH_OUTPUT_UNIQUE_TIMESPAN_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + "timespan_unique" + File.EXTENSION_SEPARATOR + File.EXTENSION_TXT;
    public static final String PATH_OUTPUT_TIMESPAN_ANALYSIS_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + "timespan-analysis" + File.EXTENSION_SEPARATOR + File.EXTENSION_CSV;
}
