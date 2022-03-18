package ro.webdata.translator.edm.approach.event.cimec.mapping.leaf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.graph.Namespace;

import java.util.ArrayList;
import java.util.Map;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;
import static ro.webdata.translator.edm.approach.event.lido.commons.PropertyUtils.addProperty;
import static ro.webdata.translator.edm.approach.event.lido.commons.PropertyUtils.addUriProperty;
import static ro.webdata.translator.edm.approach.event.lido.commons.PropertyUtils.createSubProperty;

public class Location {
    public static void mapEntries(
            Model model,
            Resource museum,
            Map.Entry<String, JsonElement> jsonEntry,
            String lang
    ) {
        try {
            JsonObject contact = jsonEntry.getValue().getAsJsonObject();
            ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(contact.entrySet());

            for (Map.Entry<String, JsonElement> entry : entries) {
                String key = entry.getKey();

                switch (key) {
                    case ACCESS:
                        addProperty(model, museum, SKOS.note, LOCATION_ACCESS, contact, ACCESS, lang);
                        break;
                    case ADDRESS:
                        addProperty(model, museum, SKOS.note, LOCATION_ADDRESS, contact, ADDRESS, lang);
                        break;
                    case ADMINISTRATIVE:
                        addProperty(model, museum, SKOS.note, LOCATION_ADM_UNIT, contact, ADMINISTRATIVE, null);
                        break;
                    case COMMUNE:
                        addProperty(model, museum, SKOS.note, LOCATION_COMMUNE, contact, COMMUNE, null);
                        break;
                    case COUNTY:
                        mapCounty(model, museum, contact);
                        break;
                    case GEO:
                        mapGeo(model, museum, contact);
                        break;
                    case LOCALITY:
                        mapLocality(model, museum, lang, contact);
                        break;
                    case ZIP_CODE:
                        addProperty(model, museum, SKOS.note, LOCATION_ZIP_CODE, contact, ZIP_CODE, null);
                        break;
                    default:
                        break;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static void mapCounty(Model model, Resource museum, JsonObject object) {
        try {
            String countyName = object.get(COUNTY).getAsString();
            Property property = createSubProperty(model, SKOS.note, LOCATION_COUNTY, false);
            String link = (
                    Namespace.NS_DBPEDIA_RESOURCE + StringUtils.capitalize(countyName) + "_County"
            ).replaceAll("\\s", "_");
            addUriProperty(model, museum, property, link);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static void mapGeo(Model model, Resource museum, JsonObject contact) {
        JsonObject agent = contact.get(GEO).getAsJsonObject();
        ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(agent.entrySet());

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();

            switch (key) {
                case LATITUDE:
                    addProperty(model, museum, SKOS.note, LOCATION_GEO_LATITUDE, agent, LATITUDE, null);
                    break;
                case LONGITUDE:
                    addProperty(model, museum, SKOS.note, LOCATION_GEO_LONGITUDE, agent, LONGITUDE, null);
                    break;
                default:
                    break;
            }
        }
    }

    private static void mapLocality(Model model, Resource museum, String lang, JsonObject contact) {
        JsonObject agent = contact.get(LOCALITY).getAsJsonObject();
        ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(agent.entrySet());

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();

            switch (key) {
                case CODE:
                    addProperty(model, museum, SKOS.note, LOCATION_LOCALITY_CODE, agent, CODE, null);
                    break;
                case NAME:
                    addProperty(model, museum, SKOS.note, LOCATION_LOCALITY_NAME, agent, NAME, lang);
                    break;
                default:
                    break;
            }
        }
    }
}
