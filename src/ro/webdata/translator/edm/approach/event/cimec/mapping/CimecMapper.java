package ro.webdata.translator.edm.approach.event.cimec.mapping;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.EnvConstants;
import ro.webdata.translator.commons.FileConstants;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.Museum;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.MuseumTest;

public class CimecMapper {
    public static void run() {
        Model model = GraphModel.generateModel();
        Museum.mapEntries(model);
        GraphUtils.writeRDFGraph(model, FileConstants.PATH_OUTPUT_CIMEC_DATASET, EnvConstants.PRINT_RDF_RESULTS);
    }

    public static void runDemo() {
        Model model = GraphModel.generateModel();
        MuseumTest.mapEntries(model, 10);
        GraphUtils.writeRDFGraph(model, FileConstants.PATH_OUTPUT_CIMEC_DEMO, EnvConstants.PRINT_RDF_RESULTS);
    }
}
