package ro.webdata.translator.commons;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.graph.GraphModel;

import java.io.StringWriter;

public class GraphUtils {
    public static void writeRDFGraph(Model model, String outputFilePath, boolean printResult) {
        StringWriter writer = new StringWriter();
        model.write(writer, GraphModel.SYNTAX_RDF_XML);
        File.write(writer, outputFilePath, false);

        if (printResult) {
            String result = writer.toString();
            System.out.println(result);
        }
    }
}
