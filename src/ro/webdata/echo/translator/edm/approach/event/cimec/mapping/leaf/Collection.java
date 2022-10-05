package ro.webdata.echo.translator.edm.approach.event.cimec.mapping.leaf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.translator.commons.PropertyUtils;

import java.util.ArrayList;
import java.util.Map;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;

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
                        PropertyUtils.addSubProperty(model, museum, SKOS.note, COLLECTION_IMPORTANCE, collection, IMPORTANCE, lang);
                        break;
                    case PICTURES:
                        PropertyUtils.addAgentUris(model, museum, SKOS.note, COLLECTION_PICTURES, collection, PICTURES, null);
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
                    PropertyUtils.addSubProperty(model, museum, SKOS.note, COLLECTION_GENERAL_PROFILE, profile, GENERAL, lang);
                    break;
                case MAIN:
                    PropertyUtils.addSubProperty(model, museum, SKOS.note, COLLECTION_MAIN_PROFILE, profile, MAIN, lang);
                    break;
                default:
                    break;
            }
        }
    }
}
