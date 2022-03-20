package ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.ResourceWrapProcessing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.lido.core.leaf.linkResource.LinkResource;
import ro.webdata.parser.xml.lido.core.leaf.resourceRepresentation.ResourceRepresentation;
import ro.webdata.parser.xml.lido.core.leaf.rightsResource.RightsResource;
import ro.webdata.parser.xml.lido.core.leaf.rightsType.RightsType;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;
import ro.webdata.parser.xml.lido.core.set.resourceSet.ResourceSet;
import ro.webdata.parser.xml.lido.core.wrap.resourceWrap.ResourceWrap;

import java.util.ArrayList;

public class ResourceWrapProcessing {
    public static void mapEntries(
            Model model,
            Resource aggregation,
            ResourceWrap resourceWrap
    ) {
        addResourceSet(model, aggregation, resourceWrap.getResourceSet());
    }

    private static void addResourceSet(
            Model model,
            Resource aggregation,
            ArrayList<ResourceSet> resourceSetList
    ) {
        for (ResourceSet resourceSet : resourceSetList) {
            aggregateResourceWeb(
                    model, aggregation, resourceSet.getResourceRepresentation(), resourceSet.getRightsResource()
            );
        }
    }

    /**
     * Add the "edm:isShownBy" and "edm:object" properties to the Aggregation object
     * @param model The RDF graph
     * @param aggregation The Aggregation object
     * @param resourceRepresentationList The list with <b>lido:resourceRepresentation</b> elements
     * @param rightsResourceList The list with <b>lido:rightsResource</b> elements
     */
    private static void aggregateResourceWeb(
            Model model,
            Resource aggregation,
            ArrayList<ResourceRepresentation> resourceRepresentationList,
            ArrayList<RightsResource> rightsResourceList
    ) {
        for (ResourceRepresentation resourceRepresentation : resourceRepresentationList) {
            Resource webResource = generateWebResource(
                    model, resourceRepresentation, rightsResourceList
            );
            aggregation.addProperty(EDM.isShownBy, webResource);
            // EDM.object is used for generating previews for use in the Europeana portal
            // This may be the same URL as edm:isShownBy
            aggregation.addProperty(EDM.object, webResource);
        }
    }

    /**
     * Generate an <b>edm:WebResource</b>
     * @param model The RDF graph
     * @param resourceRepresentation <b>lido:rightsResource</b> element
     * @return <b>Resource</b>
     */
    private static Resource generateWebResource(
            Model model,
            ResourceRepresentation resourceRepresentation,
            ArrayList<RightsResource> rightsResourceList
    ) {
        LinkResource linkResource = resourceRepresentation.getLinkResource();
        String resourceLink = Text.encodeSpace(
                linkResource.getText()
        );

        Resource webResource = model.createResource(resourceLink);
        webResource.addProperty(RDF.type, EDM.WebResource);
        webResource.addProperty(DC_11.format, getResourceFormat(resourceLink));
        addRightsProperty(model, webResource, rightsResourceList);

        return webResource;
    }

    /**
     * Add the <b>edm:rights</b> to the provided <b>edm:WebResource</b> Resource
     * @param model The RDF graph
     * @param webResource <b>edm:WebResource</b>
     * @param list The list with <b>lido:rightsResource</b> elements
     */
    private static void addRightsProperty(Model model, Resource webResource, ArrayList<RightsResource> list) {
        for (RightsResource rightsResource : list) {
            ArrayList<RightsType> rightsTypeList = rightsResource.getRightsType();

            for (RightsType rightsType : rightsTypeList) {
                ArrayList<Term> termList = rightsType.getTerm();

                for (Term term : termList) {
                    Resource edmRights = model.createResource(term.getText());
                    webResource.addProperty(EDM.rights, edmRights);
                }
            }
        }
    }

    /**
     * Get the file format from the provided link
     * @param link The provided link
     * @return <b>String</b>
     */
    private static String getResourceFormat(String link) {
        int index = link.lastIndexOf(".") + 1;
        return link.substring(index);
    }
}
