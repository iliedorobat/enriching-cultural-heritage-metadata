package ro.webdata.echo.translator.edm.approach.event;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.commons.GraphUtils;
import ro.webdata.echo.translator.edm.approach.event.lido.Stats;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.FileUtils;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.LidoWrapProcessing;

import java.io.File;

import static ro.webdata.echo.translator.commons.Env.PRINT_RDF_RESULTS;

public class Lido {
    public static void run() {
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

       Stats.run();
    }

    public static void mapEntries(String fileName) {
        Model model = GraphModel.generateModel();
        String inputFilePath = FileUtils.getInputFilePath(fileName);
        String outputPath = FileUtils.getOutputFilePath(fileName);

        LidoWrapProcessing.mapEntries(model, inputFilePath);
        GraphUtils.writeRDFGraph(model, outputPath, PRINT_RDF_RESULTS);
    }
}
