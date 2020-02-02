package ro.webdata.translator.edm.approach.object.dspace;

import org.apache.jena.rdf.model.Model;
import ro.webdata.common.utils.FileUtils;
import ro.webdata.common.utils.ModelUtils;
import ro.webdata.translator.edm.approach.object.dspace.common.constants.FileConstants;

import java.io.StringWriter;

public class Main {
    public static void main(String[] args) {
        Model model = ModelUtils.generateModel();
        DSpaceMapping.processing(model, FileConstants.PATH_INPUT_DSPACE_DIR);
        writeRDFGraph(model, FileConstants.PATH_OUTPUT_DATASET_FILE, false);
    }

    private static void writeRDFGraph(Model model, String outputFilePath, boolean append) {
        StringWriter writer = new StringWriter();
        model.write(writer, ModelUtils.SYNTAX_RDF_XML);
        FileUtils.write(writer, outputFilePath, append);
        String result = writer.toString();
        System.out.println(result);
    }
}
