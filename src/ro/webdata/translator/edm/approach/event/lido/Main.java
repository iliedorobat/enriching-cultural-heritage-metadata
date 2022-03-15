package ro.webdata.translator.edm.approach.event.lido;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.EnvConstants;
import ro.webdata.translator.commons.FileConstants;
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
        Print.operation(Const.OPERATION_START, EnvConstants.SHOULD_PRINT);
        if (!EnvConstants.IS_DEMO) run();
        else runDemo();
        Print.operation(Const.OPERATION_END, EnvConstants.SHOULD_PRINT);
    }

    //---------------------- Real Scenario ---------------------- //
    private static void run() {
        for (int i = 0; i < fileNames.length; i++) {
            Model model = GraphModel.generateModel();
            String filePath = FileConstants.PATH_INPUT_LIDO_DIR
                    + File.FILE_SEPARATOR + fileNames[i]
                    + File.EXTENSION_SEPARATOR + File.EXTENSION_XML;
            lidoWrapProcessing.processing(model, filePath);

            String outputPath = FileConstants.PATH_OUTPUT_LIDO_DIR
                    + File.FILE_SEPARATOR + fileNames[i]
                    + File.EXTENSION_SEPARATOR + File.EXTENSION_RDF;
            writeRDFGraph(model, outputPath);
        }
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo() {
        Model model = GraphModel.generateModel();
        // The demo file is found in: files/lido-schema/inp-clasate-arheologie-2014-02-02.xml
        String filePath = FileConstants.PATH_INPUT_LIDO_DIR
                + File.FILE_SEPARATOR + FileConstants.FILE_NAME_DEMO
                + File.EXTENSION_SEPARATOR + File.EXTENSION_XML;
        lidoWrapProcessing.processing(model, filePath);
        writeRDFGraph(model, FileConstants.PATH_OUTPUT_DEMO_FILE);
    }

    private static void writeRDFGraph(Model model, String outputFilePath) {
        StringWriter writer = new StringWriter();
        model.write(writer, GraphModel.SYNTAX_RDF_XML);
        File.write(writer, outputFilePath, false);
//        String result = writer.toString();
//        System.out.println(result);
    }
}
