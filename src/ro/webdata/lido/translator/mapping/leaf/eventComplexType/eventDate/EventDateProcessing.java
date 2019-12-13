package ro.webdata.lido.translator.mapping.leaf.eventComplexType.eventDate;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.lido.translator.processing.timespan.ro.TimespanUtils;
import ro.webdata.lido.translator.vocabulary.EDM;
import ro.webdata.parser.xml.lido.core.leaf.displayDate.DisplayDate;
import ro.webdata.parser.xml.lido.core.leaf.eventDate.EventDate;

import java.util.ArrayList;
import java.util.TreeSet;

public class EventDateProcessing {
    /**
     * Generate an event date Resource
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param eventDate The <b>lido:eventDate</b> object
     * @return
     */
    public ArrayList<Resource> generateEventDate(Model model, Resource providedCHO, EventDate eventDate) {
        ArrayList<DisplayDate> displayDateList = new ArrayList<>();
        ArrayList<Resource> eventDateResourceList = new ArrayList<>();
        Resource eventDateResource = null;

        //TODO: remove this check when the LIDO Parser bug is fixed
        if (eventDate != null)
            displayDateList = eventDate.getDisplayDate();

        for (int i = 0; i < displayDateList.size(); i++) {
            DisplayDate displayDate = displayDateList.get(i);
            String text = displayDate.getText();
            TreeSet<String> timespanSet = TimespanUtils.getTimespanSet(text);

            for (String timespan : timespanSet) {
                eventDateResource = model.createResource(timespan);
                eventDateResource.addProperty(RDF.type, EDM.TimeSpan);
                eventDateResourceList.add(eventDateResource);
            }
        }

        return eventDateResourceList;
    }
}
