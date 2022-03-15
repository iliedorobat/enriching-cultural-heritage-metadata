package ro.webdata.translator.edm.approach.event.cimec.commons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ro.webdata.echo.commons.Json;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DatasetUtils {
    public static void printKeys(String path, String lang) {
        Set<String> keys = getKeys(path);

        System.out.println("LANG: " + lang);
        for (String key: keys) {
            System.out.println(key);
        }
        System.out.println();
    }

    /**
     * Get the list of unique CIMEC keys from a dataset
     * @param path The input file path
     * @return The list of unique keys
     */
    public static Set<String> getKeys(String path) {
        TreeSet<String> keys = new TreeSet<>();
        JsonArray array = Json.getJsonArray(path);

        for (JsonElement element: array) {
            JsonObject object = element.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = object.entrySet();
            addKeys(entrySet, keys, null);
        }

        return keys;
    }

    private static void addKeys(Set<Map.Entry<String, JsonElement>> entrySet, TreeSet<String> keys, String parentKey) {
        for (Map.Entry<String, JsonElement> entry: entrySet) {
            JsonElement value = entry.getValue();
            String parent = parentKey != null
                    ? parentKey + entry.getKey()
                    : entry.getKey();

            if (value instanceof JsonObject) {
                JsonObject object = value.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> innerEntrySet = object.entrySet();
                addKeys(innerEntrySet, keys, parent + ".");
            } else if (value instanceof JsonPrimitive) {
                String key = parentKey != null ?
                        parentKey + entry.getKey()
                        : "";
                keys.add(key);
            }
        }
    }
}
