package ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.eventWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Writer;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.edm.lido.commons.PlaceMapUtils;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.EventComplexTypeProcessing;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.parser.xml.lido.core.leaf.eventPlace.EventPlace;
import ro.webdata.parser.xml.lido.core.leaf.place.Place;
import ro.webdata.parser.xml.lido.core.set.eventSet.EventSet;
import ro.webdata.parser.xml.lido.core.wrap.eventWrap.EventWrap;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class EventWrapProcessing {
    public static void mapEntries(
            Model model,
            Resource providedCHO,
            EventWrap eventWrap
    ) {
        addEventWrap(model, providedCHO, eventWrap);
    }

    private static void addEventWrap(Model model, Resource providedCHO, EventWrap eventWrap) {
        if (eventWrap != null) {
            addEventSet(model, providedCHO, eventWrap.getEventSet());
        }
    }

    private static void addEventSet(Model model, Resource providedCHO, ArrayList<EventSet> eventSetList) {
        for (EventSet eventSet : eventSetList) {
            Event event = eventSet.getEvent();
            objectCentricSupport(model, providedCHO, event);
            eventCentricSupport(model, providedCHO, event);
            generateMissingPlaces(event.getEventPlace());
        }
    }

    // object-centric support
    private static void objectCentricSupport(Model model, Resource providedCHO, Event event) {
        EventComplexTypeProcessing.addDate(model, providedCHO, event);
    }

    // event-centric support
    private static void eventCentricSupport(Model model, Resource providedCHO, Event event) {
        EventComplexTypeProcessing.addEvent(model, providedCHO, event);
    }

    private static void generateMissingPlaces(ArrayList<EventPlace> eventPlaceList) {
        for (EventPlace eventPlace : eventPlaceList) {
                generateMissingPlace(eventPlace.getPlace());
        }
    }

    private static void generateMissingPlace(Place place) {
        LinkedHashMap<String, HashMap<String, String>> reducedPlaceNameMap = PlaceMapUtils.preparePlaceNameMap(place);
        HashMap<String, String> country = reducedPlaceNameMap.get(PlaceType.COUNTRY);
        HashMap<String, String> region = reducedPlaceNameMap.get(PlaceType.REGION);

        if (country == null && region == null) {
            HashMap<String, String> commune = reducedPlaceNameMap.get(PlaceType.COMMUNE);
            HashMap<String, String> county = reducedPlaceNameMap.get(PlaceType.COUNTY);
            HashMap<String, String> locality = reducedPlaceNameMap.get(PlaceType.LOCALITY);
            HashMap<String, String> point = reducedPlaceNameMap.get(PlaceType.POINT);
            StringWriter sw = new StringWriter();

            // Add the header
            if (!File.exists(FileConst.PATH_MISSING_COUNTRY_REGION)) {
                Writer.appendLine(
                        sw,
                        PlaceType.COUNTRY,
                        PlaceType.REGION,
                        PlaceType.COUNTY,
                        PlaceType.COMMUNE,
                        PlaceType.LOCALITY,
                        PlaceType.POINT
                );
            }

            Writer.appendLine(
                    sw,
                    Writer.toString(country),
                    Writer.toString(region),
                    Writer.toString(county),
                    Writer.toString(commune),
                    Writer.toString(locality),
                    Writer.toString(point)
            );

            File.write(sw, FileConst.PATH_MISSING_COUNTRY_REGION, true);
        }
    }
}
