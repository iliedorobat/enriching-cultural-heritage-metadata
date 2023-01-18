package ro.webdata.echo.fetcher.museums.commons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
     * Get unique CIMEC keys extracted from the parsed dataset
     * @param path The path to the input file
     * @return The list of unique keys
     */
    public static Set<String> getKeys(String path) {
        Set<String> keys = new TreeSet<>();
        JsonArray array = Json.getJsonArray(path);

        for (JsonElement element: array) {
            JsonObject object = element.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = object.entrySet();

            for (Map.Entry<String, JsonElement> entry: entrySet) {
                keys.add(entry.getKey());
            }
        }

        return keys;
    }
}
