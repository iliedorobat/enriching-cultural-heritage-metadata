package ro.webdata.echo.translator.edm.lido.stats;

import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Writer;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.edm.lido.commons.FileUtils;
import ro.webdata.echo.translator.edm.lido.commons.PlaceMapUtils;
import ro.webdata.echo.translator.edm.lido.commons.StatsUtils;
import ro.webdata.parser.xml.lido.core.ParserDAO;
import ro.webdata.parser.xml.lido.core.ParserDAOImpl;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.parser.xml.lido.core.leaf.eventPlace.EventPlace;
import ro.webdata.parser.xml.lido.core.leaf.lido.Lido;
import ro.webdata.parser.xml.lido.core.leaf.place.Place;
import ro.webdata.parser.xml.lido.core.set.eventSet.EventSet;
import ro.webdata.parser.xml.lido.core.wrap.eventWrap.EventWrap;
import ro.webdata.parser.xml.lido.core.wrap.lidoWrap.LidoWrap;

import java.util.*;

public class PlacesStats {
    private static final ParserDAO parserDAO = new ParserDAOImpl();

    public static void writeAll(String inputPath, String outputFullPath) {
        ArrayList<String> missingPlaces = getMissingPlaces(inputPath, outputFullPath);
        File.write(missingPlaces, outputFullPath, false);
    }

    public static void writeUnique(String inputPath, String outputFullPath) {
        ArrayList<String> missingPlaces = getMissingPlaces(inputPath, outputFullPath);
        Set<String> set = new TreeSet<>(missingPlaces);
        ArrayList<String> uniqueMissingPlaces = new ArrayList<>(set);
        File.write(uniqueMissingPlaces, outputFullPath, false);
    }

    /*
     * Get the places that do not belong to any country and region
     * E.g.:
     *    <lido:eventPlace>
     *       <lido:place
     *         lido:politicalEntity="locality">
     *         <lido:namePlaceSet>
     *           <lido:appellationValue>Steva</lido:appellationValue>
     *         </lido:namePlaceSet>
     *       </lido:place>
     *     </lido:eventPlace>
     */
    public static ArrayList<String> getMissingPlaces(String inputPath, String outputFullPath) {
        ArrayList<String> missingPlaces = new ArrayList<>();
        java.io.File lidoDirectory = new java.io.File(inputPath);
        java.io.File[] subDirectories = lidoDirectory.listFiles();

        // Add the header
        if (!File.exists(outputFullPath)) {
            missingPlaces.add(
                    StatsUtils.prepareLine(
                            PlaceType.COUNTRY,
                            PlaceType.REGION,
                            PlaceType.COUNTY,
                            PlaceType.COMMUNE,
                            PlaceType.LOCALITY,
                            PlaceType.POINT
                    )
            );
        }

        if (subDirectories != null) {
            for (java.io.File file : subDirectories) {
                String fullName = file.getName();
                int dotIndex = fullName.lastIndexOf(".");
                String fileName = fullName.substring(0, dotIndex);

                if (!fileName.startsWith("demo")) {
                    String inputFilePath = FileUtils.getInputFilePath(fileName);
                    missingPlaces.addAll(
                            getFileMissingPlaces(inputFilePath)
                    );
                }
            }
        } else {
            System.err.println(FileConst.PATH_INPUT_LIDO_DIR + " does not contain any directories!");
        }

        return missingPlaces;
    }

    private static ArrayList<String> getFileMissingPlaces(String fullPath) {
        ArrayList<String> missingPlaces = new ArrayList<>();
        LidoWrap lidoWrap = parserDAO.parseLidoFile(fullPath);
        ArrayList<Lido> lidoList = lidoWrap.getLidoList();

        for (Lido lido : lidoList) {
            ArrayList<DescriptiveMetadata> descriptiveMetadataList = lido.getDescriptiveMetadata();
            DescriptiveMetadata descriptiveMetadata = descriptiveMetadataList.size() > 0
                    ? descriptiveMetadataList.get(0)
                    : null;
            EventWrap eventWrap = descriptiveMetadata != null
                    ? descriptiveMetadata.getEventWrap()
                    : null;
            ArrayList<EventSet> eventSetList = eventWrap != null
                    ? eventWrap.getEventSet()
                    : new ArrayList<>();

            for (EventSet eventSet : eventSetList) {
                Event event = eventSet.getEvent();
                addFileMissingPlaces(missingPlaces, event.getEventPlace());
            }
        }

        return missingPlaces;
    }

    private static void addFileMissingPlaces(ArrayList<String> missingPlaces, ArrayList<EventPlace> eventPlaceList) {
        if (eventPlaceList != null) {
            for (EventPlace eventPlace : eventPlaceList) {
                Place place = eventPlace.getPlace();
                addMissingPlace(missingPlaces, place);
            }
        }
    }

    private static void addMissingPlace(ArrayList<String> missingPlaces, Place place) {
        LinkedHashMap<String, HashMap<String, String>> reducedPlaceNameMap = PlaceMapUtils.preparePlaceNameMap(place);
        HashMap<String, String> country = reducedPlaceNameMap.get(PlaceType.COUNTRY);
        HashMap<String, String> region = reducedPlaceNameMap.get(PlaceType.REGION);

        if (country == null && region == null) {
            HashMap<String, String> commune = reducedPlaceNameMap.get(PlaceType.COMMUNE);
            HashMap<String, String> county = reducedPlaceNameMap.get(PlaceType.COUNTY);
            HashMap<String, String> locality = reducedPlaceNameMap.get(PlaceType.LOCALITY);
            HashMap<String, String> point = reducedPlaceNameMap.get(PlaceType.POINT);


            if (commune != null || county != null || locality != null || point != null) {
                missingPlaces.add(
                        StatsUtils.prepareLine(
                                Writer.toString(null),
                                Writer.toString(null),
                                Writer.toString(county),
                                Writer.toString(commune),
                                Writer.toString(locality),
                                Writer.toString(point)
                        )
                );
            }
        }
    }
}
