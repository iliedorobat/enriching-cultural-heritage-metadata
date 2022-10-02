package ro.webdata.translator.edm.approach.event.cimec.mapping.leaf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;

import java.util.ArrayList;
import java.util.Map;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;
import static ro.webdata.translator.commons.PropertyUtils.addProperty;

public class Building {
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
                    case DESCRIPTION:
                        addProperty(model, museum, SKOS.note, building, DESCRIPTION, lang);
                        break;
                    case LMI_CODE:
                        addProperty(model, museum, SKOS.note, BUILDING_LMI_CODE, building, LMI_CODE, null);
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
