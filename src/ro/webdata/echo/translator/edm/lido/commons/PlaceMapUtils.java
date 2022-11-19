package ro.webdata.echo.translator.edm.lido.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.translator.commons.Env;
import ro.webdata.echo.translator.edm.lido.commons.constants.PlaceConst;
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
        HashMap<String, String> countyMap = reducedPlaceNameMap.get(PlaceType.COUNTY);
        String uri = "";

        for (String placeType : PlaceType.TYPES) {
            // Avoid adding regions for Romanian places
            if (placeType.equals(PlaceType.REGION) && hasRoCounty(countyMap)) {
                continue;
            }

            HashMap<String, String> placeNames = reducedPlaceNameMap.get(placeType);

            if (placeNames != null) {
                uri = preparePlaceUri(placeNames, placeType, uri);

                HashMap<String, Object> placeLinkMap = new HashMap<>();
                placeLinkMap.put(URI_PROP, uri);
                placeLinkMap.put(NAME_PROP, placeNames);
                placesLinkMap.put(placeType, placeLinkMap);
            }
        }

        return placesLinkMap;
    }

    /**
     * Check if it is a Romanian county
     * @param countyMap Map containing county names by language
     * @return True/False
     */
    private static boolean hasRoCounty(HashMap<String, String> countyMap) {
        String roCountyName = countyMap != null
                ? countyMap.get(Const.LANG_RO)
                : null;

        if (roCountyName != null) {
            String lcPlaceName = StringUtils.stripAccents(roCountyName.trim()).toLowerCase();

            return PlaceConst.SANITIZED_RO_COUNTIES.contains(lcPlaceName);
        }

        return false;
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
            String placeName = placeNameMap.getValue();
            String preparedPlaceName = StringUtils.stripAccents(
                    Text.sanitizeString(
                            WordUtils.capitalizeFully(placeName)
                    )
            );

            return baseUri + placeType + ":" + preparedPlaceName + File.FILE_SEPARATOR;
        }

        return baseUri;
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
        HashMap<String, PlaceComplexType> placeMap = Preparation.preparePlaceMap(place);

        Curation.country(map, placeMap);
        Curation.region(map, placeMap);
        Curation.county(map, placeMap);
        Curation.commune(map, placeMap);
        Curation.locality(map, placeMap);
        Curation.point(map, placeMap);

        return map;
    }
}

class Curation {
    protected static void country(LinkedHashMap<String, HashMap<String, String>> map, HashMap<String, PlaceComplexType> placeMap) {
        HashMap<String, String> nameMap = Preparation.preparePlaceNameMap(placeMap, PlaceType.COUNTRY);
        String placeName = nameMap.get(Const.LANG_RO);
        String placeType = Cleaning.sanitizePlaceType(placeName, PlaceType.COUNTRY);

        if (!nameMap.isEmpty()) {
            if (placeName != null && placeName.equals("Regatul Unit al Marii Britanii și Irlandei de Nord")) {
                nameMap.put(Const.LANG_RO, "Marea Britanie");
            }

            map.put(placeType, nameMap);
        }
    }

    protected static void region(LinkedHashMap<String, HashMap<String, String>> map, HashMap<String, PlaceComplexType> placeMap) {
        HashMap<String, String> nameMap = Preparation.preparePlaceNameMap(placeMap, PlaceType.REGION);
        String placeName = nameMap.get(Const.LANG_RO);
        String placeType = Cleaning.sanitizePlaceType(placeName, PlaceType.REGION);

        if (!nameMap.isEmpty()) {
            // TODO: check for regions before adding the country (similar to how it is done in the case of counties)
            addMissingPlace(map, PlaceType.COUNTRY, "România");
            String regionName = Cleaning.sanitizeRegionName(
                    nameMap.get(Const.LANG_RO)
            );
            nameMap.put(Const.LANG_RO, regionName);
            map.put(placeType, nameMap);

            if (placeName != null) {
                String lcPlaceName = StringUtils.stripAccents(placeName.trim()).toLowerCase();

                // "Sibiu, Transilvania", "Sălaj, Transilvania"
                if (lcPlaceName.equals("sibiu, transilvania") || lcPlaceName.equals("salaj, transilvania")) {
                    String countyName = placeName.split(",")[0];
                    addMissingPlace(map, PlaceType.COUNTY, countyName);
                }
            }
        }
    }

    protected static void county(LinkedHashMap<String, HashMap<String, String>> map, HashMap<String, PlaceComplexType> placeMap) {
        HashMap<String, String> nameMap = Preparation.preparePlaceNameMap(placeMap, PlaceType.COUNTY);
        String placeName = nameMap.get(Const.LANG_RO);
        String placeType = Cleaning.sanitizePlaceType(placeName, PlaceType.COUNTY);

        if (!nameMap.isEmpty()) {
            String countyName = Cleaning.sanitizeCountyName(placeName);

            if (countyName != null) {
                String lcPlaceName = StringUtils.stripAccents(countyName.trim()).toLowerCase();

                if (PlaceConst.SANITIZED_RO_COUNTIES.contains(lcPlaceName)) {
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                }

                nameMap.put(Const.LANG_RO, countyName);
            }

            map.put(placeType, nameMap);
        }
    }

