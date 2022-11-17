package ro.webdata.echo.translator.edm.test;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.commons.GraphUtils;
import ro.webdata.echo.translator.edm.dspace.DSpaceTranslator;

import static ro.webdata.echo.translator.commons.Env.PRINT_RDF_RESULTS;

public class DSpaceDemo {
    public static void run() {
        Model model = GraphModel.generateModel();
        DSpaceTranslator.mapEntries(model, FileConst.PATH_INPUT_DSPACE_DIR);
        GraphUtils.writeRDFGraph(model, FileConst.PATH_OUTPUT_DSPACE_DEMO, PRINT_RDF_RESULTS);
    }
}
