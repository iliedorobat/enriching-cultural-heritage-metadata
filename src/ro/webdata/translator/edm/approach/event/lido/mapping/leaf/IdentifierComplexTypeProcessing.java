package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.translator.edm.approach.event.lido.common.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.common.constants.LIDOConstants;
import ro.webdata.parser.xml.lido.core.complex.identifierComplexType.IdentifierComplexType;

import java.util.ArrayList;

public class IdentifierComplexTypeProcessing {
    /**
     * Add an <b>dc:identifier</b> property to a resource
     * @param model The RDF graph
     * @param resource A RDF resource
     * @param identifierComplexType <b>IdentifierComplexType</b>
     */
    public void addIdentifier(Model model, Resource resource, IdentifierComplexType identifierComplexType) {
        String text = identifierComplexType.getText();
        String type = identifierComplexType.getType().getType();

        if (text != null) {
            // Only the entries from romanian dataset could have a CIMEC id (link)
            if (Constants.LANG_MAIN.equals(Constants.LANG_RO) &&
                    (type.equals(LIDOConstants.LIDO_TYPE_CIMEC) || type.equals(LIDOConstants.LIDO_TYPE_GUID))
            ) {
                resource.addProperty(DC_11.identifier, Constants.CIMEC_LINK_RO + text, Constants.LANG_RO);
                resource.addProperty(DC_11.identifier, Constants.CIMEC_LINK_EN + text, Constants.LANG_EN);
            } else {
                resource.addProperty(DC_11.identifier, text);
            }
        }
    }

    /**
     * Get the list of identifiers
     * @param model The RDF graph
     * @param identifierComplexType <b>IdentifierComplexType</b>
     * @return
     */
    public ArrayList<Literal> getIdentifierList(Model model, IdentifierComplexType identifierComplexType) {
        ArrayList<Literal> idList = new ArrayList<>();
        String text = identifierComplexType.getText();
        String type = identifierComplexType.getType().getType();

        if (text != null) {
            if (type.equals(LIDOConstants.LIDO_TYPE_CIMEC) || type.equals(LIDOConstants.LIDO_TYPE_GUID)) {
                idList.add(model.createLiteral(Constants.CIMEC_LINK_RO + text, Constants.LANG_RO));
                idList.add(model.createLiteral(Constants.CIMEC_LINK_EN + text, Constants.LANG_EN));
            } else {
                idList.add(model.createLiteral(text));
            }
        }

        return idList;
    }
}