    protected static void commune(LinkedHashMap<String, HashMap<String, String>> map, HashMap<String, PlaceComplexType> placeMap) {
        HashMap<String, String> nameMap = Preparation.preparePlaceNameMap(placeMap, PlaceType.COMMUNE);
        String placeName = nameMap.get(Const.LANG_RO);
        String placeType = Cleaning.sanitizePlaceType(placeName, PlaceType.COMMUNE);

        if (!nameMap.isEmpty()) {
            if (placeName != null) {
                String lcPlaceName = StringUtils.stripAccents(placeName.trim()).toLowerCase();

                if (!lcPlaceName.contains("udesti")) {
                    HashMap<String, String> countyNameMap = map.get(PlaceType.COUNTY);

                    if (countyNameMap == null) {
                        addMissingPlace(map, PlaceType.COUNTRY, "România");
                        addMissingPlace(map, PlaceType.COUNTY, "Suceava");
                    }
                }
            }

            map.put(placeType, nameMap);
        }
    }

    protected static void locality(LinkedHashMap<String, HashMap<String, String>> map, HashMap<String, PlaceComplexType> placeMap) {
        HashMap<String, String> nameMap = Preparation.preparePlaceNameMap(placeMap, PlaceType.LOCALITY);
        String placeName = nameMap.get(Const.LANG_RO);
        String placeType = Cleaning.sanitizePlaceType(placeName, PlaceType.LOCALITY);

        if (!nameMap.isEmpty() && placeName != null) {
            HashMap<String, String> countryMap = map.get(PlaceType.COUNTRY);
            HashMap<String, String> countyMap = map.get(PlaceType.COUNTY);
            String countryName = countryMap != null
                    ? countryMap.get(Const.LANG_RO)
                    : null;
            String lcPlaceName = StringUtils.stripAccents(placeName.trim()).toLowerCase();

            switch (lcPlaceName) {
                // "Arad"
                case "arad":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Arad");
                    break;

                // "Botoșani"
                case "botosani":
                    if (countyMap == null && countryMap == null) {
                        addMissingPlace(map, PlaceType.COUNTRY, "România");
                        addMissingPlace(map, PlaceType.COUNTY, "Botoșani");
                    }
                    break;

                // "Brașov"
                case "brasov":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Brașov");
                    break;

                // "Bucrești", "București", "Bucuresti", "BUCUREȘTI", "BUCUREȘTI (Târgul moșilor)",
                case "bucresti":
                case "bucuresti":
                case "bucuresti (targul mosilor)":
                    // "Ilfov" county was added for some entries, but "București" is not part of any county
                    map.remove(PlaceType.COUNTY);

                    // For some entries, the country is missing
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    nameMap.put(Const.LANG_RO, "București");
                    break;

                // "Budapesta"
                case "budapesta":
                    addMissingPlace(map, PlaceType.COUNTRY, "Ungaria");
                    break;

                // "Butin (Timiș)", "Cebza (Timiș)", "Jebel (Timiș)"
                case "butin (timis)":
                case "cebza (timis)":
                case "jebel (timis)":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Timiș");
                    nameMap.put(Const.LANG_RO, placeName.split(" \\(")[0]);
                    break;

                // "Câmpulung Moldovenesc"
                case "campulung moldovenesc":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Suceava");
                    break;

                // "Cașva"
                case "casva":
                    String countyName = countyMap.get("ro");

                    if (countyName != null && countyName.equals("Mureș")) {
                        addMissingPlace(map, PlaceType.COUNTRY, "România");
                    }

                    break;

                // "Clopodia, județul Timiș", "Lăpușnic, județ Timiș", "Hodoș, județ Timiș"
                case "clopodia, judetul timis":
                case "lapusnic, judet timis":
                case "hodos, judet timis":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Timiș");
                    nameMap.put(Const.LANG_RO, placeName.split(",")[0]);
                    break;

                // "Cluj", "Cluj-Napoca"
                case "cluj":
                case "cluj-napoca":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Cluj");
                    nameMap.put(Const.LANG_RO, "Cluj-Napoca");
                    break;

                // "Hațeg"
                case "hateg":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Hunedoara");
                    break;

                // "Hodac Hodac"
                case "hodac hodac":
                    nameMap.put(Const.LANG_RO, "Hodac");
                    break;

                // "IAȘI"
                case "iasi":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Iași");
                    nameMap.put(Const.LANG_RO, "Iași");
                    break;

                // "Ilisești (Suceava)"
                case "ilisesti (suceava)":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Suceava");
                    addMissingPlace(map, PlaceType.COMMUNE, "Ilisești");
                    nameMap.put(Const.LANG_RO, "Ilisești");
                    break;

                // "Logig"
                case "logig":
                    HashMap<String, String> communeMap = map.get(PlaceType.COMMUNE);
                    String communeName = communeMap.get("ro");

                    // There is an entry for which the county "Maramureș" was assigned, but it should be "Mureș"
                    if (communeName != null && communeName.equals("Lunca")) {
                        countyMap.put("ro", "Mureș");
                        map.put(PlaceType.COUNTY, countyMap);
                    }

                    break;

                // "Londra"
                case "londra":
                    if (map.containsKey(PlaceType.COUNTRY)) {
                        // Replace "Marea Britanie" with "Anglia"
                        if (countryName != null && countryName.equals("Marea Britanie")) {
                            countryMap.put(Const.LANG_RO, "Anglia");
                        }
                    }
                    // Add the country if it is missing
                    addMissingPlace(map, PlaceType.COUNTRY, "Anglia");
                    break;

                // "Moscova"
                case "moscova":
                    addMissingPlace(map, PlaceType.COUNTRY, "Rusia");
                    break;

                // "necunoscută", "Necunoscută", "nu are"
                case "necunoscuta":
                case "nu are":
                    nameMap.remove(Const.LANG_RO);
                    break;

//                // "Olănești (Basarabia)"
//                case "olanesti (basarabia)":
//                    addMissingPlace(map, PlaceType.COUNTRY, "Republica Moldova");
//                    nameMap.put(Const.LANG_RO, "Olănești");
//                    break;

                // "ORADEA", "Oradea Mare"
                case "oradea":
                case "oradea mare":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Bihor");
                    // replace ORADEA with Oradea
                    if (lcPlaceName.equals("oradea")) {
                        nameMap.put(Const.LANG_RO, "Oradea");
                    }
                    break;

                // "Șelimbăr", "Sibiu"
                case "selimbar":
                case "sibiu":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Sibiu");
                    break;

                // "Strassbourg"
                case "strassbourg":
                    addMissingPlace(map, PlaceType.COUNTRY, "Franța");
                    nameMap.put(Const.LANG_RO, "Strasbourg");
                    break;

                // "Târgu Mureș"
                case "targu mures":
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Mureș");
                    break;

                default:
                    break;
            }

            if (!nameMap.isEmpty()) {
                map.put(placeType, nameMap);
            }
        }
    }

