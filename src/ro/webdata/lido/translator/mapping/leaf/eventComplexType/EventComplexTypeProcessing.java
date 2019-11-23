package ro.webdata.lido.translator.mapping.leaf.eventComplexType;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventDate.EventDateProcessing;
import ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventPlace.EventPlaceProcessing;
import ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventType.EventTypeProcessing;
import ro.webdata.lido.translator.mapping.leaf.eventComplexType.culture.CultureProcessing;
import ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventActor.EventActorProcessing;
import ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventMaterialsTech.EventMaterialsTechProcessing;
import ro.webdata.lido.translator.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.event.Event;

import java.util.ArrayList;

public class EventComplexTypeProcessing {
    private static CultureProcessing cultureProcessing = new CultureProcessing();
    private static EventActorProcessing eventActorProcessing = new EventActorProcessing();
    private static EventDateProcessing eventDateProcessing = new EventDateProcessing();
    private static EventMaterialsTechProcessing eventMaterialsTechProcessing = new EventMaterialsTechProcessing();
    private static EventPlaceProcessing eventPlaceProcessing = new EventPlaceProcessing();
    private static EventTypeProcessing eventTypeProcessing = new EventTypeProcessing();

    public void addEvent(Model model, Resource providedCHO, Event event) {
        ArrayList<Literal> cultureList = cultureProcessing.getCultureList(
                model, providedCHO, event.getCulture());
        Resource resourceEvent = eventTypeProcessing.generateEventList(
                model, providedCHO, event.getEventType());
        ArrayList<ArrayList<Resource>> actorsList = eventActorProcessing.generateActorsList(
                model, event.getEventActor());
        ArrayList<Resource> eventDateResourceList = eventDateProcessing.generateEventDate(
                model, providedCHO, event.getEventDate());
        ArrayList<Resource> eventPlaceList = eventPlaceProcessing.generateEventPlaceList(
                model, providedCHO, event.getEventPlace());
        ArrayList<Resource> eventMaterialsList = eventMaterialsTechProcessing.addEventMaterialsTechList(
                model, providedCHO, event.getEventMaterialsTech());

//        PropertyUtils.createSubProperty(model, "timePeriod", SKOS.note);
        providedCHO.addProperty(EDM.wasPresentAt, resourceEvent);

        addActors(resourceEvent, actorsList);
        addCulture(resourceEvent, cultureList);
        addEvents(resourceEvent, eventPlaceList);
        addMaterials(resourceEvent, eventMaterialsList);
        //TODO: add a new property "edm:timePeriod" (extending "skos:note") for storing
        // the original data related to "edm:occuredAt"
        addTimespan(resourceEvent, eventDateResourceList);
    }

    private void addActors(Resource resourceEvent, ArrayList<ArrayList<Resource>> actorsList) {
        for (int i = 0; i < actorsList.size(); i++) {
            ArrayList<Resource> actorList = actorsList.get(i);
            for (int j = 0; j < actorList.size(); j++) {
                Resource actor = actorList.get(j);
                actor.addProperty(EDM.wasPresentAt, resourceEvent);
            }
        }
    }

    private void addCulture(Resource resourceEvent, ArrayList<Literal> cultureList) {
        for (int i = 0; i < cultureList.size(); i++) {
            Literal culture = cultureList.get(i);
            //TODO: create a concept for culture
            resourceEvent.addProperty(EDM.isRelatedTo, culture);
        }
    }

    private void addEvents(Resource resourceEvent, ArrayList<Resource> eventPlaceList) {
        for (int i = 0; i < eventPlaceList.size(); i++) {
            Resource eventPlace = eventPlaceList.get(i);
            resourceEvent.addProperty(EDM.happenedAt, eventPlace);
        }
    }

    //TODO: material => dcterms:medium
    private void addMaterials(Resource resourceEvent, ArrayList<Resource> eventMaterialsList) {
        for (int i = 0; i < eventMaterialsList.size(); i++) {
            Resource eventMaterial = eventMaterialsList.get(i);
            resourceEvent.addProperty(EDM.isRelatedTo, eventMaterial);
        }
    }

    private void addTimespan(Resource resourceEvent, ArrayList<Resource> eventDateResourceList) {
        for (int i = 0; i < eventDateResourceList.size(); i++) {
            Resource eventDateResource = eventDateResourceList.get(i);
            resourceEvent.addProperty(EDM.occurredAt, eventDateResource);
        }
    }
}
