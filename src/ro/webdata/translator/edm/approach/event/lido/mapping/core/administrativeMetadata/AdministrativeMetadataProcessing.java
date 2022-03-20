package ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.graph.vocab.ORE;
import ro.webdata.parser.xml.lido.core.leaf.administrativeMetadata.AdministrativeMetadata;
import ro.webdata.parser.xml.lido.core.wrap.recordWrap.RecordWrap;
import ro.webdata.parser.xml.lido.core.wrap.resourceWrap.ResourceWrap;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.RecordWrapProcessing.RecordWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.ResourceWrapProcessing.ResourceWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.RecordIDProcessing;

import static ro.webdata.translator.commons.EnvConstants.NS_REPO_RESOURCE_AGGREGATION;

public class AdministrativeMetadataProcessing {
    public static void mapEntries(
            Model model,
            Resource providedCHO,
            AdministrativeMetadata administrativeMetadata
    ) {
        if (administrativeMetadata != null) {
            RecordWrap recordWrap = administrativeMetadata.getRecordWrap();
            ResourceWrap resourceWrap = administrativeMetadata.getResourceWrap();
            Resource aggregation = generateAggregation(model, recordWrap);

            RecordWrapProcessing.mapEntries(
                    model, aggregation, providedCHO, recordWrap
            );
            ResourceWrapProcessing.mapEntries(
                    model, aggregation, resourceWrap
            );
        }
    }

    /**
     * Generate the RDF "ore:Aggregation" object
     * @param model The RDF graph
     * @param recordWrap The <b>lido:recordWrap</b> object
     * @return <b>Resource</b>
     */
    private static Resource generateAggregation(Model model, RecordWrap recordWrap) {
        String identifier = RecordIDProcessing.consolidatesIdentifiers(recordWrap);

        Resource aggregation = model.createResource(
                NS_REPO_RESOURCE_AGGREGATION + identifier
        );
        aggregation.addProperty(RDF.type, ORE.Aggregation);

        return aggregation;
    }
}
