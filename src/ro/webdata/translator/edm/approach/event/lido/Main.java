package ro.webdata.translator.edm.approach.event.lido;

import org.apache.jena.rdf.model.Model;
import ro.webdata.common.utils.FileUtils;
import ro.webdata.common.utils.ModelUtils;
import ro.webdata.translator.edm.approach.event.lido.common.PrintMessages;
import ro.webdata.translator.edm.approach.event.lido.common.constants.EnvConst;
import ro.webdata.translator.edm.approach.event.lido.common.constants.FileConstants;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.LidoWrapProcessing;

import java.io.StringWriter;

public class Main {
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
        System.out.println(EnvConst.OPERATION_START);
        if (!EnvConst.IS_DEMO) run();
        else runDemo();
        PrintMessages.printOperation(EnvConst.OPERATION_FINISH);
    }

    //---------------------- Real Scenario ---------------------- //
    private static void run() {
        for (int i = 0; i < fileNames.length; i++) {
            Model model = ModelUtils.generateModel();
            String filePath = FileConstants.PATH_INPUT_LIDO_DIR
                    + FileConstants.FILE_SEPARATOR + fileNames[i]
                    + FileConstants.FILE_EXTENSION_SEPARATOR + FileConstants.FILE_EXTENSION_XML;
            lidoWrapProcessing.processing(model, filePath);

            String outputPath = FileConstants.PATH_OUTPUT_LIDO_DIR
                    + FileConstants.FILE_SEPARATOR + fileNames[i]
                    + FileConstants.FILE_EXTENSION_SEPARATOR + FileConstants.FILE_EXTENSION_RDF;
            writeRDFGraph(model, outputPath);
        }
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo() {
        Model model = ModelUtils.generateModel();
        // The demo file is found in: files/lido-schema/inp-clasate-arheologie-2014-02-02.xml
        String filePath = FileConstants.PATH_INPUT_LIDO_DIR
                + FileConstants.FILE_SEPARATOR + FileConstants.FILE_NAME_DEMO
                + FileConstants.FILE_EXTENSION_SEPARATOR + FileConstants.FILE_EXTENSION_XML;
        lidoWrapProcessing.processing(model, filePath);
        writeRDFGraph(model, FileConstants.PATH_OUTPUT_DEMO_FILE);
    }

    private static void writeRDFGraph(Model model, String outputFilePath) {
        StringWriter writer = new StringWriter();
        model.write(writer, ModelUtils.SYNTAX_RDF_XML);
        FileUtils.write(writer, outputFilePath);
//        String result = writer.toString();
//        System.out.println(result);
    }
}
