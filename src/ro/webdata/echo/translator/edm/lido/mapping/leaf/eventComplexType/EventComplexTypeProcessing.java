package ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.commons.PropertyUtils;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.culture.CultureProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventActor.EventActorProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventDate.EventDateProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventMaterialsTech.EventMaterialsTechProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventPlace.EventPlaceProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventType.EventTypeProcessing;
import ro.webdata.parser.xml.lido.core.leaf.actorInRole.ActorInRole;
import ro.webdata.parser.xml.lido.core.leaf.culture.Culture;
import ro.webdata.parser.xml.lido.core.leaf.displayDate.DisplayDate;
import ro.webdata.parser.xml.lido.core.leaf.event.Event;
import ro.webdata.parser.xml.lido.core.leaf.eventActor.EventActor;
import ro.webdata.parser.xml.lido.core.leaf.eventDate.EventDate;
import ro.webdata.parser.xml.lido.core.leaf.eventMaterialsTech.EventMaterialsTech;
import ro.webdata.parser.xml.lido.core.leaf.eventPlace.EventPlace;
import ro.webdata.parser.xml.lido.core.leaf.periodName.PeriodName;
import ro.webdata.parser.xml.lido.core.leaf.roleActor.RoleActor;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventComplexTypeProcessing {
    // object-centric support
    public static void addDate(Model model, Resource providedCHO, Event event) {
        List<String> eventTypeList = EventTypeProcessing
                .getEventTypeList(event.getEventType())
                .stream().map(String::toLowerCase)
                .collect(Collectors.toList());

        ObjectCentric.addActors(model, providedCHO, event.getEventActor());
        Utils.addCulture(model, providedCHO, event.getCulture());
        Utils.addEventPlaces(model, providedCHO, EDM.hasMet, event.getEventPlace());
        ObjectCentric.addEventMaterialsTechList(model,providedCHO, eventTypeList, event.getEventMaterialsTech());
        Utils.addPeriodName(model, providedCHO, DCTerms.temporal, event.getPeriodName());
        ObjectCentric.addTimePeriod(model, providedCHO, event.getEventActor(), event.getEventDate(), eventTypeList);
    }

    // event-centric support
    public static void addEvent(Model model, Resource providedCHO, Event event) {
        Resource resourceEvent = EventTypeProcessing.generateEventList(
                model, providedCHO, event.getEventType()
        );
        Property medium = PropertyUtils.createSubProperty(model, SKOS.note, "medium", false);
        Property occurredAt = PropertyUtils.createSubProperty(model, SKOS.note, "occuredAt", false);
        Property temporal = PropertyUtils.createSubProperty(model, SKOS.note, "temporal", false);

        providedCHO.addProperty(EDM.wasPresentAt, resourceEvent);

        EventCentric.addActors(model, resourceEvent, event.getEventActor());
        Utils.addCulture(model, resourceEvent, event.getCulture());
        Utils.addEventPlaces(model, resourceEvent, EDM.happenedAt, event.getEventPlace());
        EventMaterialsTechProcessing.addEventMaterialsTechList(
                model, resourceEvent, medium, event.getEventMaterialsTech()
        );
        Utils.addPeriodName(model, resourceEvent, temporal, event.getPeriodName());
        Utils.addTimePeriod(model, resourceEvent, EDM.occurredAt, occurredAt, event.getEventDate());
    }
}

class EventCentric {
    static void addActors(Model model, Resource resourceEvent, ArrayList<EventActor> eventActorsList) {
        ArrayList<Resource> actorsList = EventActorProcessing.generateActorsList(
                model, eventActorsList
        );

        for (Resource actor : actorsList) {
            actor.addProperty(EDM.wasPresentAt, resourceEvent);
        }
    }
}

class ObjectCentric {
    static void addActors(Model model, Resource providedCHO, ArrayList<EventActor> eventActorsList) {
        ArrayList<Resource> actorsList = EventActorProcessing.generateActorsList(
                model, eventActorsList
        );

        for (Resource actor : actorsList) {
            providedCHO.addProperty(EDM.hasMet, actor);
        }
    }

