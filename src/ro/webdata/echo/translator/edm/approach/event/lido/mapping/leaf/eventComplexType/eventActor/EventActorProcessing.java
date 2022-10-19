package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventActor;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.lido.core.leaf.actor.Actor;
import ro.webdata.parser.xml.lido.core.leaf.actorID.ActorID;
import ro.webdata.parser.xml.lido.core.leaf.actorInRole.ActorInRole;
import ro.webdata.parser.xml.lido.core.leaf.appellationValue.AppellationValue;
import ro.webdata.parser.xml.lido.core.leaf.eventActor.EventActor;
import ro.webdata.parser.xml.lido.core.leaf.roleActor.RoleActor;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;
import ro.webdata.parser.xml.lido.core.set.nameActorSet.NameActorSet;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.URIUtils;

import java.util.ArrayList;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_AGENT;
import static ro.webdata.echo.translator.commons.Env.LANG_MAIN;

public class EventActorProcessing {
    /**
     * Generate a list which include the actor name list for every actor
     * @param model The RDF graph
     * @param eventActorList The list with actors
     * @return
     */
    public static ArrayList<ArrayList<Resource>> generateActorsList(Model model, ArrayList<EventActor> eventActorList) {
        ArrayList<ArrayList<Resource>> actorsList = new ArrayList<>();

        for (EventActor eventActor : eventActorList) {
            ActorInRole actorInRole = eventActor.getActorInRole();

            ArrayList<Resource> actorList = generateActorNameList(
                    model, actorInRole.getActor(), actorInRole.getRoleActor()
            );

            actorsList.add(actorList);
        }

        return actorsList;
    }

    /**
     * Generate a list which contains every name for an Actor
     * @param model The RDF graph
     * @param actor The involved actor
     * @param roleActorList The actor roles
     * @return
     */
    private static ArrayList<Resource> generateActorNameList(Model model, Actor actor, ArrayList<RoleActor> roleActorList) {
        ArrayList<NameActorSet> nameActorSetList = actor.getNameActorSet();
        ArrayList<Resource> actorNameList = new ArrayList<>();

        for (NameActorSet actorSet : nameActorSetList) {
            // TODO: check if the actorID exists in "lido:actor" class
            //  if exists, check if there is any other actor with the same actorID
            ArrayList<ActorID> actorID = actor.getActorID();
            // TODO: same as for all kind of resources ([agent], aggregation, cho, event, license, organization) !!!

            ArrayList<AppellationValue> appellationValueList = actorSet.getAppellationValue();

            for (AppellationValue appellationValue : appellationValueList) {
                Resource resource = generateActorName(model, appellationValue, roleActorList);

                if (resource != null) {
                    actorNameList.add(resource);
                }
            }
        }
        addSameAsProperty(actorNameList);

        return actorNameList;
    }

    /**
     * Create an "edm:Agent" resource for every actor
     * @param model The RDF graph
     * @param appellationValue
     * @param roleActorList The actor roles
     * @return
     */
    private static Resource generateActorName(Model model, AppellationValue appellationValue, ArrayList<RoleActor> roleActorList) {
        String actorName = appellationValue.getText();

        if (actorName != null) {
            String role = getActorRole(roleActorList);
            Literal literal = model.createLiteral(actorName);
            String uri = URIUtils.prepareUri(NS_REPO_RESOURCE_AGENT, Text.sanitizeString(actorName));

            Resource actor = model
                    .createResource(uri)
                    .addProperty(RDF.type, EDM.Agent)
                    .addProperty(FOAF.name, literal);
            addPrefLabelProperty(model, actor, roleActorList);

            return actor;
        }

        return null;
    }

    /**
     * Get the first role for an actor
     * @param roleActorList The actor roles
     * @return The actor role
     */
    private static String getActorRole(ArrayList<RoleActor> roleActorList) {
        String role = getActorRole(roleActorList, Const.LANG_EN);

        if (role == null) {
            role = getActorRole(roleActorList, LANG_MAIN);
        }

        if (role == null) {
            role = getActorRole(roleActorList, null);
        }

        return role;
    }

    /**
     * Get the first role for an actor in the specified language
     * @param roleActorList The actor roles
     * @param language The searching language
     */
    private static String getActorRole(ArrayList<RoleActor> roleActorList, String language) {
        for (RoleActor roleActor : roleActorList) {
            ArrayList<Term> termList = roleActor.getTerm();

            for (Term term : termList) {
                String lang = term.getLang().getLang();
                String text = term.getText();

                if (lang.equals(language) || language == null) {
                    return text;
                }
            }
        }

        return null;
    }

    /**
     * Add the actor roles (<b>skos:prefLabel</b> for every actor role)
     * @param model The RDF graph
     * @param actor The actor
     * @param roleActorList The actor roles
     */
    private static void addPrefLabelProperty(Model model, Resource actor, ArrayList<RoleActor> roleActorList) {
        for (RoleActor roleActor : roleActorList) {
            ArrayList<Term> termList = roleActor.getTerm();

            for (Term term : termList) {
                String lang = term.getLang().getLang();
                String text = term.getText();

                if (text != null) {
                    Literal literal = model.createLiteral(text, lang);
                    actor.addProperty(SKOS.prefLabel, literal);
                }
            }
        }
    }

    /**
     * Add the <b>owl:sameAs</b> property for all the Actor resources that belong to one person
     * (all the names that refers to the same person)
     * @param actorNameList The name list of one person
     */
    private static void addSameAsProperty(ArrayList<Resource> actorNameList) {
        for (int i = 0; i < actorNameList.size(); i++) {
            Resource actorNameI = actorNameList.get(i);

            for (int j = 0; j < actorNameList.size() && j != i; j++) {
                Resource actorNameJ = actorNameList.get(j);
                actorNameI.addProperty(OWL2.sameAs, actorNameJ);
            }
        }
    }
}
