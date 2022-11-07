package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.commons.PropertyUtils;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.RDFConceptService;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.culture.CultureProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventActor.EventActorProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventDate.EventDateProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventMaterialsTech.EventMaterialsTechProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventPlace.EventPlaceProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventType.EventTypeProcessing;
import ro.webdata.parser.xml.lido.core.leaf.displayDate.DisplayDate;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.parser.xml.lido.core.leaf.eventDate.EventDate;

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
                model, resourceEvent, event.getEventMaterialsTech()
        );

        providedCHO.addProperty(EDM.wasPresentAt, resourceEvent);

        addActors(resourceEvent, actorsList);
        addCulture(model, resourceEvent, cultureList);
        addEvents(resourceEvent, eventPlaceList);
        addMaterials(resourceEvent, eventMaterialsList);
        addTimePeriod(model, resourceEvent, eventDateResourceList, event.getEventDate());
    }

    private static void addActors(Resource resourceEvent, ArrayList<ArrayList<Resource>> actorsList) {
        for (ArrayList<Resource> actorList : actorsList) {
            for (Resource actor : actorList) {
                actor.addProperty(EDM.wasPresentAt, resourceEvent);
            }
        }
    }

    private static void addCulture(Model model, Resource resourceEvent, ArrayList<Literal> cultureList) {
        for (Literal culture : cultureList) {
            RDFConceptService.addConcept(model, resourceEvent, EDM.isRelatedTo, culture, null);
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

    private static void addTimePeriod(Model model, Resource resourceEvent, ArrayList<Resource> eventDateResourceList, EventDate eventDate) {
        for (Resource eventDateResource : eventDateResourceList) {
            addTimeSpan(resourceEvent, eventDateResource);
            addRawTimePeriod(model, resourceEvent, eventDate);
        }
    }

    private static void addTimeSpan(Resource resourceEvent, Resource eventDateResource) {
        resourceEvent.addProperty(EDM.occurredAt, eventDateResource);
    }

    private static void addRawTimePeriod(Model model, Resource resourceEvent, EventDate eventDate) {
        Property property = PropertyUtils.createSubProperty(model, SKOS.note, "occurredAt", false);

        for (DisplayDate displayDate : eventDate.getDisplayDate()) {
            String timePeriod = displayDate.getText();
            String label = displayDate.getLabel().getLabel();
            String lang = displayDate.getLang().getLang();

            Literal literal = model.createLiteral(timePeriod, lang);
            resourceEvent.addProperty(property, literal);

            if (label != null) {
                Literal literalLabel = model.createLiteral(label, lang);
                resourceEvent.addProperty(property, literalLabel);
            }
        }
    }
}
