package ro.webdata.lido.convert.edm;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.convert.edm.common.TextUtils;
import ro.webdata.lido.convert.edm.common.constants.FileConstants;
import ro.webdata.lido.convert.edm.common.constants.NSConstants;
import ro.webdata.lido.convert.edm.core.LidoWrapProcessing;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.FullDateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimespanAnalysis;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimespanUtils;
import ro.webdata.lido.convert.edm.vocabulary.EDM;
import ro.webdata.lido.convert.edm.vocabulary.ORE;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        boolean PLAY = true;
//        // 1.
//        TimespanAnalysis.write(fileNames, FileConstatnts.OUTPUT_FILE_TIMESPAN);
        if (PLAY)
            TimespanUtils.read(FileConstants.OUTPUT_FILE_TIMESPAN);

        if (!PLAY)
            TimespanAnalysis.check(FileConstants.OUTPUT_FILE_TIMESPAN);

        if (!PLAY) {
            String str = "1914, aprilie 3";
//            str = "1914 aprilie 3";
//            String replaced = str.replaceAll("mij", "MIJ").replaceAll("lea", "LEA");
//            replaced = StringUtils.stripAccents(replaced);
            String replaced = StringUtils.stripAccents(str);

            Pattern pattern = Pattern.compile(FullDateRegex.DATE_YMD_OPTIONS);
            Matcher matcher = pattern.matcher(replaced);
            while (matcher.find()) {
                String matched = matcher.group();
                System.out.println("matched: \"" + matched + "\"");
            }
//            System.err.println("didn't match: " + string);
        }


        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("dc", DC_11.getURI());
        model.setNsPrefix("dcterms", DCTerms.getURI());
        model.setNsPrefix("edm", EDM.getURI());
        model.setNsPrefix("foaf", FOAF.getURI());
        model.setNsPrefix("ore", ORE.getURI());
        model.setNsPrefix("skos", SKOS.getURI());
        model.setNsPrefix("openData", NSConstants.NS_REPO_PROPERTY + FileConstants.FILE_SEPARATOR);

//        System.out.println(EnvConst.OPERATION_START);
//        if (!EnvConst.IS_DEMO) run(model);
//        else runDemo(model);
//        PrintMessages.printOperation(EnvConst.OPERATION_FINISH);
    }

    //---------------------- Real Scenario ---------------------- //
    private static void run(Model model) {
        for (int i = 0; i < fileNames.length; i++) {
            String filePath = FileConstants.FILE_PATH + FileConstants.FILE_SEPARATOR + fileNames[i] + FileConstants.FILE_EXTENSION;
            lidoWrapProcessing.processing(model, filePath);
        }
        writeRDFGraph(model, FileConstants.OUTPUT_FILE_FULL_PATH);
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo(Model model) {
        // The demo file is found in: files/lido-schema/inp-clasate-arheologie-2014-02-02.xml
        String filePath = FileConstants.FILE_PATH + FileConstants.FILE_SEPARATOR
                + FileConstants.FILE_NAME_DEMO + FileConstants.FILE_EXTENSION;
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