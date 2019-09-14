package ro.webdata.lido.convert.edm.core.administrativeMetadata.ResourceWrapProcessing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.lido.convert.edm.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.linkResource.LinkResource;
import ro.webdata.lido.parser.core.leaf.resourceRepresentation.ResourceRepresentation;
import ro.webdata.lido.parser.core.leaf.rightsResource.RightsResource;
import ro.webdata.lido.parser.core.leaf.rightsType.RightsType;
import ro.webdata.lido.parser.core.leaf.term.Term;
import ro.webdata.lido.parser.core.set.resourceSet.ResourceSet;
import ro.webdata.lido.parser.core.wrap.resourceWrap.ResourceWrap;

import java.net.URLEncoder;
import java.util.ArrayList;

public class ResourceWrapProcessing {
    public void addResourceWrap(
            Model model,
            Resource aggregation,
            ResourceWrap resourceWrap
    ) {
        addResourceSet(model, aggregation, resourceWrap.getResourceSet());
    }

    private void addResourceSet(
            Model model,
            Resource aggregation,
            ArrayList<ResourceSet> resourceSetList
    ) {
        for (int i = 0; i < resourceSetList.size(); i++) {
            ResourceSet resourceSet = resourceSetList.get(i);
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
    private void aggregateResourceWeb(
            Model model,
            Resource aggregation,
            ArrayList<ResourceRepresentation> resourceRepresentationList,
            ArrayList<RightsResource> rightsResourceList
    ) {
        for (int j = 0; j < resourceRepresentationList.size(); j++) {
            Resource webResource = generateWebResource(
                    model, resourceRepresentationList.get(j), rightsResourceList
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
    private Resource generateWebResource(
            Model model,
            ResourceRepresentation resourceRepresentation,
            ArrayList<RightsResource> rightsResourceList
    ) {
        LinkResource linkResource = resourceRepresentation.getLinkResource();
        String resourceLink = linkResource.getText();
        //TODO: externalize (replace the spaces with the URL encoded character)
        resourceLink = resourceLink.replaceAll(" ", "%20");

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
    private void addRightsProperty(Model model, Resource webResource, ArrayList<RightsResource> list) {
        for (int i = 0; i < list.size(); i++) {
            RightsResource rightsResource = list.get(i);
            ArrayList<RightsType> rightsTypeList = rightsResource.getRightsType();

            for (int j = 0; j < rightsTypeList.size(); j++) {
                RightsType rightsType = rightsTypeList.get(j);
                ArrayList<Term> termList = rightsType.getTerm();

                for (int k = 0; k < termList.size(); k++) {
                    Term term = termList.get(k);
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
    private String getResourceFormat(String link) {
        int index = link.lastIndexOf(".") + 1;
        return link.substring(index);
    }
}
