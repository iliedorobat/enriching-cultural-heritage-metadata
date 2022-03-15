package ro.webdata.translator.edm.approach.event.cimec.mapping.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.translator.edm.approach.event.cimec.mapping.leaf.*;

import java.util.Map;
import java.util.Set;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;

public class Museum {
    public static void mapEntries(Model model, String lang, JsonArray jsonArray) {
        for (JsonElement museumJson : jsonArray) {
            JsonObject object = museumJson.getAsJsonObject();
            mapEntry(model, lang, object);
        }
    }

    public static void mapEntriesTest(Model model, String lang, JsonArray jsonArray, int index) {
        JsonElement museumJson = jsonArray.get(index);
        JsonObject object = museumJson.getAsJsonObject();
        mapEntry(model, lang, object);
    }

    private static void mapEntry(Model model, String lang, JsonObject object) {
        Set<Map.Entry<String, JsonElement>> objectEntries = object.entrySet();
        Resource museum = generateMuseum(model, object);

        for (Map.Entry<String, JsonElement> baseEntry : objectEntries) {
            String baseKey = baseEntry.getKey();

            switch (baseKey) {
                case ACCREDITATION:
                case CIMEC_URI:
                case CODE:
                case FOUNDED:
                case NAME:
                    GeneralInfo.mapEntries(model, museum, baseEntry, lang);
                    break;
                case BUILDING:
                    Building.mapEntries(model, museum, baseEntry, lang);
                    break;
                case COLLECTION:
                    Collection.mapEntries(model, museum, baseEntry, lang);
                    break;
                case CONTACT:
                    Contact.mapEntries(model, museum, baseEntry, lang);
                    break;
                case DESCRIPTION:
                    Description.mapEntries(model, museum, baseEntry, lang);
                    break;
                case LOCATION:
                    Location.mapEntries(model, museum, baseEntry, lang);
                    break;
                case PUBLICATIONS:
                    Publications.mapEntries(model, museum, baseEntry, lang);
                    break;
                case SUBORDINATION:
                    Subordination.mapEntries(model, museum, baseEntry, lang);
                    break;
                default:
                    break;
            }
        }
    }

    private static Resource generateMuseum(Model model, JsonObject museumObject) {
        String id = Namespace.NS_REPO_RESOURCE_AGENT
                + "CIMEC"
                + File.FILE_SEPARATOR
                + museumObject.get(CODE).getAsString()
                + File.FILE_SEPARATOR;

        Resource museum = model.createResource(id);
        museum.addProperty(RDF.type, EDM.Agent);

        return museum;
    }
}
