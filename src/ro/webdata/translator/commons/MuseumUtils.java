package ro.webdata.translator.commons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.lang3.StringUtils;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Json;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.parser.xml.lido.core.leaf.legalBodyID.LegalBodyID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;
import static ro.webdata.translator.commons.EnvConstants.NS_REPO_RESOURCE_ORGANIZATION;

public class MuseumUtils {
    public static final String enPath = getCimecJsonPath(Const.LANG_EN);
    public static final String roPath = getCimecJsonPath(Const.LANG_RO);

    public static final JsonArray enJsonArray = Json.getJsonArray(enPath);
    public static final JsonArray roJsonArray = Json.getJsonArray(roPath);

    public static List<String> getCimecCodeList(JsonArray jsonArray) {
        List<String> cimecCodeList = new ArrayList<>();

        for (JsonElement museumJson : jsonArray) {
            JsonObject object = museumJson.getAsJsonObject();
            String cimecCode = object.get(CODE).getAsString();
            cimecCodeList.add(cimecCode);
        }

        return cimecCodeList;
    }

    public static String getCimecCode(ArrayList<LegalBodyID> legalBodyIDList, JsonArray jsonArray) {
        List<String> cimecCodeList = MuseumUtils.getCimecCodeList(jsonArray);

        for (LegalBodyID legalBodyID : legalBodyIDList) {
            String cimecCode = legalBodyID.getText();

            if (cimecCodeList.contains(cimecCode)) {
                return cimecCode;
            }
        }

        return null;
    }

    public static boolean hasCimecCode(JsonArray jsonArray, String cimecCode) {
        List<String> cimecCodeList = MuseumUtils.getCimecCodeList(jsonArray);

        return cimecCodeList.contains(cimecCode);
    }

    /**
     * Create a unique identifier based on the name of the museum and the county in which it is located
     * @param museumCode Current iterated museum code
     * @return Unique identifier
     */
    public static String generateMuseumId(String museumCode) {
        String museumName = getEnStringValue(museumCode, MUSEUM_NAME.split("\\."));
        String county = getEnStringValue(museumCode, LOCATION_COUNTY.split("\\."));

        return museumName != null && county != null ? (
                NS_REPO_RESOURCE_ORGANIZATION
                        + PlaceType.COUNTRY + ":" + Constants.ROMANIAN_COUNTRY_NAME
                        + File.FILE_SEPARATOR
                        + PlaceType.COUNTY + ":" + sanitizeCountyName(county, Constants.ROMANIAN_COUNTRY_NAME_SEPARATOR)
                        + File.FILE_SEPARATOR
                        + sanitizeString(museumName)
                        + File.FILE_SEPARATOR
        ) : null;
    }

    /**
     * Strip accents, remove all quotation marks, punctuation, and replace
     * non-alphanumeric characters with "_" for each part of the countyName
     *
     * E.g.:
     *      - countyName: "Bistrița-Năsăud"
     *      - separator: "-"
     *      - result: "Bistrita-Nasaud"
     *
     * @param countyName The name of the county
     * @param separator The character used to delimit two words in the name of the county
     * @return Cleaned value
     */
    public static String sanitizeCountyName(String countyName, String separator) {
        List<String> countyArr = Arrays.stream(countyName.split(separator))
                .map(MuseumUtils::sanitizeString)
                .collect(Collectors.toList());

        return countyArr.stream()
                .reduce("", (acc, item) -> acc.equals("") ? item : acc + "-" + item);
    }

    /**
     * Strip accents, remove all quotation marks, punctuation, and replace
     * non-alphanumeric characters with "_"
     * @param museumName The input string
     * @return Cleaned value
     */
    public static String sanitizeString(String museumName) {
        if (museumName == null) {
            return null;
        }

        char chr = museumName.charAt(0);
        String unicode = String.format ("\\u%04x", (int)chr);
        String QUOTATION_MARKS_REGEX = "[\\u0022\u0027\u201a\u201b\u201c\u201d\u201e\u201f\u2018\u2019]";

        return StringUtils.stripAccents(museumName)
                .replaceAll(QUOTATION_MARKS_REGEX, "")
                .replaceAll("\\p{Punct}", "")
                .replaceAll("\\W", "_");
    }

    /**
     * Extract the value from the English dataset corresponding to the properties' path
     * @param code CIMEC code used to extract the element from the English dataset
     * @param propertyList Properties' path (E.g.: ["location", "county"])
     * @return String extracted from the English dataset
     */
    private static String getEnStringValue(String code, String[] propertyList) {
        JsonElement jsonElement = getEnElement(code,propertyList);

        if (jsonElement instanceof JsonPrimitive) {
            if (((JsonPrimitive) jsonElement).isString()) {
                return jsonElement.getAsString();
            }
        }

        return null;
    }

    /**
     * Get the primitive value of the English element corresponding to the properties' path
     * @param code CIMEC code used to extract the element from the English dataset
     * @param propertyList Properties' path (E.g.: ["location", "county"])
     * @return Primitive value extracted from the English dataset
     */
    private static JsonElement getEnElement(String code, String[] propertyList) {
        List<String> properties = Arrays.asList(propertyList);

        for (JsonElement museumJson : enJsonArray) {
            JsonObject object = museumJson.getAsJsonObject();
            String crrCode = object.get(CODE).getAsString();

            if (code.equals(crrCode)) {
                return getJsonValue(object, properties, 0);
            }
        }

        return null;
    }

    /**
     * Get the primitive value of the input element corresponding to the properties' path
     *
     * E.g.:
     *      - object:
     *          {
     *              [...]
     *              "location": {
     *                  "address": "Strada Victor Ștefănescu nr. 8",
     *                  "county": "Argeș",
     *                  [...]
     *              },
     *              [...]
     *          }
     *      - properties' path: "location.county"
     *      - result: "Argeș"
     *
     * @param baseElement Current iterated museum
     * @param properties Properties' path (E.g.: ["location", "county"])
     * @param index The index of the current iterated property
     * @return Primitive value extracted from the input object
     */
    private static JsonElement getJsonValue(JsonElement baseElement, List<String> properties, int index) {
        if (baseElement instanceof JsonObject) {
            JsonObject object = baseElement.getAsJsonObject();
            int nextIndex = index + 1;
            String nextProperty = properties.get(index);
            JsonElement nextElement = object.get(nextProperty);

            return getJsonValue(nextElement, properties, nextIndex);
        }

        return baseElement;
    }

    private static String getCimecJsonPath(String lang) {
        return File.PATH_INPUT_DIR
                + File.FILE_SEPARATOR
                + "cimec"
                + File.FILE_SEPARATOR
                + "merged"
                + StringUtils.capitalize(lang)
                + "PreparedData"
                + File.EXTENSION_SEPARATOR
                + File.EXTENSION_JSON;
    }
}
