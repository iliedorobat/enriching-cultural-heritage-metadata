package ro.webdata.translator.edm.approach.event.lido.common.constants;

public class FileConstants {
    public static final String RDF_FILE_EXTENSION = ".rdf";
    public static final String XML_FILE_EXTENSION = ".xml";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String WORKSPACE_DIR = System.getProperty("user.dir");

//    public static final String FILE_PATH = WORKSPACE_DIR + FILE_SEPARATOR + "files/lido-schema";
    public static final String FILE_PATH = "files/lido-schema";
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

    public static final String OUTPUT_FILE_PATH = WORKSPACE_DIR + FILE_SEPARATOR + "files";
    public static final String OUTPUT_FILE_FULL_PATH = OUTPUT_FILE_PATH + FILE_SEPARATOR + "output"
            + FILE_SEPARATOR + "dataset" + RDF_FILE_EXTENSION;
    public static final String OUTPUT_DEMO_FILE_FULL_PATH = OUTPUT_FILE_PATH + FILE_SEPARATOR + "output"
            + FILE_SEPARATOR + FILE_NAME_DEMO + RDF_FILE_EXTENSION;
    public static final String OUTPUT_FILE_TIMESPAN = OUTPUT_FILE_PATH + FILE_SEPARATOR + "data-processing"
            + FILE_SEPARATOR + "timespan-original.txt";

    //TODO: demo file name
    //    public static final String TEST_FILE_NAME = "LIDO-Extended-Example.xml";
    public static final String TEST_FILE_NAME = "test-item-istorie";
    public static final String TEST_FILE_PATH = WORKSPACE_DIR + FILE_SEPARATOR + "files/testing";
    public static final String TEST_FILE_FULL_PATH = TEST_FILE_PATH + FILE_SEPARATOR + TEST_FILE_NAME + XML_FILE_EXTENSION;
    public static final String TEST_OUTPUT_FILE_FULL_PATH = TEST_FILE_PATH + FILE_SEPARATOR +
            "output" + FILE_SEPARATOR + "test.rdf";
}
