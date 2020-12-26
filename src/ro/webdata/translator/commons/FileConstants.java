package ro.webdata.translator.commons;

import ro.webdata.echo.commons.File;

public class FileConstants {
    //TODO: automatically parse the "files/input/lido" files
    public static final String FILE_NAME_ARHEOLOGIE = "inp-clasate-arheologie-2014-02-02";
    public static final String FILE_NAME_ARTA = "inp-clasate-arta-2014-02-02";
    public static final String FILE_NAME_ARTE_DECO = "inp-clasate-arte-decorative-2014-02-02";
    public static final String FILE_NAME_DOC = "inp-clasate-documente-2014-02-02";
    public static final String FILE_NAME_ETNO = "inp-clasate-etnografie-2014-02-02";
    public static final String FILE_NAME_ST_TEH = "inp-clasate-istoria-stiintei-si-tehnicii-2014-02-02";
    public static final String FILE_NAME_ISTORIE = "inp-clasate-istorie-2014-02-02";
    public static final String FILE_NAME_MEDALISTICA = "inp-clasate-medalistica-2014-02-02";
    public static final String FILE_NAME_NUMISMATICA = "inp-clasate-numismatica-2014-02-02";
    public static final String FILE_NAME_ST_NAT = "inp-clasate-stiintele-naturii-2014-02-03-7";
    public static final String FILE_NAME_DEMO = "demo3";

    public static final String PATH_INPUT_DSPACE_DIR = File.PATH_INPUT_DIR + File.FILE_SEPARATOR + "dspace";
    public static final String PATH_OUTPUT_DSPACE_DIR = File.PATH_OUTPUT_DIR + File.FILE_SEPARATOR + "dspace2edm";

    public static final String PATH_INPUT_LIDO_DIR = File.PATH_INPUT_DIR + File.FILE_SEPARATOR + "lido";
    public static final String PATH_OUTPUT_LIDO_DIR = File.PATH_OUTPUT_DIR + File.FILE_SEPARATOR + "lido2edm";

    public static final String PATH_OUTPUT_DATASET_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + "dataset" + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;
    public static final String PATH_OUTPUT_DEMO_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + FILE_NAME_DEMO + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;
    public static final String PATH_OUTPUT_PROPERTIES_FILE = PATH_OUTPUT_LIDO_DIR + File.FILE_SEPARATOR + "properties" + File.EXTENSION_SEPARATOR + File.EXTENSION_CSV;
    public static final String PATH_OUTPUT_TIMESPAN_FILE = File.PATH_DATA_PROCESSING_DIR + File.FILE_SEPARATOR + "timespan" + File.EXTENSION_SEPARATOR + File.EXTENSION_TXT;
    public static final String PATH_OUTPUT_TIMESPAN_ANALYSIS_FILE = File.PATH_DATA_PROCESSING_DIR + File.FILE_SEPARATOR + "timespan-analysis" + File.EXTENSION_SEPARATOR + File.EXTENSION_CSV;
}
