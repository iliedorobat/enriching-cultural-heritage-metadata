package ro.webdata.lido.convert.edm.processing.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.lido.convert.edm.common.PropertyUtils;
import ro.webdata.lido.parser.core.leaf.workID.WorkID;

import java.util.ArrayList;

public class WorkIDProcessing {
    /**
     * Add subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param workIDList The related WorkID objects list
     */
    public void addWorkIDList(Model model, Resource providedCHO, ArrayList<WorkID> workIDList) {
        WorkID workID;
        String text, type;

        for (int i = 0; i < workIDList.size(); i++) {
            workID = workIDList.get(i);
            text = workID.getText();
            type = workID.getType().getType();

            if (text != null) {
                Literal textLiteral = model.createLiteral(text);
                Property property = PropertyUtils.createSubProperty(model, type, DC_11.identifier);

                if (!type.equals("inventory number"))
                    property = PropertyUtils.createSubProperty(model, type, DC_11.description);

                providedCHO.addProperty(property, textLiteral);
            }
        }
    }
}
