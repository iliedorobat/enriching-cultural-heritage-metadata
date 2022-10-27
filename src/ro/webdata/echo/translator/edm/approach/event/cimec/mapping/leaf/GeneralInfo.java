package ro.webdata.echo.translator.edm.approach.event.cimec.mapping.leaf;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.commons.PropertyUtils;

import java.util.Map;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;

public class GeneralInfo {
    public static void mapEntries(
            Model model,
            Resource museum,
            Map.Entry<String, JsonElement> jsonEntry,
            String lang
    ) {
        JsonElement value = jsonEntry.getValue();
        String key = jsonEntry.getKey();

        switch (key) {
            case ACCREDITATION:
                PropertyUtils.addSubProperty(model, museum, SKOS.note, MUSEUM_ACCREDITATION, value, lang);
                break;
            case CIMEC_URI:
                PropertyUtils.addAgentUri(model, museum, DC_11.identifier, CIMEC_URI, value, null);
                break;
            case CODE:
                PropertyUtils.addSubProperty(model, museum, DC_11.identifier, CODE, value, null);
                break;
            case FOUNDED:
                if (value instanceof JsonPrimitive) {
                    museum.addProperty(EDM.begin, value.getAsString());
                }
                break;
            case NAME:
                PropertyUtils.addProperty(model, museum, SKOS.prefLabel, value, lang);
                break;
            case TYPE:
                PropertyUtils.addSubProperty(model, museum, SKOS.note, TYPE, value, lang);
                break;
            default:
                break;
        }
    }
}
