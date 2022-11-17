package ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.eventWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.EventComplexTypeProcessing;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.parser.xml.lido.core.set.eventSet.EventSet;
import ro.webdata.parser.xml.lido.core.wrap.eventWrap.EventWrap;

import java.util.ArrayList;

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
}
