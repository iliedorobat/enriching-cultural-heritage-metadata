package ro.webdata.echo.translator.edm.approach.event.cimec.mapping.leaf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.translator.commons.PropertyUtils;

import java.util.ArrayList;
import java.util.Map;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;
import static ro.webdata.echo.translator.commons.PropertyUtils.addProperty;

public class Subordination {
    public static void mapEntries(
            Model model,
            Resource museum,
            Map.Entry<String, JsonElement> jsonEntry,
            String lang
    ) {
        try {
            JsonObject building = jsonEntry.getValue().getAsJsonObject();
            ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(building.entrySet());

            for (Map.Entry<String, JsonElement> entry : entries) {
                String key = entry.getKey();

                switch (key) {
                    case SUPERVISED_BY:
                        PropertyUtils.addSubProperty(model, museum, SKOS.note, MUSEUM_SUPERVISED_BY, building, SUPERVISED_BY, lang);
                        break;
                    case SUPERVISOR_FOR:
                        PropertyUtils.addSubProperties(model, museum, SKOS.note, MUSEUM_SUPERVISOR_FOR, building, SUPERVISOR_FOR, lang);
                        break;
                    case PART_OF:
                        PropertyUtils.addProperty(model, museum, DCTerms.isPartOf, building, PART_OF, lang);
                        break;
                    default:
                        break;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
