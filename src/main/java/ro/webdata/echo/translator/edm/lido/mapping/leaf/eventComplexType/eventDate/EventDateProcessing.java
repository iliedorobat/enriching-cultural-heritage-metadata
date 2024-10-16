package ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventDate;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.normalization.timespan.ro.TimespanUtils;
import ro.webdata.normalization.timespan.ro.model.TimespanModel;
import ro.webdata.parser.xml.lido.core.leaf.displayDate.DisplayDate;
import ro.webdata.parser.xml.lido.core.leaf.eventDate.EventDate;

import java.util.ArrayList;
import java.util.TreeSet;

import static ro.webdata.echo.translator.commons.Env.LANG_MAIN;

public class EventDateProcessing {
    /**
     * Generate an event date Resource
     * @param model The RDF graph
     * @param eventDate The <b>lido:eventDate</b> object
     * @return
     */
    public static ArrayList<Resource> generateEventDate(Model model, EventDate eventDate) {
        ArrayList<DisplayDate> displayDateList = eventDate != null
                ? eventDate.getDisplayDate()
                : new ArrayList<>();
        ArrayList<Resource> eventDateResourceList = new ArrayList<>();
        Resource eventDateResource = null;

        for (DisplayDate displayDate : displayDateList) {
            String text = displayDate.getText();
            TreeSet<String> timespanSet = new TreeSet<>();

            if (LANG_MAIN.equals(Const.LANG_RO)) {
                TimespanModel timespanModel = TimespanUtils.prepareTimespanModel(text);
                timespanSet = timespanModel.getDBpediaUris();
            }

            for (String timespan : timespanSet) {
                eventDateResource = model
                        .createResource(timespan)
                        .addProperty(RDF.type, EDM.TimeSpan);
                eventDateResourceList.add(eventDateResource);
            }
        }

        return eventDateResourceList;
    }
}
