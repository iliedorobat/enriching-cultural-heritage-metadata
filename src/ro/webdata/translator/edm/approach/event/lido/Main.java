package ro.webdata.translator.edm.approach.event.lido;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.FileConstants;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.LidoWrapProcessing;

import java.io.File;

import static ro.webdata.translator.commons.EnvConstants.*;
import static ro.webdata.echo.commons.File.*;

public class Main {
    public static void main(String[] args) {
        Print.operation(Const.OPERATION_START, IS_PRINT_ENABLED);

        if (!IS_DEMO) {
            run();
        } else {
            runDemo();
        }

        Print.operation(Const.OPERATION_END, IS_PRINT_ENABLED);
    }

    //---------------------- Real Scenario ---------------------- //
    private static void run() {
        File lidoDirectory = new File(FileConstants.PATH_INPUT_LIDO_DIR);
        File[] subDirectories = lidoDirectory.listFiles();

        for (File file : subDirectories) {
            String fullName = file.getName();
            int dotIndex = fullName.lastIndexOf(".");
            String fileName = fullName.substring(0, dotIndex);

            if (!fileName.startsWith("demo")) {
                mapEntries(fileName);
            }
        }
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo() {
        mapEntries(FileConstants.FILE_NAME_DEMO);
    }

    private static void mapEntries(String fileName) {
        Model model = GraphModel.generateModel();
        String inputFilePath = getInputFilePath(fileName);
        String outputPath = getOutputFilePath(fileName);

        LidoWrapProcessing.mapEntries(model, inputFilePath);
        GraphUtils.writeRDFGraph(model, outputPath, PRINT_RDF_RESULTS);
    }

    private static String getInputFilePath(String fileName) {
        return FileConstants.PATH_INPUT_LIDO_DIR
                + FILE_SEPARATOR + fileName
                + EXTENSION_SEPARATOR + EXTENSION_XML;
    }

    private static String getOutputFilePath(String fileName) {
        return FileConstants.PATH_OUTPUT_LIDO_DIR
                + FILE_SEPARATOR + fileName
                + EXTENSION_SEPARATOR + EXTENSION_RDF;
    }
}
