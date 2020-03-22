package ro.webdata.translator.edm.approach.event.lido.common.constants;

public class FileConstants extends ro.webdata.common.constants.FileConstants {
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

    public static final String PATH_INPUT_LIDO_DIR = PATH_INPUT_DIR + FILE_SEPARATOR + "lido";
    public static final String PATH_OUTPUT_LIDO_DIR = PATH_OUTPUT_DIR + FILE_SEPARATOR + "lido2edm";

    public static final String PATH_OUTPUT_DATASET_FILE = PATH_OUTPUT_LIDO_DIR + FILE_SEPARATOR + "dataset" + FILE_EXTENSION_SEPARATOR + FILE_EXTENSION_RDF;
    public static final String PATH_OUTPUT_DEMO_FILE = PATH_OUTPUT_LIDO_DIR + FILE_SEPARATOR + FILE_NAME_DEMO + FILE_EXTENSION_SEPARATOR + FILE_EXTENSION_RDF;
    public static final String PATH_OUTPUT_PROPERTIES_FILE = PATH_OUTPUT_LIDO_DIR + FILE_SEPARATOR + "properties" + FILE_EXTENSION_SEPARATOR + FILE_EXTENSION_CSV;
    public static final String PATH_OUTPUT_TIMESPAN_FILE = PATH_DATA_PROCESSING_DIR + FILE_SEPARATOR + "timespan" + FILE_EXTENSION_SEPARATOR + FILE_EXTENSION_TXT;
    public static final String PATH_OUTPUT_TIMESPAN_ANALYSIS_FILE = PATH_DATA_PROCESSING_DIR + FILE_SEPARATOR + "timespan-analysis" + FILE_EXTENSION_SEPARATOR + FILE_EXTENSION_CSV;
}
