package ro.webdata.translator.edm.approach.event.cimec.mapping.leaf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;

import java.util.ArrayList;
import java.util.Map;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;
import static ro.webdata.translator.edm.approach.event.lido.commons.PropertyUtils.*;

public class Collection {
    public static void mapEntries(
            Model model,
            Resource museum,
            Map.Entry<String, JsonElement> jsonEntry,
            String lang
    ) {
        try {
            JsonObject collection = jsonEntry.getValue().getAsJsonObject();
            ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(collection.entrySet());

            for (Map.Entry<String, JsonElement> entry : entries) {
                String key = entry.getKey();

                switch (key) {
                    case IMPORTANCE:
                        addProperty(model, museum, SKOS.note, COLLECTION_IMPORTANCE, collection, IMPORTANCE, lang);
                        break;
                    case PICTURES:
                        // EDM.Agent does not support a reference for the "skos:note" property
                        // addUriProperties(model, museum, SKOS.note, COLLECTION_PICTURES, collection, PICTURES);
                        addProperties(model, museum, SKOS.note, COLLECTION_PICTURES, collection, PICTURES, null);
                        break;
                    case PROFILE:
                        mapProfile(model, museum, collection, lang);
                        break;
                    default:
                        break;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static void mapProfile(Model model, Resource museum, JsonObject collection, String lang) {
        JsonObject profile = collection.get(PROFILE).getAsJsonObject();
        ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(profile.entrySet());

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();

            switch (key) {
                case GENERAL:
                    addProperty(model, museum, SKOS.note, COLLECTION_GENERAL_PROFILE, profile, GENERAL, lang);
                    break;
                case MAIN:
                    addProperty(model, museum, SKOS.note, COLLECTION_MAIN_PROFILE, profile, MAIN, lang);
                    break;
                default:
                    break;
            }
        }
    }
}
