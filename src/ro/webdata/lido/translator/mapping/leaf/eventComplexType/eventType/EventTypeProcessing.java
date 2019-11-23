package ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventType;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.lido.translator.common.TextUtils;
import ro.webdata.lido.translator.common.constants.Constants;
import ro.webdata.lido.translator.common.constants.FileConstants;
import ro.webdata.lido.translator.common.constants.NSConstants;
import ro.webdata.lido.translator.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.eventType.EventType;
import ro.webdata.lido.parser.core.leaf.term.Term;

import java.util.ArrayList;

public class EventTypeProcessing {
    /**
     * Generate an event resource
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param eventType The EventType
     * @return Resource which represents the generated event
     */
    //TODO: owlSameAs for different languages events
    public Resource generateEventList(Model model, Resource providedCHO, EventType eventType) {
        ArrayList<Term> termList = eventType.getTerm();

        Resource resource = generateEvent(model, providedCHO, termList, Constants.LANG_EN);

        if (resource == null) {
            resource = generateEvent(model, providedCHO, termList, Constants.LANG_MAIN);
        }

        if (resource == null) {
            resource = generateEvent(model, providedCHO, termList, null);
        }

        return resource;
    }

    /**
     * Generate an Event resource for a specified language
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param termList The list of terms
     * @param eventLang The searched event language
     * @return
     */
    private Resource generateEvent(Model model, Resource providedCHO, ArrayList<Term> termList, String eventLang) {
        for (int i = 0; i < termList.size(); i++) {
            Term term = termList.get(i);
            String termLang = term.getLang().getLang();

            String choMainUri = NSConstants.NS_REPO_RESOURCE_CHO;
            String choUri = providedCHO.getURI();
            int index = choMainUri.length() + 1;

            //TODO: subClass of Constants.NS_REPO_RESOURCE_EVENT + Constants.FILE_SEPARATOR + TextUtils.sanitizeString(eventName)
            if (termLang.equals(eventLang) || eventLang == null) {
                String eventType = TextUtils.sanitizeString(term.getText());
                Resource resource = model.createResource(
                        NSConstants.NS_REPO_RESOURCE_EVENT
                        + FileConstants.FILE_SEPARATOR + eventType
                        + FileConstants.FILE_SEPARATOR + choUri.substring(index)
                );
                resource.addProperty(RDF.type, EDM.Event);
                //TODO: create a concept for the event type
                resource.addProperty(EDM.hasType, eventType);
                return resource;
            }
        }

        return null;
    }
}