    static void addEventMaterialsTechList(Model model, Resource providedCHO, List<String> eventTypeList, ArrayList<EventMaterialsTech> materialsTechList) {
        if (eventTypeList.contains("production")) {
            EventMaterialsTechProcessing.addEventMaterialsTechList(
                    model, providedCHO, DCTerms.medium, materialsTechList
            );
        }
    }

    static void addTimePeriod(Model model, Resource providedCHO, ArrayList<EventActor> eventActorsList, EventDate eventDate, List<String> eventTypeList) {
        if (eventTypeList.contains("production")) {
            if (Utils.isIssuer(eventActorsList)) {
                Utils.addTimePeriod(model, providedCHO, DCTerms.created, DCTerms.issued, eventDate);
            } else {
                Utils.addTimePeriod(model, providedCHO, DCTerms.created, DCTerms.created, eventDate);
            }
        } else if (eventTypeList.contains("finding")) {
            Property found = PropertyUtils.createSubProperty(model, DC_11.date, "found", false);
            Utils.addTimePeriod(model, providedCHO, found, found, eventDate);
        }
    }
}

class Utils {
    // resource = providedCHO or resourceEvent
    static void addTimePeriod(Model model, Resource resource, Property timeSpanProperty, Property rawTimePeriodProperty, EventDate eventDate) {
        ArrayList<Resource> eventDateResourceList = EventDateProcessing.generateEventDate(
                model, eventDate
        );

        for (Resource eventDateResource : eventDateResourceList) {
            Utils.addTimeSpan(resource, timeSpanProperty, eventDateResource);
            Utils.addRawTimePeriod(model, resource, rawTimePeriodProperty, eventDate);
        }
    }

    static void addCulture(Model model, Resource resourceEvent, ArrayList<Culture> cultureList) {
        ArrayList<Literal> cultureLiteralList = CultureProcessing.getCultureList(
                model, cultureList
        );
        for (Literal culture : cultureLiteralList) {
            RDFConceptService.addConcept(model, resourceEvent, EDM.isRelatedTo, culture, null);
        }
    }

    // resource = providedCHO or resourceEvent
    static void addEventPlaces(Model model, Resource resource, Property property, ArrayList<EventPlace> placeList) {
        ArrayList<Resource> eventPlaceList = EventPlaceProcessing.generateEventPlaceList(
                model, placeList
        );

        for (Resource eventPlace : eventPlaceList) {
            resource.addProperty(property, eventPlace);
        }
    }

    // resource = providedCHO or resourceEvent
    static void addPeriodName(Model model, Resource providedCHO, Property property, ArrayList<PeriodName> periodNameList) {
        for (PeriodName periodName : periodNameList) {
            for (Term term : periodName.getTerm()) {
                providedCHO.addProperty(property, term.getText());
            }
        }
    }

    // resource = providedCHO or resourceEvent
    private static void addRawTimePeriod(Model model, Resource resource, Property property, EventDate eventDate) {
        for (DisplayDate displayDate : eventDate.getDisplayDate()) {
            String timePeriod = displayDate.getText();
            String label = displayDate.getLabel().getLabel();
            String lang = displayDate.getLang().getLang();

            Literal literal = model.createLiteral(timePeriod, lang);
            resource.addProperty(property, literal);

            if (label != null) {
                Literal literalLabel = model.createLiteral(label, lang);
                resource.addProperty(property, literalLabel);
            }
        }
    }

    // resource = providedCHO or resourceEvent
    private static void addTimeSpan(Resource resource, Property property, Resource eventDateResource) {
        resource.addProperty(property, eventDateResource);
    }

    static boolean isIssuer(ArrayList<EventActor> eventActorsList) {
        for (EventActor eventActor : eventActorsList) {
            ActorInRole actorInRole = eventActor.getActorInRole();

            for (RoleActor roleActor : actorInRole.getRoleActor()) {
                for (Term term : roleActor.getTerm()) {
                    String lcTerm = term.getText().toLowerCase();
                    String preparedTerm = StringUtils.stripAccents(lcTerm);

                    if (preparedTerm.equals("issuer") || preparedTerm.equals("emitent")) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
