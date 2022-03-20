package ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventType;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.lido.core.leaf.eventType.EventType;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;

import java.util.ArrayList;

import static ro.webdata.translator.commons.EnvConstants.*;

public class EventTypeProcessing {
    /**
     * Generate an event resource
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param eventType The EventType
     * @return Resource which represents the generated event
     */
    // TODO: owlSameAs for different languages events
    public static Resource generateEventList(Model model, Resource providedCHO, EventType eventType) {
        ArrayList<Term> termList = eventType.getTerm();

        Resource resource = generateEvent(model, providedCHO, termList, Const.LANG_EN);

        if (resource == null) {
            resource = generateEvent(model, providedCHO, termList, LANG_MAIN);
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
    private static Resource generateEvent(Model model, Resource providedCHO, ArrayList<Term> termList, String eventLang) {
        for (Term term : termList) {
            String termLang = term.getLang().getLang();
            String choMainUri = NS_REPO_RESOURCE_CHO;
            String choUri = providedCHO.getURI();
            int index = choMainUri.length() + 1;

            // TODO: subClass of Constants.NS_REPO_RESOURCE_EVENT + Constants.FILE_SEPARATOR + TextUtils.sanitizeString(eventName)
            if (termLang.equals(eventLang) || eventLang == null) {
                String eventType = Text.sanitizeString(term.getText());
                Resource resource = model.createResource(
                        NS_REPO_RESOURCE_EVENT + eventType + File.FILE_SEPARATOR + choUri.substring(index)
                );
                resource.addProperty(RDF.type, EDM.Event);
                // TODO: create a concept for event type
                resource.addProperty(EDM.hasType, eventType);
                return resource;
            }
        }

        return null;
    }
}
