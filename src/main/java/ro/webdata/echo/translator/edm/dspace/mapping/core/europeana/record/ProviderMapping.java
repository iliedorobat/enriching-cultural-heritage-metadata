package ro.webdata.echo.translator.edm.dspace.mapping.core.europeana.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.GraphResource;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE;

public class ProviderMapping {
    public static void mapEntries(Model model, Resource providedCHO, Resource aggregation, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                String relativeUri = GraphResource.generateURI("", EDM.Agent, value, true);
                String providerUri = URIUtils.prepareUri(NS_REPO_RESOURCE, relativeUri);

                Resource provider = model
                        .createResource(providerUri)
                        .addProperty(RDF.type, EDM.Agent)
                        .addProperty(SKOS.prefLabel, value, language);

                aggregation.addProperty(EDM.provider, provider);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
