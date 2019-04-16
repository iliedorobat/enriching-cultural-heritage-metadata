package ro.webdata.lido.convert.edm;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.convert.edm.common.PrintMessages;
import ro.webdata.lido.convert.edm.common.TextUtils;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.common.constants.FileConstatnts;
import ro.webdata.lido.convert.edm.common.constants.NSConstants;
import ro.webdata.lido.convert.edm.core.LidoWrapProcessing;
import ro.webdata.lido.convert.edm.vocabulary.EDM;
import ro.webdata.lido.convert.edm.vocabulary.ORE;

import java.io.StringWriter;

public class Main {
    private static final String SYNTAX = "RDF/XML";
//    public static final String SYNTAX = "turtle";
//    private static final String SYNTAX = "N3";
    private static LidoWrapProcessing lidoWrapProcessing = new LidoWrapProcessing();
    private static String[] fileNames = {
            FileConstatnts.FILE_NAME_ARHEOLOGIE,
            FileConstatnts.FILE_NAME_ARTA,
            FileConstatnts.FILE_NAME_ARTE_DECO,
            FileConstatnts.FILE_NAME_DOC,
            FileConstatnts.FILE_NAME_ETNO,
            FileConstatnts.FILE_NAME_ST_TEH,
            FileConstatnts.FILE_NAME_ISTORIE,
            FileConstatnts.FILE_NAME_MEDALISTICA,
            FileConstatnts.FILE_NAME_NUMISMATICA,
            FileConstatnts.FILE_NAME_ST_NAT
    };

    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("dc", DC_11.getURI());
        model.setNsPrefix("dcterms", DCTerms.getURI());
        model.setNsPrefix("edm", EDM.getURI());
        model.setNsPrefix("foaf", FOAF.getURI());
        model.setNsPrefix("ore", ORE.getURI());
        model.setNsPrefix("skos", SKOS.getURI());
        model.setNsPrefix("openData", NSConstants.NS_REPO_PROPERTY + FileConstatnts.FILE_SEPARATOR);

        System.out.println(Constants.OPERATION_START);
        for (int i = 0; i < fileNames.length; i++) {
            String filePath = FileConstatnts.FILE_PATH + FileConstatnts.FILE_SEPARATOR + fileNames[i] + FileConstatnts.FILE_EXTENSION;
            lidoWrapProcessing.processing(model, filePath);
        }

        writeRDFGraph(model, FileConstatnts.OUTPUT_FILE_FULL_PATH);

        PrintMessages.printOperation(Constants.OPERATION_FINISH);
    }

    private static void writeRDFGraph(Model model, String outputFilePath) {
        StringWriter writer = new StringWriter();
        model.write(writer, SYNTAX);
        TextUtils.write(writer, outputFilePath);
//        String result = writer.toString();
//        System.out.println(result);
    }
}
