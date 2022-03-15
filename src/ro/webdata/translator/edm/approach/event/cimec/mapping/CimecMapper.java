package ro.webdata.translator.edm.approach.event.cimec.mapping;

import com.google.gson.JsonArray;
import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Json;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.translator.edm.approach.event.cimec.commons.FilePath;
import ro.webdata.translator.edm.approach.event.cimec.mapping.core.Museum;

import java.io.StringWriter;

public class CimecMapper {
    public static void processing() {
        String enPath = FilePath.getCimecJsonPath(Const.LANG_EN);
        String roPath = FilePath.getCimecJsonPath(Const.LANG_RO);

        JsonArray enJsonArray = Json.getJsonArray(enPath);
        JsonArray roJsonArray = Json.getJsonArray(roPath);

        StringWriter writer = new StringWriter();
        Model model = GraphModel.generateModel();

//        Museum.mapEntries(model, Const.LANG_EN, enJsonArray);
//        Museum.mapEntries(model, Const.LANG_RO, roJsonArray);

        Museum.mapEntriesTest(model, Const.LANG_EN, enJsonArray, 10);
        Museum.mapEntriesTest(model, Const.LANG_RO, roJsonArray, 10);

        model.write(writer, GraphModel.SYNTAX_RDF_XML);

        String result = writer.toString();
        // TODO: write to disk
        System.out.println(result);
    }
}
