package ro.webdata.translator.edm.approach.object.dspace.mapping.core.europeana.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.GraphResource;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.object.dspace.commons.PrintMessages;

public class ProviderMapping {
    public static void processing(Model model, Resource providedCHO, Resource aggregation, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                String providerUri = GraphResource.generateURI(
                        Namespace.NS_REPO_RESOURCE, EDM.Agent, value
                );

                Resource provider = model
                        .createResource(providerUri)
                        .addProperty(RDF.type, EDM.Agent)
                        .addProperty(SKOS.prefLabel, value, language);

                aggregation.addProperty(EDM.provider, provider);
                break;
            default:
                PrintMessages.elementWarning(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
