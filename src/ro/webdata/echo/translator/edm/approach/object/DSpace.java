package ro.webdata.echo.translator.edm.approach.object;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.commons.GraphUtils;
import ro.webdata.echo.translator.edm.approach.object.dspace.DSpaceMapping;

import static ro.webdata.echo.translator.commons.Env.PRINT_RDF_RESULTS;

public class DSpace {
    public static void run() {
        Model model = GraphModel.generateModel();
        DSpaceMapping.mapEntries(model, FileConst.PATH_INPUT_DSPACE_DIR);
        GraphUtils.writeRDFGraph(model, FileConst.PATH_OUTPUT_DSPACE_DATASET_DIR, PRINT_RDF_RESULTS);
    }
}
