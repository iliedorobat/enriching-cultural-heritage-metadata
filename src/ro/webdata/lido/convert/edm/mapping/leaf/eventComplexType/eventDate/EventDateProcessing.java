package ro.webdata.lido.convert.edm.mapping.leaf.eventComplexType.eventDate;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.convert.edm.common.constants.NSConstants;
import ro.webdata.lido.convert.edm.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.displayDate.DisplayDate;
import ro.webdata.lido.parser.core.leaf.eventDate.EventDate;

import java.util.ArrayList;

public class EventDateProcessing {
    /**
     * Generate an event date Resource
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param eventDate The <b>lido:eventDate</b> object
     * @return
     */
    public Resource generateEventDate(Model model, Resource providedCHO, EventDate eventDate) {
        ArrayList<DisplayDate> displayDateList = new ArrayList<>();
        //TODO: remove this check when the LIDO Parser bug is fixed
        if (eventDate != null)
            displayDateList = eventDate.getDisplayDate();
        ArrayList<String> langList = new ArrayList<>();

        Resource eventDateResource = model.createResource(
                providedCHO.getURI() + NSConstants.LINK_ID_EVENT + "/EventDate"
        );
        eventDateResource.addProperty(RDF.type, EDM.TimeSpan);

        for (int i = 0; i < displayDateList.size(); i++) {
            DisplayDate displayDate = displayDateList.get(i);
            String text = displayDate.getText();
            String lang = displayDate.getLang().getLang();
            Literal literal = model.createLiteral(text, lang);

            if (langList.indexOf(lang) == -1)
                eventDateResource.addProperty(SKOS.prefLabel, literal);
            else
                eventDateResource.addProperty(SKOS.altLabel, literal);

            langList.add(lang);
        }

        return eventDateResource;
    }
}