    protected static void point(LinkedHashMap<String, HashMap<String, String>> map, HashMap<String, PlaceComplexType> placeMap) {
        HashMap<String, String> nameMap = Preparation.preparePlaceNameMap(placeMap, PlaceType.POINT);
        String placeName = nameMap.get(Const.LANG_RO);
        String placeType = Cleaning.sanitizePlaceType(placeName, PlaceType.POINT);

        if (!nameMap.isEmpty() && placeName != null) {
            // The placeName may or may not have quotes: "\"Dealul Ruina\"" or "Dealul Ruina"
            if (placeName.equals("Dealul Ruina") || placeName.equals("\"Dealul Ruina\"") || placeName.equals("Dealu Ruina")) {
                HashMap<String, String> localityNameMap = map.get(PlaceType.LOCALITY);
                String localityName = localityNameMap.get(Const.LANG_RO);

                if (localityName != null && localityName.equals("Siret")) {
                    addMissingPlace(map, PlaceType.COUNTRY, "România");
                    addMissingPlace(map, PlaceType.COUNTY, "Suceava");
                    nameMap.put(Const.LANG_RO, "Dealul Ruina");

                    map.put(PlaceType.LOCALITY, localityNameMap);
                }
            }

            map.put(placeType, nameMap);
        }
    }

    private static void addMissingPlace(LinkedHashMap<String, HashMap<String, String>> map, String placeType, String countryName) {
        if (!map.containsKey(placeType)) {
            HashMap<String, String> countryNameMap = new HashMap<>();
            countryNameMap.put(Const.LANG_RO, countryName);
            map.put(placeType, countryNameMap);
        }
    }
}

class Preparation {
    /**
     * Prepare a map containing all the places.
     * @param place The related place
     */
    protected static HashMap<String, PlaceComplexType> preparePlaceMap(PlaceComplexType place) {
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
    protected static HashMap<String, String> preparePlaceNameMap(HashMap<String, PlaceComplexType> placeMap, String placeType) {
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
    protected static boolean isMainLang(String language) {
        return language == null || language.equals(Env.LANG_MAIN);
    }
}

class Cleaning {
    protected static String sanitizeCountyName(String placeName) {
        if (placeName == null) {
            return null;
        }

        switch (placeName) {
            case "jud. Hunedoara":
                return "Hunedoara";
            case "Mureș Mureș":
            case "Mureș M MUREȘ":
            case "Mureș MU":
                return "Mureș";
            case "jud. Sibiu":
                return "Sibiu";
            default:
                return placeName;
        }
    }

    protected static String sanitizeRegionName(String placeName) {
        if (placeName == null) {
            return null;
        }

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

    protected static String sanitizePlaceType(String placeName, String placeType) {
        // E.g.: "jud. Hunedoara", "jud. Sibiu"
        if (placeName != null && placeName.startsWith("jud")) {
            return PlaceType.COUNTY;
        }

        return placeType;
    }
}
