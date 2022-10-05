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

public class Contact {
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
                    case AGENT:
                        mapAgent(model, museum, contact, lang);
                        break;
                    case DIRECTOR:
                        PropertyUtils.addSubProperty(model, museum, SKOS.note, CONTACT_DIRECTOR, contact, DIRECTOR, lang);
                        break;
                    case EMAIL:
                        PropertyUtils.addSubProperties(model, museum, SKOS.note, CONTACT_EMAIL, contact, EMAIL, null);
                        break;
                    case FAX:
                        PropertyUtils.addSubProperties(model, museum, SKOS.note, CONTACT_FAX, contact, FAX, null);
                        break;
                    case SOCIAL_MEDIA:
                        PropertyUtils.addAgentUris(model, museum, SKOS.note, CONTACT_SOCIAL_MEDIA, contact, SOCIAL_MEDIA, null);
                        break;
                    case PHONE:
                        PropertyUtils.addSubProperties(model, museum, SKOS.note, CONTACT_PHONE, contact, PHONE, null);
                        break;
                    case TIMETABLE:
                        PropertyUtils.addSubProperties(model, museum, SKOS.note, CONTACT_TIME_TABLE, contact, TIMETABLE, lang);
                        break;
                    case VIRTUAL_TOUR:
                        PropertyUtils.addAgentUris(model, museum, SKOS.note, CONTACT_VIRTUAL_TOUR, contact, VIRTUAL_TOUR, null);
                        break;
                    case WEB:
                        PropertyUtils.addAgentUris(model, museum, SKOS.note, CONTACT_WEB, contact, WEB, null);
                        break;
                    default:
                        break;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static void mapAgent(Model model, Resource museum, JsonObject contact, String lang) {
        JsonObject agent = contact.get(AGENT).getAsJsonObject();
        ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(agent.entrySet());

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();

            switch (key) {
                case NAME:
                    PropertyUtils.addSubProperty(model, museum, SKOS.note, CONTACT_PERSON_NAME, agent, NAME, lang);
                    break;
                case POSITION:
                    PropertyUtils.addSubProperty(model, museum, SKOS.note, CONTACT_PERSON_POSITION, agent, POSITION, lang);
                    break;
                default:
                    break;
            }
        }
    }
}
