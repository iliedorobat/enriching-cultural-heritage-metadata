package ro.webdata.echo.translator.edm.lido.mapping.leaf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.translator.commons.PropertyUtils;
import ro.webdata.parser.xml.lido.core.leaf.workID.WorkID;

import java.util.ArrayList;

public class WorkIDProcessing {
    /**
     * Add subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param workIDList The related WorkID objects list
     */
    public static void addWorkIDList(Model model, Resource providedCHO, ArrayList<WorkID> workIDList) {
        String text, type;

        for (WorkID workID : workIDList) {
            text = workID.getText();
            type = workID.getType().getType();

            if (text != null) {
                Property property = type.equals("inventory number")
                        ? PropertyUtils.createSubProperty(model, DC_11.identifier, type, true)
                        : PropertyUtils.createSubProperty(model, DC_11.description, type, true);
                providedCHO.addProperty(property, text);
            }
        }
    }
}
