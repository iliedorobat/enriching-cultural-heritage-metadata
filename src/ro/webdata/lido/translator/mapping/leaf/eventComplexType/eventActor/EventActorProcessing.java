package ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventActor;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.translator.common.TextUtils;
import ro.webdata.lido.translator.common.constants.Constants;
import ro.webdata.lido.translator.common.constants.FileConstants;
import ro.webdata.lido.translator.common.constants.NSConstants;
import ro.webdata.lido.translator.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.actor.Actor;
import ro.webdata.lido.parser.core.leaf.actorID.ActorID;
import ro.webdata.lido.parser.core.leaf.actorInRole.ActorInRole;
import ro.webdata.lido.parser.core.leaf.appellationValue.AppellationValue;
import ro.webdata.lido.parser.core.leaf.eventActor.EventActor;
import ro.webdata.lido.parser.core.leaf.roleActor.RoleActor;
import ro.webdata.lido.parser.core.leaf.term.Term;
import ro.webdata.lido.parser.core.set.nameActorSet.NameActorSet;

import java.util.ArrayList;

public class EventActorProcessing {
    /**
     * Generate a list which include the actor name list for every actor
     * @param model The RDF graph
     * @param eventActorList The list with actors
     * @return
     */
    public ArrayList<ArrayList<Resource>> generateActorsList(Model model, ArrayList<EventActor> eventActorList) {
        ArrayList<ArrayList<Resource>> actorsList = new ArrayList<>();

        for (int i = 0; i < eventActorList.size(); i++) {
            EventActor eventActor = eventActorList.get(i);
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
    private ArrayList<Resource> generateActorNameList(Model model, Actor actor, ArrayList<RoleActor> roleActorList) {
        ArrayList<NameActorSet> nameActorSetList = actor.getNameActorSet();
        ArrayList<Resource> actorNameList = new ArrayList<>();

        for (int i = 0; i < nameActorSetList.size(); i++) {
            //TODO: check if in "lido:actor" class exists the actorID resource and
            // if exists, check if there is any other actor with the same actorID
            ArrayList<ActorID> actorID = actor.getActorID();
            //TODO: same as for all kind of resources ([agent], aggregation, cho, event, license, organization) !!!

            NameActorSet nameActorSet = nameActorSetList.get(i);
            ArrayList<AppellationValue> appellationValueList = nameActorSet.getAppellationValue();

            for (int j = 0; j < appellationValueList.size(); j++) {
                Resource resource = generateActorName(model, appellationValueList.get(j), roleActorList);
                if (resource != null)
                    actorNameList.add(resource);
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
    private Resource generateActorName(Model model, AppellationValue appellationValue, ArrayList<RoleActor> roleActorList) {
        String actorName = appellationValue.getText();

        if (actorName != null) {
            String role = getActorRole(roleActorList);
            Literal literal = model.createLiteral(actorName);

            Resource actor = model.createResource(
                    NSConstants.NS_REPO_RESOURCE_AGENT
                    + FileConstants.FILE_SEPARATOR + role
                    + FileConstants.FILE_SEPARATOR + TextUtils.sanitizeString(actorName)
            );
            actor.addProperty(RDF.type, EDM.Agent);
            actor.addProperty(FOAF.name, literal);
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
    private String getActorRole(ArrayList<RoleActor> roleActorList) {
        String role = getActorRole(roleActorList, Constants.LANG_EN);

        if (role == null)
            role = getActorRole(roleActorList, Constants.LANG_MAIN);

        if (role == null)
            role = getActorRole(roleActorList, null);

        return role;
    }

    /**
     * Get the first role for an actor in the specified language
     * @param roleActorList The actor roles
     * @param language The searching language
     * @param roleActorList The actor roles
     */
    private String getActorRole(ArrayList<RoleActor> roleActorList, String language) {
        for (int i = 0; i < roleActorList.size(); i++) {
            RoleActor roleActor = roleActorList.get(i);
            ArrayList<Term> termList = roleActor.getTerm();

            for (int j = 0; j < termList.size(); j++) {
                Term term = termList.get(j);
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
    private void addPrefLabelProperty(Model model, Resource actor, ArrayList<RoleActor> roleActorList) {
        for (int i = 0; i < roleActorList.size(); i++) {
            RoleActor roleActor = roleActorList.get(i);
            ArrayList<Term> termList = roleActor.getTerm();

            for (int j = 0; j < termList.size(); j++) {
                Term term = termList.get(j);
                String lang = term.getLang().getLang();
                String text = term.getText();
                Literal literal = model.createLiteral(text, lang);
                actor.addProperty(SKOS.prefLabel, literal);
            }
        }
    }

    /**
     * Add the <b>owl:sameAs</b> property for all the Actor resources that belong to one person
     * (all the names that refers to the same person)
     * @param actorNameList The name list of one person
     */
    private void addSameAsProperty(ArrayList<Resource> actorNameList) {
        for (int i = 0; i < actorNameList.size(); i++) {
            Resource actorNameI = actorNameList.get(i);
            for (int j = 0; j < actorNameList.size() && j != i; j++) {
                Resource actorNameJ = actorNameList.get(j);
                actorNameI.addProperty(OWL2.sameAs, actorNameJ);
            }
        }
    }
}
