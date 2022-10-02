package ro.webdata.translator.edm.test.approach.object;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.FileConst;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.edm.approach.object.dspace.DSpaceMapping;

import static ro.webdata.translator.commons.Env.PRINT_RDF_RESULTS;

public class DSpaceDemo {
    public static void run() {
        Model model = GraphModel.generateModel();
        DSpaceMapping.mapEntries(model, FileConst.PATH_INPUT_DSPACE_DIR);
        GraphUtils.writeRDFGraph(model, FileConst.PATH_OUTPUT_DSPACE_DEMO, PRINT_RDF_RESULTS);
    }
}
