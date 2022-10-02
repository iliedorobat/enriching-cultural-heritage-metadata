package ro.webdata.translator.edm.approach.object.dspace;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.FileConstants;

import java.io.StringWriter;

// TODO: java 10 => java 1.8
public class Main {
    public static void main(String[] args) {
        Model model = GraphModel.generateModel();
        DSpaceMapping.mapEntries(model, FileConstants.PATH_INPUT_DSPACE_DIR);
        writeRDFGraph(model, FileConstants.PATH_OUTPUT_DATASET_FILE, false);
    }

    private static void writeRDFGraph(Model model, String outputFilePath, boolean append) {
        StringWriter writer = new StringWriter();
        model.write(writer, GraphModel.SYNTAX_RDF_XML);
        File.write(writer, outputFilePath, append);
        String result = writer.toString();
        System.out.println(result);
    }
}
