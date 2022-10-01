package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.parser.xml.lido.core.complex.identifierComplexType.IdentifierComplexType;
import ro.webdata.translator.commons.MuseumUtils;

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
            // NOTE: Only the entries from romanian dataset could have a CIMEC id (link)
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

        if (text != null) {
            // Skip adding entries which have CIEMC id because they have already been added in an earlier stage
            // (look at LegalBodyRefComplexTypeProcessing.createLegalBodyRef method)
            // Only the entries from romanian dataset could have a CIMEC id (link)
            if (!MuseumUtils.hasCimecCode(MuseumUtils.enJsonArray, text)) {
                idList.add(model.createLiteral(text));
            }
        }

        return idList;
    }
}
