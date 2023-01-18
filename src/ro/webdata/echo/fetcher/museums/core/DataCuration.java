package ro.webdata.echo.fetcher.museums.core;

import com.google.gson.*;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.accessor.CimecAccessors;
import ro.webdata.echo.commons.accessor.MuseumAccessors;
import ro.webdata.echo.fetcher.museums.commons.DataFormatter;
import ro.webdata.echo.fetcher.museums.commons.FilePath;
import ro.webdata.echo.fetcher.museums.core.cimec.CimecCuration;
import ro.webdata.echo.fetcher.museums.core.inp.InpCuration;

import java.util.*;

public class DataCuration {
    public static ArrayList<String> SUBORDINATION_ACCESSORS = new ArrayList<>(
            Arrays.asList(
                    CimecAccessors.EN_ADMINISTRATIVE_SUBORDINATION,
                    CimecAccessors.RO_ADMINISTRATIVE_SUBORDINATION
            )
    );
    private static final Gson gson = new Gson();

    /**
     * Write the curated CIMEC dataset to disk
     * @param lang The language used
     */
    public static void writeCimecJson(String lang) {
        String inputPath = FilePath.getCimecRawJsonPath(lang);
        String outputPath = FilePath.getCimecPreparedJsonPath(lang);

        ArrayList<Map<String, Object>> entries = CimecCuration.getEntries(inputPath, lang);
        String json = gson.toJson(entries);

        File.write(json, outputPath, false);
    }

    /**
     * Write the curated INP dataset to disk
     */
    public static void writeInpJson(String lang) {
        String inputPath = FilePath.getInpRawJsonPath(lang);
        String outputPath = FilePath.getInpPreparedJsonPath(lang);

        ArrayList<Map<String, Object>> entries = InpCuration.getEntries(inputPath, lang);
        String json = gson.toJson(entries);

        File.write(json, outputPath, false);
    }

    /**
     * Prepare the entries of a record and add them to the target map
     * @param entryMap The target map
     * @param record The key-value pairs collection describing a museum
     */
    public static void prepareEntryMap(Map<String, Object> entryMap, Map.Entry<String, JsonElement> record) {
        Map<String, Object> crrObject = entryMap;
        String key = DataMapping.getKeyName(record.getKey());
        JsonElement value = record.getValue();

        String[] accessors = Arrays.stream(
                key.split("\\.")
        ).filter(
                accessor -> MuseumAccessors.AVAILABLE_ACCESSORS.contains(accessor)
        ).toArray(
                String[] :: new
        );

        for (int i = 0; i < accessors.length; i++) {
            String accessor = accessors[i];

            // If there is a simple key, add it as a simple string
            // E.g.: key = "contact"   =>   accessors = {"contract"}
            if (accessors.length == 1) {
                addValue(crrObject, accessor, key, value);
            }
            // E.g.: key = "contact.agent.name"   =>   accessors = {"contract", "agent", "name"}
            else {
                // For each key item (E.g.: "contract" and "agent") except the last one
                // (E.g.: "name"), build an empty map to use for storage
                if (i < accessors.length - 1) {
                    Map item = (Map) crrObject.get(accessor);

                    if (item == null) {
                        crrObject.put(accessor, new HashMap<>());
                    }

                    crrObject = (Map<String, Object>) crrObject.get(accessor);
                }
                // Add the last key item as a simple string
                // E.g.: key = "contact.agent.name"   =>   last key item = "name"
                else {
                    addValue(crrObject, accessor, key, value);
                }
            }
        }
    }

    /**
     * Format the value and add it to the map
     * @param map The target map
     * @param accessor One of the values of MuseumAccessors.ALLOWED_ACCESSORS
     * @param keyName The key retrieved by the means of DataMapping.getKeyName (E.g.: "contact.agent.name")
     * @param jsonArray Array of values
     */
    private static void addValue(Map<String, Object> map, String accessor, String keyName, JsonArray jsonArray) {
        if (jsonArray != null) {
            List<Object> list = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                if (element instanceof JsonPrimitive) {
                    String formattedValue = DataFormatter.format(keyName, element.getAsString());
                    list.add(formattedValue);
                }
                // keyName.contains("supervisorFor")
                else {
                    JsonObject object = element.getAsJsonObject();
                    Map<String, Object> objectMap = new TreeMap<>();

                    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                        String key = DataMapping.getKeyName(entry.getKey());
                        JsonElement value = entry.getValue();

                        if (value instanceof JsonPrimitive) {
                            String formattedValue = DataFormatter.format(key, value.getAsString());
                            objectMap.put(key, formattedValue);
                        }
                    }

                    if (!objectMap.isEmpty())
                        list.add(objectMap);
                }
            }

            if (list.size() > 0)
                map.put(accessor, list);
        }
    }

    /**
     * Format the value and add it to the map according to its type
     * @param map The target map
     * @param accessor One of the values of MuseumAccessors.ALLOWED_ACCESSORS
     * @param keyName The key retrieved by the means of DataMapping.getKeyName (E.g.: "contact.agent.name")
     * @param value The original value
     */
    private static void addValue(Map<String, Object> map, String accessor, String keyName, JsonElement value) {
        String strValue;
        JsonArray arrValue;

        try {
            arrValue = value.getAsJsonArray();
            addValue(map, accessor, keyName, arrValue);
        } catch (IllegalStateException e) {
            try {
                strValue = value.getAsString();
                addValue(map, accessor, keyName, strValue);
            } catch (IllegalStateException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * Format the value and add it to the map
     * @param map The target map
     * @param accessor One of the values of MuseumAccessors.ALLOWED_ACCESSORS
     * @param keyName The key retrieved by the means of DataMapping.getKeyName (E.g.: "contact.agent.name")
     * @param value Single string value
     */
    private static void addValue(Map<String, Object> map, String accessor, String keyName, String value) {
        if (value != null) {
            String formattedValue = DataFormatter.format(keyName, value);

            if (MuseumAccessors.DOUBLE_VALUE_ACCESSORS.contains(accessor)) {
                map.put(accessor, Double.parseDouble(formattedValue));
            } else {
                map.put(accessor, formattedValue);
            }
        }
    }
}
