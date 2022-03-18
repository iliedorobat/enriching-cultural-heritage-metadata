package ro.webdata.translator.edm.approach.event.cimec.mapping;

import com.google.gson.JsonArray;
import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Json;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.EnvConstants;
import ro.webdata.translator.commons.FileConstants;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.edm.approach.event.cimec.commons.FilePath;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.Museum;

public class CimecMapper {
    public static void run() {
        String enPath = FilePath.getCimecJsonPath(Const.LANG_EN);
        String roPath = FilePath.getCimecJsonPath(Const.LANG_RO);

        JsonArray enJsonArray = Json.getJsonArray(enPath);
        JsonArray roJsonArray = Json.getJsonArray(roPath);

        Model model = GraphModel.generateModel();

        Museum.mapEntries(model, Const.LANG_EN, enJsonArray);
        Museum.mapEntries(model, Const.LANG_RO, roJsonArray);

        GraphUtils.writeRDFGraph(model, FileConstants.PATH_OUTPUT_CIMEC_DATASET, EnvConstants.PRINT_RDF_RESULTS);
    }

    public static void runDemo() {
        String enPath = FilePath.getCimecJsonPath(Const.LANG_EN);
        String roPath = FilePath.getCimecJsonPath(Const.LANG_RO);

        JsonArray enJsonArray = Json.getJsonArray(enPath);
        JsonArray roJsonArray = Json.getJsonArray(roPath);

        Model model = GraphModel.generateModel();

        Museum.mapEntriesTest(model, Const.LANG_EN, enJsonArray, 10);
        Museum.mapEntriesTest(model, Const.LANG_RO, roJsonArray, 10);

        GraphUtils.writeRDFGraph(model, FileConstants.PATH_OUTPUT_CIMEC_DEMO, EnvConstants.PRINT_RDF_RESULTS);
    }
}
