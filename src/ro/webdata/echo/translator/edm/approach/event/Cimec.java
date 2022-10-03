package ro.webdata.echo.translator.edm.approach.event;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.commons.GraphUtils;
import ro.webdata.echo.translator.edm.approach.event.cimec.mapping.core.Museum;

import static ro.webdata.echo.translator.commons.Env.PRINT_RDF_RESULTS;

public class Cimec {
    public static void run() {
        Model model = GraphModel.generateModel();
        Museum.mapEntries(model);
        GraphUtils.writeRDFGraph(model, FileConst.PATH_OUTPUT_CIMEC_DATASET, PRINT_RDF_RESULTS);
    }
}
