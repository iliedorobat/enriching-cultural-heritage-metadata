package ro.webdata.translator.edm.approach.event;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.FileConst;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.edm.approach.event.lido.commons.FileUtils;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.LidoWrapProcessing;

import java.io.File;

import static ro.webdata.translator.commons.EnvConstants.*;

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
        File lidoDirectory = new File(FileConst.PATH_INPUT_LIDO_DIR);
        File[] subDirectories = lidoDirectory.listFiles();

        if (subDirectories != null) {
            for (File file : subDirectories) {
                String fullName = file.getName();
                int dotIndex = fullName.lastIndexOf(".");
                String fileName = fullName.substring(0, dotIndex);

                if (!fileName.startsWith("demo")) {
                    mapEntries(fileName);
                }
            }
        } else {
            System.err.println(FileConst.PATH_INPUT_LIDO_DIR + " does not contain any directories!");
        }
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo() {
        mapEntries(FileConst.FILE_NAME_DEMO);
    }

    private static void mapEntries(String fileName) {
        Model model = GraphModel.generateModel();
        String inputFilePath = FileUtils.getInputFilePath(fileName);
        String outputPath = FileUtils.getOutputFilePath(fileName);

        LidoWrapProcessing.mapEntries(model, inputFilePath);
        GraphUtils.writeRDFGraph(model, outputPath, PRINT_RDF_RESULTS);
    }
}
