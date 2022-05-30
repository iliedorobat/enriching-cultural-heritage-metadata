package ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.graph.GraphResource;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.event.lido.commons.URIUtils;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE;

public class PublisherMapping {
    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        String relativeUri = GraphResource.generateURI("", EDM.Agent, value);
        String publisherUri = URIUtils.prepareUri(NS_REPO_RESOURCE, relativeUri);

        Resource publisher = model
                .createResource(publisherUri)
                .addProperty(RDF.type, EDM.Agent)
                .addProperty(SKOS.prefLabel, value, language);

        if (!qualifier.equals(BasicRecord.EMPTY) && !qualifier.equals(BasicRecord.NONE))
            publisher.addProperty(SKOS.note, qualifier, language);

        providedCHO.addProperty(DC_11.publisher, publisher);
    }
}
