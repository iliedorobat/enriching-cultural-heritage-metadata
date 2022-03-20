package ro.webdata.translator.edm.approach.event.cimec;

import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.EnvConstants;
import ro.webdata.translator.commons.FileConstants;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.Museum;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.MuseumTest;

public class Main {
    public static void main(String[] args) {
        Print.operation(Const.OPERATION_START, EnvConstants.SHOULD_PRINT);

        if (!EnvConstants.IS_DEMO) {
            run();
        } else {
            runDemo();
        }

        Print.operation(Const.OPERATION_END, EnvConstants.SHOULD_PRINT);
    }

    //---------------------- Real Scenario ---------------------- //
    private static void run() {
        Model model = GraphModel.generateModel();
        Museum.mapEntries(model);
        GraphUtils.writeRDFGraph(model, FileConstants.PATH_OUTPUT_CIMEC_DATASET, EnvConstants.PRINT_RDF_RESULTS);
    }

    //---------------------- DEMO Scenario ---------------------- //
    private static void runDemo() {
        Model model = GraphModel.generateModel();
        MuseumTest.mapEntries(model, 10);
        GraphUtils.writeRDFGraph(model, FileConstants.PATH_OUTPUT_CIMEC_DEMO, EnvConstants.PRINT_RDF_RESULTS);
    }
}
