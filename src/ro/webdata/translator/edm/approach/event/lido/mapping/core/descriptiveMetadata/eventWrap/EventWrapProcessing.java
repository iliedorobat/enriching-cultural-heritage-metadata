package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.eventWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.parser.xml.lido.core.set.eventSet.EventSet;
import ro.webdata.parser.xml.lido.core.wrap.eventWrap.EventWrap;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.EventComplexTypeProcessing;

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
            addEvent(model, providedCHO, eventSet.getEvent());
        }
    }

    private static void addEvent(Model model, Resource providedCHO, Event event) {
        EventComplexTypeProcessing.addEvent(model, providedCHO, event);
    }
}
