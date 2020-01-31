package ro.webdata.translator.edm.approach.event.lido;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.common.utils.FileUtils;
import ro.webdata.translator.edm.approach.event.lido.common.PrintMessages;
import ro.webdata.translator.edm.approach.event.lido.common.constants.EnvConst;
import ro.webdata.translator.edm.approach.event.lido.common.constants.FileConstants;
import ro.webdata.translator.edm.approach.event.lido.common.constants.NSConstants;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.LidoWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.ORE;

import java.io.*;
import java.util.*;

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
//        TimespanAnalysis.write(fileNames, FileConstants.PATH_OUTPUT_TIMESPAN_FILE);

//        //TODO: remove
//        boolean PLAY = true;
//        if (PLAY)
//            TimespanUtils.read(FileConstants.PATH_OUTPUT_TIMESPAN_FILE);
//
//        if (!PLAY)
//            TimespanAnalysis.check(FileConstants.PATH_OUTPUT_TIMESPAN_FILE);



        System.out.println(EnvConst.OPERATION_START);
        if (!EnvConst.IS_DEMO) run();
        else runDemo();
        PrintMessages.printOperation(EnvConst.OPERATION_FINISH);

//        test();
    }

    private static Model createDataModel() {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("dc", DC_11.getURI());
        model.setNsPrefix("dcterms", DCTerms.getURI());
        model.setNsPrefix("edm", EDM.getURI());
        model.setNsPrefix("foaf", FOAF.getURI());
        model.setNsPrefix("ore", ORE.getURI());
        model.setNsPrefix("skos", SKOS.getURI());
        model.setNsPrefix("openData", NSConstants.NS_REPO_PROPERTY + FileConstants.FILE_SEPARATOR);
        model.setNsPrefix("dbpedia", "http://dbpedia.org/page/");
        return model;
    }

    //---------------------- Real Scenario ---------------------- //
    private static void run() {
        for (int i = 0; i < fileNames.length; i++) {
            Model model = createDataModel();
            String filePath = FileConstants.PATH_INPUT_LIDO_DIR + FileConstants.FILE_SEPARATOR + fileNames[i] + FileConstants.FILE_EXTENSION_XML;
            lidoWrapProcessing.processing(model, filePath);

            String outputPath = FileConstants.PATH_OUTPUT_LIDO_DIR + FileConstants.FILE_SEPARATOR + fileNames[i] + FileConstants.FILE_EXTENSION_RDF;
            writeRDFGraph(model, outputPath);
        }
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo() {
        Model model = createDataModel();
        // The demo file is found in: files/lido-schema/inp-clasate-arheologie-2014-02-02.xml
        String filePath = FileConstants.PATH_INPUT_LIDO_DIR + FileConstants.FILE_SEPARATOR
                + FileConstants.FILE_NAME_DEMO + FileConstants.FILE_EXTENSION_XML;
        lidoWrapProcessing.processing(model, filePath);
        writeRDFGraph(model, FileConstants.PATH_OUTPUT_DEMO_FILE);
    }

    private static void writeRDFGraph(Model model, String outputFilePath) {
        StringWriter writer = new StringWriter();
        model.write(writer, SYNTAX);
        FileUtils.write(writer, outputFilePath);
//        String result = writer.toString();
//        System.out.println(result);
    }

    private static void test() {
        System.out.println(EnvConst.OPERATION_START);

        HashSet<String> values = new HashSet<>();
        BufferedReader br = null;
        HashMap<String, Integer> map = new HashMap<>();
        ArrayList<String> array = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(FileConstants.PATH_OUTPUT_PROPERTIES_FILE));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    values.add(readLine);
                    array.add(readLine);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file " + FileConstants.PATH_OUTPUT_PROPERTIES_FILE + " have not been found."
                    + "\nError: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>(values);
        Collections.sort(list);

        System.out.println("------------------------------------");
        int total = 0;
        for (String entry : list) {
//            System.out.println(entry);

            int count = 0;
            for (int i = 0; i < array.size(); i++) {
                if (entry.equals(array.get(i)))
                    count++;
            }

            total = total + count;
            System.out.println(entry + ":\t\t" + count);
        }
        System.out.println(total);
        System.out.println("------------------------------------");



        PrintMessages.printOperation(EnvConst.OPERATION_FINISH);
    }
}
