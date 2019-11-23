package ro.webdata.lido.translator;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.translator.common.PrintMessages;
import ro.webdata.lido.translator.common.TextUtils;
import ro.webdata.lido.translator.common.constants.EnvConst;
import ro.webdata.lido.translator.common.constants.FileConstants;
import ro.webdata.lido.translator.common.constants.NSConstants;
import ro.webdata.lido.translator.mapping.core.LidoWrapProcessing;
import ro.webdata.lido.translator.vocabulary.EDM;
import ro.webdata.lido.translator.vocabulary.ORE;

import java.io.StringWriter;

public class Main {
    private static final String SYNTAX = "RDF/XML";
//    public static final String SYNTAX = "turtle";
//    private static final String SYNTAX = "N3";
    private static LidoWrapProcessing lidoWrapProcessing = new LidoWrapProcessing();
    private static String[] fileNames = {
            FileConstants.FILE_NAME_ARHEOLOGIE,
            FileConstants.FILE_NAME_ARTA,
            FileConstants.FILE_NAME_ARTE_DECO,
            FileConstants.FILE_NAME_DOC,
            FileConstants.FILE_NAME_ETNO,
            FileConstants.FILE_NAME_ST_TEH,
            FileConstants.FILE_NAME_ISTORIE,
            FileConstants.FILE_NAME_MEDALISTICA,
            FileConstants.FILE_NAME_NUMISMATICA,
            FileConstants.FILE_NAME_ST_NAT
    };

    public static void main(String[] args) {
//        // 1. Write to the disc all unique timespan values
//        TimespanAnalysis.write(fileNames, FileConstants.OUTPUT_FILE_TIMESPAN);

//        //TODO: remove
//        boolean PLAY = true;
//        if (PLAY)
//            TimespanUtils.read(FileConstants.OUTPUT_FILE_TIMESPAN);
//
//        if (!PLAY)
//            TimespanAnalysis.check(FileConstants.OUTPUT_FILE_TIMESPAN);

        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("dc", DC_11.getURI());
        model.setNsPrefix("dcterms", DCTerms.getURI());
        model.setNsPrefix("edm", EDM.getURI());
        model.setNsPrefix("foaf", FOAF.getURI());
        model.setNsPrefix("ore", ORE.getURI());
        model.setNsPrefix("skos", SKOS.getURI());
        model.setNsPrefix("openData", NSConstants.NS_REPO_PROPERTY + FileConstants.FILE_SEPARATOR);

        System.out.println(EnvConst.OPERATION_START);
        if (!EnvConst.IS_DEMO) run(model);
        else runDemo(model);
        PrintMessages.printOperation(EnvConst.OPERATION_FINISH);
    }

    //---------------------- Real Scenario ---------------------- //
    private static void run(Model model) {
        for (int i = 0; i < fileNames.length; i++) {
            String filePath = FileConstants.FILE_PATH + FileConstants.FILE_SEPARATOR + fileNames[i] + FileConstants.XML_FILE_EXTENSION;
            lidoWrapProcessing.processing(model, filePath);
        }
        writeRDFGraph(model, FileConstants.OUTPUT_FILE_FULL_PATH);
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo(Model model) {
        // The demo file is found in: files/lido-schema/inp-clasate-arheologie-2014-02-02.xml
        String filePath = FileConstants.FILE_PATH + FileConstants.FILE_SEPARATOR
                + FileConstants.FILE_NAME_DEMO + FileConstants.XML_FILE_EXTENSION;
        lidoWrapProcessing.processing(model, filePath);
        writeRDFGraph(model, FileConstants.OUTPUT_FILE_FULL_PATH);
    }

    private static void writeRDFGraph(Model model, String outputFilePath) {
        StringWriter writer = new StringWriter();
        model.write(writer, SYNTAX);
        TextUtils.write(writer, outputFilePath);
//        String result = writer.toString();
//        System.out.println(result);
    }
}
