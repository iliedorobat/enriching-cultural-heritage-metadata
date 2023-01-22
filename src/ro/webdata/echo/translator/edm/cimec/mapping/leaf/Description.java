package ro.webdata.echo.translator.edm.cimec.mapping.leaf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.translator.commons.PropertyUtils;

import java.util.ArrayList;
import java.util.Map;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;

public class Description {
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
                    case DETAILS:
                        PropertyUtils.addSubProperty(model, museum, SKOS.note, DESCRIPTION_DETAILS, building, DETAILS, lang);
                        break;
                    case HISTORIC:
                        PropertyUtils.addSubProperty(model, museum, SKOS.note, DESCRIPTION_HISTORIC, building, HISTORIC, lang);
                        break;
                    case SUMMARY:
                        PropertyUtils.addSubProperty(model, museum, SKOS.note, DESCRIPTION_SUMMARY, building, SUMMARY, lang);
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
