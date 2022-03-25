package ro.webdata.translator.edm.approach.event.cimec.mapping.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.translator.commons.MuseumUtils;
import ro.webdata.translator.edm.approach.event.cimec.mapping.leaf.*;

import java.util.Map;
import java.util.Set;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;

public class Museum {
    public static void mapEntries(Model model) {
        mapEntries(model, Const.LANG_EN, MuseumUtils.enJsonArray);
        mapEntries(model, Const.LANG_RO, MuseumUtils.roJsonArray);
    }

    private static void mapEntries(Model model, String lang, JsonArray jsonArray) {
        for (JsonElement museumJson : jsonArray) {
            JsonObject object = museumJson.getAsJsonObject();
            mapEntry(model, lang, object);
        }
    }

    protected static void mapEntry(Model model, String lang, JsonObject object) {
        Set<Map.Entry<String, JsonElement>> objectEntries = object.entrySet();
        String museumCode = object.get(CODE).getAsString();
        Resource museum = generateMuseum(model, museumCode);

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

    private static Resource generateMuseum(Model model, String museumCode) {
        String museumUri = MuseumUtils.generateMuseumId(museumCode);
        Resource museum = model
                .createResource(museumUri)
                .addProperty(RDF.type, EDM.Agent);

        return museum;
    }
}
