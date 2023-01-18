package ro.webdata.echo.fetcher.museums.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Json;
import ro.webdata.echo.commons.accessor.MuseumAccessors;
import ro.webdata.echo.fetcher.museums.commons.FilePath;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

public class DataConsolidation {
    private static final Gson gson = new Gson();

    public static void writeFinalJson(String lang) {
        String path = FilePath.getFinalJsonPath(lang);
        JsonArray jsonArray = mergeData(lang);
        appendTypeEntry(lang, jsonArray);

        String json = gson.toJson(jsonArray);
        File.write(json, path, false);
    }

    private static void appendTypeEntry(String lang, JsonArray jsonArray) {
        for (JsonElement targetElement: jsonArray) {
            JsonObject object = (JsonObject) targetElement;

            if (lang.equals(Const.LANG_EN)) {
                object.addProperty(MuseumAccessors.TYPE, "Museum");
            } else if (lang.equals(Const.LANG_RO)) {
                object.addProperty(MuseumAccessors.TYPE, "Muzeu");
            }
        }
    }

    private static JsonArray mergeData(String lang) {
        String cimecInputPath = FilePath.getCimecPreparedJsonPath(lang);
        String inpInputPath = FilePath.getInpPreparedJsonPath(lang);

        JsonArray cimecEntries = Json.getJsonArray(cimecInputPath);
        JsonArray inpEntries = Json.getJsonArray(inpInputPath);

        return mergeKeyValuePairs(inpEntries, cimecEntries, lang);
    }

    // sourceEntries = inpEntries
    // targetEntries = cimecEntries
    private static JsonArray mergeKeyValuePairs(JsonArray sourceEntries, JsonArray targetEntries, String lang) {
        JsonArray mergedElements = new JsonArray();
        ArrayList<String> uncommonKeys = getUncommonKeys(sourceEntries, targetEntries);
        int index = 0;

        System.out.println("Starting data consolidation (lang = " + lang + ")...");

        for (JsonElement targetElement: targetEntries) {
            System.out.println("Consolidating record no. " + index);
            index++;

            JsonObject targetObject = Json.getJsonObject(targetElement);
            String targetCode = targetObject.get(MuseumAccessors.MUSEUM_CODE).getAsString();

            if (exists(sourceEntries, targetCode)) {
                for (JsonElement sourceElement: sourceEntries) {
                    JsonObject sourceObject = Json.getJsonObject(sourceElement);
                    String sourceCode = sourceObject.get(MuseumAccessors.MUSEUM_CODE).getAsString();

                    if (targetCode.equals(sourceCode)) {
                        for (String backendKey: uncommonKeys) {
                            mergeKeyValuePair(sourceObject, targetObject, backendKey);
                        }
                        mergedElements.add(targetObject);
                    }
                }
            } else {
                mergedElements.add(targetObject);
            }
            // TODO: if the "geo" object is empty, use geonames for adding the geo coords
        }

        System.out.println("The data consolidation is finished!");

        return mergedElements;
    }

    /**
     * Check if the CIMEC entry exists in the INP dataset
     * @param sourceEntries The INP dataset
     * @param targetCode The CIMEC museum code
     * @return True/False
     */
    private static boolean exists(JsonArray sourceEntries, String targetCode) {
        for (JsonElement targetElement: sourceEntries) {
            JsonObject targetObject = Json.getJsonObject(targetElement);
            String sourceCode = targetObject.get(MuseumAccessors.MUSEUM_CODE).getAsString();

            if (targetCode.equals(sourceCode)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Merge the key-value pair from the source object to the target object
     * @param sourceJsonObject The object from which the key-value pair will be extracted (inpObject)
     * @param targetJsonObject The object in which the key-value pair will be added (cimecObject)
     * @param backendKey The backend accessor (e.g.: "location.geo.longitude")
     */
    private static void mergeKeyValuePair(JsonObject sourceJsonObject, JsonObject targetJsonObject, String backendKey) {
        JsonElement jsonElement = getTargetElement(targetJsonObject, backendKey);

        if (jsonElement != null) {
            String[] keys = backendKey.split(MuseumAccessors.ESCAPED_SEPARATOR);
            String key = keys[keys.length - 1];
            Object value = Json.getAccessorValue(sourceJsonObject, backendKey);

            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (value != null) {
                    jsonObject.add(key, Json.getJsonElement(value));
                }
            } catch (IllegalStateException e) {
//                e.printStackTrace();
            }
        }
    }

    /**
     * Get the target JsonElement for which the value will be added
     * @param jsonObject A JSON object (see the example provided in JsonUtils.getAccessorValue)
     * @param keyName The key retrieved by the means of DataMapping.getKeyName (E.g.: "location.geo.longitude")
     * @return The element in which the key-value pair will be added
     */
    private static JsonElement getTargetElement(JsonObject jsonObject, String keyName) {
        String[] keys = keyName.split(MuseumAccessors.ESCAPED_SEPARATOR);
        JsonElement targetElement = jsonObject.get(keys[0]);

        for (int i = 1; i < keys.length; i++) {
            String key = keys[i];

            if (i < keys.length - 1) {
                if (targetElement.isJsonObject()) {
                    JsonElement crrElement = targetElement.getAsJsonObject().get(key);

                    if (crrElement != null) {
                        targetElement = crrElement;
                    } else {
                        targetElement.getAsJsonObject().add(key, new JsonObject());
                        targetElement = targetElement.getAsJsonObject().get(key);
                    }
                }
            }
        }

        return targetElement;
    }

    /**
     * Get the list of the keys that are found in the sourceEntries, but not in the targetEntries
     * @param sourceEntries The source entries (inpEntries)
     * @param targetEntries The target entries (cimecEntries)
     * @return The list of uncommon keys
     */
    private static ArrayList<String> getUncommonKeys(JsonArray sourceEntries, JsonArray targetEntries) {
        ArrayList<String> sourceKeys = getUniqueBackendAccessors(sourceEntries);
        ArrayList<String> targetKeys = getUniqueBackendAccessors(targetEntries);

        // The list of unique INP backend accessors which are not common with CIMEC
        ArrayList<String> keyNames = new ArrayList<>(sourceKeys);
        keyNames.removeAll(targetKeys);

        return keyNames;
    }

    /**
     * Get the list of unique Backend Accessors for an array of JSON Objects
     * @param entries The array of JSON Objects
     * @return The list of unique Backend Accessors (e.g.: [code, location.geo.latitude, location.geo.longitude])
     */
    private static ArrayList<String> getUniqueBackendAccessors(JsonArray entries) {
        TreeSet<String> keys = new TreeSet<>();

        for (JsonElement entry: entries) {
            keys.addAll(getBackendAccessors(entry.getAsJsonObject()));
        }

        return new ArrayList<>(keys);
    }

    /**
     * Get the list of Backend Accessors of a JSON Object
     * @param object The JSON Object
     * @return The list of Backend Accessors (e.g.: [code, location.geo.latitude, location.geo.longitude])
     */
    private static ArrayList<String> getBackendAccessors(JsonObject object) {
        ArrayList<Map.Entry<String, JsonElement>> entries = new ArrayList<>(object.entrySet());
        ArrayList<String> keyNames = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonPrimitive()) {
                keyNames.add(key);
            } else if (value.isJsonObject()) {
                ArrayList<String> secondaryKeys = getBackendAccessors(value.getAsJsonObject());

                for (String secondaryKey : secondaryKeys) {
                    keyNames.add(key + MuseumAccessors.SEPARATOR + secondaryKey);
                }
            }
        }

        return keyNames;
    }
}
