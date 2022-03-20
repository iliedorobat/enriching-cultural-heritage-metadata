package ro.webdata.translator.edm.approach.event.cimec.mapping.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import ro.webdata.echo.commons.Const;
import ro.webdata.translator.commons.MuseumUtils;

public class MuseumTest extends Museum {
    public static void mapEntries(Model model, int index) {
        mapEntries(model, Const.LANG_EN, MuseumUtils.enJsonArray, index);
        mapEntries(model, Const.LANG_RO, MuseumUtils.roJsonArray, index);
    }

    private static void mapEntries(Model model, String lang, JsonArray jsonArray, int index) {
        JsonElement museumJson = jsonArray.get(index);
        JsonObject object = museumJson.getAsJsonObject();
        mapEntry(model, lang, object);
    }
}
