package ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.culture.CultureProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventActor.EventActorProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventDate.EventDateProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventMaterialsTech.EventMaterialsTechProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventPlace.EventPlaceProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventType.EventTypeProcessing;

import java.util.ArrayList;

public class EventComplexTypeProcessing {
    public static void addEvent(Model model, Resource providedCHO, Event event) {
        ArrayList<Literal> cultureList = CultureProcessing.getCultureList(
                model, providedCHO, event.getCulture());
        Resource resourceEvent = EventTypeProcessing.generateEventList(
                model, providedCHO, event.getEventType()
        );
        ArrayList<ArrayList<Resource>> actorsList = EventActorProcessing.generateActorsList(
                model, event.getEventActor()
        );
        ArrayList<Resource> eventDateResourceList = EventDateProcessing.generateEventDate(
                model, providedCHO, event.getEventDate()
        );
        ArrayList<Resource> eventPlaceList = EventPlaceProcessing.generateEventPlaceList(
                model, providedCHO, event.getEventPlace()
        );
        ArrayList<Resource> eventMaterialsList = EventMaterialsTechProcessing.addEventMaterialsTechList(
                model, providedCHO, event.getEventMaterialsTech()
        );

//        PropertyUtils.createSubProperty(model, "timePeriod", SKOS.note);
        providedCHO.addProperty(EDM.wasPresentAt, resourceEvent);

        addActors(resourceEvent, actorsList);
        addCulture(resourceEvent, cultureList);
        addEvents(resourceEvent, eventPlaceList);
        addMaterials(resourceEvent, eventMaterialsList);
        // TODO: add a new property "edm:timePeriod" (extending "skos:note") for storing
        //  the original data related to "edm:occuredAt"
        addTimespan(resourceEvent, eventDateResourceList);
    }

    private static void addActors(Resource resourceEvent, ArrayList<ArrayList<Resource>> actorsList) {
        for (ArrayList<Resource> actorList : actorsList) {
            for (Resource actor : actorList) {
                actor.addProperty(EDM.wasPresentAt, resourceEvent);
            }
        }
    }

    private static void addCulture(Resource resourceEvent, ArrayList<Literal> cultureList) {
        for (Literal culture : cultureList) {
            // TODO: create a concept for culture
            resourceEvent.addProperty(EDM.isRelatedTo, culture);
        }
    }

    private static void addEvents(Resource resourceEvent, ArrayList<Resource> eventPlaceList) {
        for (Resource eventPlace : eventPlaceList) {
            resourceEvent.addProperty(EDM.happenedAt, eventPlace);
        }
    }

    // TODO: material => dcterms:medium
    private static void addMaterials(Resource resourceEvent, ArrayList<Resource> eventMaterialsList) {
        for (Resource eventMaterial : eventMaterialsList) {
            resourceEvent.addProperty(EDM.isRelatedTo, eventMaterial);
        }
    }

    private static void addTimespan(Resource resourceEvent, ArrayList<Resource> eventDateResourceList) {
        for (Resource eventDateResource : eventDateResourceList) {
            resourceEvent.addProperty(EDM.occurredAt, eventDateResource);
        }
    }
}
