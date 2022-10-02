package ro.webdata.translator.edm.approach.event;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.FileConst;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.Museum;

import static ro.webdata.translator.commons.Env.PRINT_RDF_RESULTS;

public class Cimec {
    public static void run() {
        Model model = GraphModel.generateModel();
        Museum.mapEntries(model);
        GraphUtils.writeRDFGraph(model, FileConst.PATH_OUTPUT_CIMEC_DATASET, PRINT_RDF_RESULTS);
    }
}
