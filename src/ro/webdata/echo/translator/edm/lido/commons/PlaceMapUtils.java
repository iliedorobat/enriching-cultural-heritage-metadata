package ro.webdata.echo.translator.edm.lido.commons;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.translator.commons.Env;
import ro.webdata.parser.xml.lido.core.complex.placeComplexType.PlaceComplexType;
import ro.webdata.parser.xml.lido.core.leaf.appellationValue.AppellationValue;
import ro.webdata.parser.xml.lido.core.leaf.partOfPlace.PartOfPlace;
import ro.webdata.parser.xml.lido.core.leaf.place.Place;
import ro.webdata.parser.xml.lido.core.set.namePlaceSet.NamePlaceSet;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaceMapUtils {
    public static final String[] RO_COUNTIES = {
            "Alba",
            "Arad",
            "Argeș",
            "Bacău",
            "Bihor",
            "Bistrița-Năsăud",
            "Botoșani",
            "Brașov",
            "Brăila",
            "București",
            "Buzău",
            "Caraș-Severin",
            "Călărași",
            "Cluj",
            "Constanța",
            "Covasna",
            "Dâmbovița",
            "Dolj",
            "Galați",
            "Giurgiu",
            "Gorj",
            "Harghita",
            "Hunedoara",
            "Ialomița",
            "Iași",
            "Ilfov",
            "Maramureș",
            "Mehedinți",
            "Mureș",
            "Neamț",
            "Olt",
            "Prahova",
            "Satu Mare",
            "Sălaj",
            "Sibiu",
            "Suceava",
            "Teleorman",
            "Timiș",
            "Tulcea",
            "Vaslui",
            "Vâlcea",
            "Vrancea"
    };
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
    public static LinkedHashMap<String, HashMap<String, String>> preparePlaceNameMap(Place place) {
        LinkedHashMap<String, HashMap<String, String>> map = new LinkedHashMap<>();
        HashMap<String, PlaceComplexType> placeMap = preparePlaceMap(place);
        List<String> roCountyList = Arrays.stream(RO_COUNTIES)
                .map(county -> StringUtils.stripAccents(county.toLowerCase()))
                .collect(Collectors.toList());

        for (String placeType : PlaceType.TYPES) {
            HashMap<String, String> placeNameMap = getPlaceNameMap(placeMap, placeType);

            if (!placeNameMap.isEmpty()) {
                // Sanitize place name & place type
                String roPlaceName = placeNameMap.get(Const.LANG_RO);
                String sanitizedPlaceType = sanitizePlaceType(roPlaceName, placeType);

                if (placeType.equals(PlaceType.COUNTRY)) {
                    if (roPlaceName.equals("Regatul Unit al Marii Britanii și Irlandei de Nord")) {
                        placeNameMap.put(Const.LANG_RO, "Marea Britanie");
                        map.put(sanitizedPlaceType, placeNameMap);
                    }
                }

                else if (placeType.equals(PlaceType.REGION)) {
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    placeNameMap.put(Const.LANG_RO, sanitizeRegionName(placeNameMap.get(Const.LANG_RO)));
                    map.put(sanitizedPlaceType, placeNameMap);

                    if (roPlaceName.equals("Sibiu, Transilvania") || roPlaceName.equals("Sălaj, Transilvania")) {
                        String countyName = roPlaceName.split(",")[0];
                        HashMap<String, String> countyNameMap = new HashMap<>();
                        countyNameMap.put(Const.LANG_RO, countyName);
                        map.put(PlaceType.COUNTY, countyNameMap);
                    }
                }

                else if (placeType.equals(PlaceType.COUNTY)) {
                    if (roCountyList.contains(StringUtils.stripAccents(roPlaceName.toLowerCase()))) {
                        addMissingPlace(map, PlaceType.COUNTRY, "România");
                    }
                    placeNameMap.put(Const.LANG_RO, sanitizeCountyName(placeNameMap.get(Const.LANG_RO)));
                    map.put(sanitizedPlaceType, placeNameMap);
                }

                else if (placeType.equals(PlaceType.LOCALITY)) {
                    switch (roPlaceName) {
                        case "Arad":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Arad");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "București":
                        case "Bucuresti":
                        case "BUCUREȘTI":
                        case "BUCUREȘTI (Târgul moșilor)":
                            // "Ilfov" county was added for some entries, but "București" is not part of any county
                            map.remove(PlaceType.COUNTY);

                            // For some entries, the country is missing
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            placeNameMap.put(Const.LANG_RO, "București");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Budapesta":
                            addMissingPlace(map, PlaceType.COUNTRY, "Ungaria");
                            map.put(sanitizedPlaceType, placeNameMap);

                        case "Brașov":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Brașov");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Câmpulung Moldovenesc":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Bucovina");
                            addMissingPlace(map, PlaceType.COUNTY, "Suceava");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Cluj":
                        case "Cluj-Napoca":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Cluj");
                            placeNameMap.put(Const.LANG_RO, "Cluj-Napoca");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "IAȘI":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Moldova");
                            addMissingPlace(map, PlaceType.COUNTY, "Iași");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Clopodia, județul Timiș":
                        case "Lăpușnic, județ Timiș":
                        case "Hodoș, județ Timiș":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Timiș");
                            placeNameMap.put(Const.LANG_RO, roPlaceName.split(",")[0]);
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Hațeg":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Hunedoara");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Olănești (Basarabia)":
                            addMissingPlace(map, PlaceType.COUNTRY, "Republica Moldova");
                            placeNameMap.put(Const.LANG_RO, "Olănești");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "ORADEA":
                        case "Oradea Mare":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
//                            addMissingPlace(map, PlaceType.REGION, "Crișana");
                            addMissingPlace(map, PlaceType.COUNTY, "Bihor");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Târgu Mureș":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Mureș");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        // The country & county are missing in the case of the localities "Șelimbăr" and "Sibiu"
                        case "Șelimbăr":
                        case "Sibiu":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Sibiu");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        // The country & county are missing in some cases of the locality "Londra"
                        case "Londra":
                            if (map.containsKey(PlaceType.COUNTRY)) {
                                Map<String, String> countryMap = map.get(PlaceType.COUNTRY);
                                String roCountryName = countryMap.get(Const.LANG_RO);

                                // Replace "Marea Britanie" with "Anglia"
                                if (roCountryName.equals("Marea Britanie")) {
                                    countryMap.put(Const.LANG_RO, "Anglia");
                                }
                            }
                            // For some entries, the country is missing
                            addMissingPlace(map, PlaceType.COUNTRY, "Anglia");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Moscova":
                            addMissingPlace(map, PlaceType.COUNTRY, "Rusia");
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "Butin (Timiș)":
                        case "Cebza (Timiș)":
                        case "Jebel (Timiș)":
                            addMissingPlace(map, PlaceType.COUNTRY, "România");
                            addMissingPlace(map, PlaceType.REGION, "Transilvania");
                            addMissingPlace(map, PlaceType.COUNTY, "Timiș");
                            placeNameMap.put(Const.LANG_RO, roPlaceName.split(" \\(")[0]);
                            map.put(sanitizedPlaceType, placeNameMap);
                            break;

                        case "necunoscută":
                        case "Necunoscută":
                        case "nu are":
                            placeNameMap.remove(Const.LANG_RO);
                            break;
                        default:
                            map.put(sanitizedPlaceType, placeNameMap);
                    }
                } else {
                    map.put(sanitizedPlaceType, placeNameMap);
                }
            }
        }

        return map;
    }

    private static void addMissingPlace(LinkedHashMap<String, HashMap<String, String>> map, String placeType, String countryName) {
        if (!map.containsKey(placeType)) {
            HashMap<String, String> countryNameMap = new HashMap<>();
            countryNameMap.put(Const.LANG_RO, countryName);
            map.put(placeType, countryNameMap);
        }
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

    private static String sanitizeCountyName(String placeName) {
        switch (placeName) {
            case "jud. Hunedoara":
                return "Hunedoara";
            case "jud. Sibiu":
                return "Sibiu";
            default:
                return placeName;
        }
    }

    private static String sanitizeRegionName(String placeName) {
        switch (placeName) {
            case "Dobrofgea":
            case "Dobrogea (?)":
                return "Dobrogea";
            case "Modova":
            case "MOLDOVA DE SUD":
            case "sudul Moldovei":
            case "Sudul Moldovei":
                return "Moldova";
            case "Tansilvania":
            case "Transilvaniu":
            case "centrul Transilvaniei de astăzi":
            case "sudul Transilvaniei":
            case "Sălaj, Transilvania":
            case "Sibiu, Transilvania":
                return "Transilvania";
            default:
                return placeName;
        }
    }

    private static String sanitizePlaceType(String placeName, String placeType) {
        // E.g.: "jud. Hunedoara", "jud. Sibiu"
        if (placeName != null && placeName.startsWith("jud")) {
            return PlaceType.COUNTY;
        }

        return placeType;
    }
}
