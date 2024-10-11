package ro.webdata.echo.translator.edm.lido.mapping.core.administrativeMetadata.ResourceWrapProcessing;

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
import java.util.List;

public class ResourceWrapProcessing {
    private static final ArrayList<String> imageTypes = new ArrayList<>(
            // Common image file types: https://developer.mozilla.org/en-US/docs/Web/Media/Formats/Image_types
            List.of("apng", "avif", "gif", "jpg", "jpeg", "jfif", "pjpeg", "pjp", "png", "svg", "webp")
    );

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
     * Add the "edm:isShownBy", "edm:hasView" and "edm:object" properties to the Aggregation object
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
            String resourceLink = getResourceLink(resourceRepresentation);
            String resourceFormatType = getResourceFormat(resourceLink);

            Resource webResource = generateWebResource(
                    model, rightsResourceList, resourceLink
            );

            if (!aggregation.hasProperty(EDM.isShownBy)) {
                aggregation.addProperty(EDM.isShownBy, webResource);
            } else {
                aggregation.addProperty(EDM.hasView, webResource);
            }

            // EDM.object is used for generating previews for use in the Europeana portal
            // This must be an image, even if it is for a sound object and may be the same URL as edm:isShownBy
            if (imageTypes.contains(resourceFormatType)) {
                aggregation.addProperty(EDM.object, webResource);
            }
        }
    }

    /**
     * Generate an <b>edm:WebResource</b>
     * @param model The RDF graph
     * @param rightsResourceList The list with <b>lido:rightsResource</b> elements
     * @param resourceLink Encoded resource link
     * @return <b>Resource</b>
     */
    private static Resource generateWebResource(
            Model model,
            ArrayList<RightsResource> rightsResourceList,
            String resourceLink
    ) {
        String resourceFormatType = getResourceFormat(resourceLink);
        Resource webResource = model
                .createResource(resourceLink)
                .addProperty(RDF.type, EDM.WebResource)
                .addProperty(DC_11.format, resourceFormatType);
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
     * Extract and encode the resource's link
     * @param resourceRepresentation <b>edm:resourceRepresentation</b>
     * @return Resource's encoded link
     */
    private static String getResourceLink(ResourceRepresentation resourceRepresentation) {
        LinkResource linkResource = resourceRepresentation.getLinkResource();
        return Text.encodeSpace(
                linkResource.getText()
        );
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
