package ro.webdata.echo.translator.edm.approach.event.lido.commons;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.translator.commons.Env;
import ro.webdata.parser.xml.lido.core.complex.placeComplexType.PlaceComplexType;
import ro.webdata.parser.xml.lido.core.leaf.appellationValue.AppellationValue;
import ro.webdata.parser.xml.lido.core.leaf.partOfPlace.PartOfPlace;
import ro.webdata.parser.xml.lido.core.leaf.place.Place;
import ro.webdata.parser.xml.lido.core.set.namePlaceSet.NamePlaceSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PlaceMapUtils {
    public static final String NAME_PROP = "name";
    public static final String URI_PROP = "uri";

    public static LinkedHashMap<String, HashMap<String, Object>> getPlaceMap(Place place) {
        LinkedHashMap<String, HashMap<String, String>> reducedPlaceNameMap = preparePlaceNameMap(place);
        LinkedHashMap<String, HashMap<String, Object>> placesLinkMap = new LinkedHashMap<>();
        String uri = "";

        for (Map.Entry<String, HashMap<String, String>> placeMap : reducedPlaceNameMap.entrySet()) {
            String placeType = placeMap.getKey();
            HashMap<String, String> placeNames = placeMap.getValue();
            uri = preparePlaceUri(placeNames, placeType, uri);

            HashMap<String, Object> placeLinkMap = new HashMap<>();
            placeLinkMap.put(URI_PROP, uri);
            placeLinkMap.put(NAME_PROP, placeNames);
            placesLinkMap.put(placeType, placeLinkMap);
        }

        return placesLinkMap;
    }

    /**
     * Prepare the URI of the place of the placeType category<br/><br/>
     * E.g.:<br/>
     *      - http://opendata.cs.pub.ro/resource/place/country:Romania/
     *      - http://opendata.cs.pub.ro/resource/place/country:Romania/county:Sibiu/
     * @param placeNames Map containing the names of a place in different languages
     * @param placeType The type of the place (country; region; county; commune; locality; point)
     * @param baseUri The base URI used to create the final URI<br/>
     *                E.g.:<br/>
     *                      - http://opendata.cs.pub.ro/resource/place/                     +       country:Romania/<br/>
     *                      - http://opendata.cs.pub.ro/resource/place/country:Romania/     +       county:Sibiu/
     * @return URI pointing a place
     */
    private static String preparePlaceUri(HashMap<String, String> placeNames, String placeType, String baseUri) {
        for (Map.Entry<String, String> placeNameMap : placeNames.entrySet()) {
            String lang = placeNameMap.getKey();
            String placeName = placeNameMap.getValue();

            if (isMainLang(lang)) {
                String preparedPlaceName = StringUtils.stripAccents(
                        Text.sanitizeString(placeName)
                );
                return baseUri + placeType + ":" + preparedPlaceName + File.FILE_SEPARATOR;
            }
        }

        return null;
    }

    /**
     * Prepare a sorted key-value map containing the names of the political entities related to the input place<br/>
     * Sorting is based on the "top level" principle of political entities<br/><br/>
     *
     * country > region > county > commune > locality > point
     * @param place The related place
     * @return Map containing the names of the political entities
     */
    private static LinkedHashMap<String, HashMap<String, String>> preparePlaceNameMap(Place place) {
        LinkedHashMap<String, HashMap<String, String>> map = new LinkedHashMap<>();
        HashMap<String, PlaceComplexType> placeMap = preparePlaceMap(place);
        HashMap<String, String> country = getPlaceNameMap(placeMap, PlaceType.COUNTRY);

        // Each place must be located in a country
        if (country.size() > 0) {
            for (String placeType : PlaceType.TYPES) {
                HashMap<String, String> placeNameMap = getPlaceNameMap(placeMap, placeType);

                if (!placeNameMap.isEmpty()) {
                    map.put(placeType, placeNameMap);
                }
            }
        } else {
            System.err.println(PlaceMapUtils.class.getName() + ": The country name is missing!");
        }

        return map;
    }

    /**
     * Prepare a map containing all the places.
     * @param place The related place
     */
    public static HashMap<String, PlaceComplexType> preparePlaceMap(PlaceComplexType place) {
        HashMap<String, PlaceComplexType> placeMap = new HashMap<>();
        ArrayList<PartOfPlace> partOfPlaceList = place.getPartOfPlace();

        if (partOfPlaceList != null) {
            for (PartOfPlace partOfPlace : partOfPlaceList) {
                placeMap.putAll(
                        preparePlaceMap(partOfPlace)
                );
            }
        }

        addPlaceByType(place, placeMap);

        return placeMap;
    }

    /**
     * Add a place to the sorted key-value list of places
     * @param place The related place
     * @param placeMap Sorted key-value list of places
     */
    private static void addPlaceByType(PlaceComplexType place, HashMap<String, PlaceComplexType> placeMap) {
        String placeType = place.getPoliticalEntity().getAttrValue();

        if (!placeMap.containsKey(placeType)) {
            placeMap.put(placeType, place);
        }
    }

    /**
     * Extract the names of political entities
     * @param placeMap Sorted key-value list of places
     * @param placeType The political entity type (E.g.: country, region, county, commune, locality, point)
     * @return Map containing the list of names of political entities
     */
    private static HashMap<String, String> getPlaceNameMap(HashMap<String, PlaceComplexType> placeMap, String placeType) {
        HashMap<String, String> placeNameMap = new HashMap<>();
        PlaceComplexType place = placeMap.get(placeType);

        if (place != null) {
            for (NamePlaceSet namePlaceSet : place.getNamePlaceSet()) {
                for (AppellationValue appellationValue : namePlaceSet.getAppellationValue()) {
                    addPlaceName(placeNameMap, appellationValue);
                }
            }
        }

        return placeNameMap;
    }

    /**
     * Add key-value entry to placeNameMap
     * @param placeNameMap HashMap
     * @param appellationValue Political entity name
     */
    private static void addPlaceName(HashMap<String, String> placeNameMap, AppellationValue appellationValue) {
        String lang = appellationValue.getLang().getLang();
        String placeName = appellationValue.getText();

        /*
        Avoid using punctuation for place names
        E.g.:
            <lido:namePlaceSet>
                <lido:appellationValue>?</lido:appellationValue>
            </lido:namePlaceSet>
         */
        if (Pattern.matches("\\p{Punct}", placeName)) {
            System.err.println(PlaceMapUtils.class.getName() + ": The name of the place cannot be a punctuation mark!");
        } else {
            String language = isMainLang(lang)
                    ? Env.LANG_MAIN
                    : lang;
            placeNameMap.put(language, placeName);
        }
    }

    /**
     * Check if the language is missing or the same as the main language
     * @param language The searching language
     * @return True/False
     */
    private static boolean isMainLang(String language) {
        return language == null || language.equals(Env.LANG_MAIN);
    }
}
