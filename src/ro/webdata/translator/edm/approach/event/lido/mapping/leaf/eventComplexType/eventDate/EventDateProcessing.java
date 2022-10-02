package ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventDate;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.normalization.timespan.ro.TimespanUtils;
import ro.webdata.parser.xml.lido.core.leaf.displayDate.DisplayDate;
import ro.webdata.parser.xml.lido.core.leaf.eventDate.EventDate;
import ro.webdata.translator.commons.FileConst;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.TreeSet;

import static ro.webdata.translator.commons.Env.LANG_MAIN;

public class EventDateProcessing {
    /**
     * Generate an event date Resource
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param eventDate The <b>lido:eventDate</b> object
     * @return
     */
    public static ArrayList<Resource> generateEventDate(Model model, Resource providedCHO, EventDate eventDate) {
        ArrayList<DisplayDate> displayDateList = new ArrayList<>();
        ArrayList<Resource> eventDateResourceList = new ArrayList<>();
        Resource eventDateResource = null;

        // TODO: remove the validation when the LIDO Parser bug is fixed
        if (eventDate != null)
            displayDateList = eventDate.getDisplayDate();

        for (DisplayDate displayDate : displayDateList) {
            String text = displayDate.getText();
            TreeSet<String> timespanSet = new TreeSet<>();

            if (LANG_MAIN.equals(Const.LANG_RO)) {
                timespanSet = TimespanUtils.getTimespanSet(text);
            }

            // TODO: remove
            StringWriter sw = new StringWriter()
                    .append(text)
                    .append("|")
                    .append(timespanSet.toString())
                    .append("\n");
            File.write(sw, FileConst.PATH_OUTPUT_TIMESPAN_ANALYSIS_FILE, true);

            for (String timespan : timespanSet) {
                eventDateResource = model
                        .createResource(timespan)
                        .addProperty(RDF.type, EDM.TimeSpan);
                eventDateResourceList.add(eventDateResource);
            }

            // TODO: add the original value
        }

        return eventDateResourceList;
    }
}
