package ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventType;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;
import ro.webdata.parser.xml.lido.core.leaf.eventType.EventType;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_CHO;
import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_EVENT;

public class EventTypeProcessing {
    public static List<String> getEventTypeList(EventType eventType) {
        return eventType
                .getTerm()
                .stream()
                .map(term -> Text.sanitizeString(term.getText(), false))
                .collect(Collectors.toList());
    }

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
        return generateEvent(model, providedCHO, termList);
    }

    /**
     * Generate an Event resource for a specified language
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param termList The list of terms
     * @return
     */
    private static Resource generateEvent(Model model, Resource providedCHO, ArrayList<Term> termList) {
        Resource resource = null;
        String choUri = providedCHO.getURI();
        String choMainUri = NS_REPO_RESOURCE_CHO;
        int startIndex = choMainUri.length();

        for (Term term : termList) {
            String eventType = Text.sanitizeString(term.getText(), false);
            String uri = URIUtils.prepareUri(NS_REPO_RESOURCE_EVENT, eventType + Namespace.URL_SEPARATOR + choUri.substring(startIndex));

            resource = model.getResource(uri)
                    .addProperty(RDF.type, EDM.Event)
                    .addProperty(EDM.hasType, eventType, term.getLang().getLang());
        }

        return resource;
    }
}
