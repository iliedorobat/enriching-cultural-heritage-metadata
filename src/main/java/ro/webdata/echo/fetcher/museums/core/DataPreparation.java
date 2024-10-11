package ro.webdata.echo.fetcher.museums.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Json;
import ro.webdata.echo.commons.accessor.MuseumAccessors;
import ro.webdata.echo.fetcher.museums.commons.FilePath;

public class DataPreparation {
    public static void prepareGeoLocation(String lang) {
        String inputPath = FilePath.getFinalJsonPath(lang);
        String outputPath = FilePath.getGeoLocationJsonPath(lang);

        JsonArray rawArray = Json.getJsonArray(inputPath);
        JsonArray preparedArray = new JsonArray();

        for (int i = 0; i < rawArray.size(); i++) {
            JsonElement rawElement = rawArray.get(i);
            JsonElement preparedElement = DataPreparation.prepareElement(rawElement);
            preparedArray.add(preparedElement);
        }

        File.write(preparedArray.toString(), outputPath, false);
    }

    private static JsonElement prepareElement(JsonElement element) {
        JsonObject preparedObject = new JsonObject();
        JsonObject object = element.getAsJsonObject();

        JsonElement code = object.get(MuseumAccessors.CODE);
        preparedObject.add(MuseumAccessors.CODE, code);

        JsonElement name = object.get(MuseumAccessors.MUSEUM_NAME);
        preparedObject.add(MuseumAccessors.MUSEUM_NAME, name);

        JsonObject location = object.get(MuseumAccessors.LOCATION)
                .getAsJsonObject();
        JsonElement geo = location.get(MuseumAccessors.GEO);
        JsonElement county = location.get(MuseumAccessors.COUNTY);
        JsonObject preparedGeo = new JsonObject();
        preparedGeo.add(MuseumAccessors.GEO, geo);
        preparedGeo.add(MuseumAccessors.COUNTY, county);
        preparedObject.add(MuseumAccessors.LOCATION, preparedGeo);

        return preparedObject;
    }
}
