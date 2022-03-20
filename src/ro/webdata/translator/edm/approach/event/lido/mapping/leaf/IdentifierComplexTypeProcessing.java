package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.parser.xml.lido.core.complex.identifierComplexType.IdentifierComplexType;
import ro.webdata.translator.commons.MuseumUtils;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.LIDOType;

import java.util.ArrayList;

public class IdentifierComplexTypeProcessing {
    /**
     * Add an <b>dc:identifier</b> property to a resource
     * @param model The RDF graph
     * @param resource RDF resource
     * @param identifierComplexType <b>IdentifierComplexType</b>
     */
    public static void addIdentifier(Model model, Resource resource, IdentifierComplexType identifierComplexType) {
        String text = identifierComplexType.getText();

        if (text != null) {
            // Skip adding entries which have CIEMC id because they have already been added in an earlier stage
            // (look at LegalBodyRefComplexTypeProcessing.createLegalBodyRef method)
            // Only the entries from romanian dataset could have a CIMEC id (link)
            if (!MuseumUtils.hasCimecCode(MuseumUtils.enJsonArray, text)) {
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
    public static ArrayList<Literal> getIdentifierList(Model model, IdentifierComplexType identifierComplexType) {
        ArrayList<Literal> idList = new ArrayList<>();
        String text = identifierComplexType.getText();
        String type = identifierComplexType.getType().getType();

        if (text != null) {
            if (type.equals(LIDOType.CIMEC) || type.equals(LIDOType.GUID)) {
                idList.add(model.createLiteral(Constants.CIMEC_LINK_RO + text, Const.LANG_RO));
                idList.add(model.createLiteral(Constants.CIMEC_LINK_EN + text, Const.LANG_EN));
            } else {
                idList.add(model.createLiteral(text));
            }
        }

        return idList;
    }
}
