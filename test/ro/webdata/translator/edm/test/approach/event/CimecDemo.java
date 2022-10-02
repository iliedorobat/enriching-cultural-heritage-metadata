package ro.webdata.translator.edm.test.approach.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.commons.FileConst;
import ro.webdata.translator.commons.GraphUtils;
import ro.webdata.translator.commons.MuseumUtils;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.Museum;

import static ro.webdata.translator.commons.Env.PRINT_RDF_RESULTS;

public class CimecDemo {
    private static final int INDEX = 10;

    public static void run() {
        Model model = GraphModel.generateModel();
        mapEntries(model, INDEX);
        GraphUtils.writeRDFGraph(model, FileConst.PATH_OUTPUT_CIMEC_DEMO, PRINT_RDF_RESULTS);
    }

    private static void mapEntries(Model model, int index) {
        mapEntries(model, Const.LANG_EN, MuseumUtils.enJsonArray, index);
        mapEntries(model, Const.LANG_RO, MuseumUtils.roJsonArray, index);
    }

    private static void mapEntries(Model model, String lang, JsonArray jsonArray, int index) {
        JsonElement museumJson = jsonArray.get(index);
        JsonObject object = museumJson.getAsJsonObject();
        Museum.mapEntry(model, lang, object);
    }
}
