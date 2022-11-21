package ro.webdata.echo.translator.edm.lido.commons;

import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.parser.xml.lido.core.ParserDAO;
import ro.webdata.parser.xml.lido.core.ParserDAOImpl;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.parser.xml.lido.core.leaf.eventPlace.EventPlace;
import ro.webdata.parser.xml.lido.core.leaf.lido.Lido;
import ro.webdata.parser.xml.lido.core.leaf.place.Place;
import ro.webdata.parser.xml.lido.core.set.eventSet.EventSet;
import ro.webdata.parser.xml.lido.core.wrap.lidoWrap.LidoWrap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class StatsUtils {
    private static final ParserDAO parserDAO = new ParserDAOImpl();

    // E.g.: getUniquePlaceNames(PlaceType.COUNTRY, Const.LANG_RO)
    public static ArrayList<String> getUniquePlaceNames(String placeType, String language) {
        ArrayList<String> placeNames = getPlaceNames(placeType, language);

        return new ArrayList<>(
                new HashSet<>(placeNames)
        );
    }

    // E.g.: getPlaceNames(PlaceType.COUNTRY, Const.LANG_RO)
    public static ArrayList<String> getPlaceNames(String placeType, String language) {
        ArrayList<String> placeNames = new ArrayList<>();
        File lidoDirectory = new File(FileConst.PATH_INPUT_LIDO_DIR);
        File[] subDirectories = lidoDirectory.listFiles();

        if (subDirectories != null) {
            for (File file : subDirectories) {
                int dotIndex = file.getName().lastIndexOf(".");
                String fileName = file.getName().substring(0, dotIndex);

                if (!fileName.startsWith("demo")) {
                    String inputFilePath = FileUtils.getInputFilePath(fileName);
                    LidoWrap lidoWrap = parserDAO.parseLidoFile(inputFilePath);

                    for (Lido lido : lidoWrap.getLidoList()) {
                        ArrayList<DescriptiveMetadata> descriptiveMetadataList = lido.getDescriptiveMetadata();
                        DescriptiveMetadata descriptiveMetadata = descriptiveMetadataList.size() > 0
                                ? descriptiveMetadataList.get(0)
                                : null;

                        if (descriptiveMetadata != null) {
                            ArrayList<EventSet> eventSets = descriptiveMetadata.getEventWrap().getEventSet();
                            placeNames.addAll(
                                    getPlaceNames(eventSets, placeType, language)
                            );
                        }
                    }
                }
            }
        }

        return placeNames;
    }

    // Get the list of places
    private static ArrayList<String> getPlaceNames(ArrayList<EventSet> eventSets, String placeType, String language) {
        ArrayList<String> placeNames = new ArrayList<>();

        for (EventSet eventSet : eventSets) {
            ArrayList<LinkedHashMap<String, HashMap<String, Object>>> places = generateEventPlaceList(eventSet);

            for (LinkedHashMap<String, HashMap<String, Object>> place : places) {
                String placeName = getPlaceName(place, placeType, language);

                if (placeName != null) {
                    placeNames.add(placeName);
                }
            }
        }

        return placeNames;
    }

    // Generate the list of places
    private static ArrayList<LinkedHashMap<String, HashMap<String, Object>>> generateEventPlaceList(EventSet eventSet) {
        ArrayList<EventPlace> eventPlaceList = eventSet.getEvent().getEventPlace();
        ArrayList<LinkedHashMap<String, HashMap<String, Object>>> placeList = new ArrayList<>();

        for (EventPlace eventPlace : eventPlaceList) {
            Place place = eventPlace.getPlace();
            LinkedHashMap<String, HashMap<String, Object>> placeMapList = PlaceMapUtils.getPlaceMap(place);
            placeList.add(placeMapList);
        }

        return placeList;
    }

    // Extract the name of a single place
    private static String getPlaceName(LinkedHashMap<String, HashMap<String, Object>> place, String placeType, String language) {
        HashMap<String, Object> placeMap = place.get(placeType);
        if (placeMap == null) {
            return null;
        }

        HashMap<String, String> placeNameMap = (HashMap<String, String>) placeMap.get(PlaceMapUtils.NAME_PROP);
        return placeNameMap.get(language);
    }
}
