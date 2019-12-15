package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.eventWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.EventComplexTypeProcessing;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.parser.xml.lido.core.set.eventSet.EventSet;
import ro.webdata.parser.xml.lido.core.wrap.eventWrap.EventWrap;

import java.util.ArrayList;

public class EventWrapProcessing {
    private static EventComplexTypeProcessing eventComplexTypeProcessing = new EventComplexTypeProcessing();

    public void processing(
            Model model,
            Resource providedCHO,
            EventWrap eventWrap
    ) {
        addEventWrap(model, providedCHO, eventWrap);
    }

    private void addEventWrap(Model model, Resource providedCHO, EventWrap eventWrap) {
        if (eventWrap != null) {
            addEventSet(model, providedCHO, eventWrap.getEventSet());
        }
    }

    private void addEventSet(Model model, Resource providedCHO, ArrayList<EventSet> eventSetList) {
        for (int i = 0; i < eventSetList.size(); i++) {
            EventSet eventSet = eventSetList.get(i);
            addEvent(model, providedCHO, eventSet.getEvent());
        }
    }

    private void addEvent(Model model, Resource providedCHO, Event event) {
        eventComplexTypeProcessing.addEvent(model, providedCHO, event);
    }
}
