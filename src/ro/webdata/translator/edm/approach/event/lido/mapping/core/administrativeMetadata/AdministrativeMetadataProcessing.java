package ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.ORE;
import ro.webdata.parser.xml.lido.core.leaf.administrativeMetadata.AdministrativeMetadata;
import ro.webdata.parser.xml.lido.core.wrap.recordWrap.RecordWrap;
import ro.webdata.parser.xml.lido.core.wrap.resourceWrap.ResourceWrap;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.RecordWrapProcessing.RecordWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.ResourceWrapProcessing.ResourceWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.RecordIDProcessing;

public class AdministrativeMetadataProcessing {
    private RecordIDProcessing recordIDProcessing = new RecordIDProcessing();
    private RecordWrapProcessing recordWrapProcessing = new RecordWrapProcessing();
    private ResourceWrapProcessing resourceWrapProcessing = new ResourceWrapProcessing();

    public void processing(
            Model model,
            Resource providedCHO,
            AdministrativeMetadata administrativeMetadata
    ) {
        RecordWrap recordWrap = administrativeMetadata.getRecordWrap();
        ResourceWrap resourceWrap = administrativeMetadata.getResourceWrap();
        Resource aggregation = generateAggregation(
                model, administrativeMetadata.getRecordWrap()
        );

        recordWrapProcessing.addRecordWrap(
                model, aggregation, providedCHO, recordWrap
        );
        resourceWrapProcessing.addResourceWrap(
                model, aggregation, resourceWrap
        );
    }

    /**
     * Generate the RDF "ore:Aggregation" object
     * @param model The RDF graph
     * @param recordWrap The <b>lido:recordWrap</b> object
     * @return <b>Resource</b>
     */
    private Resource generateAggregation(Model model, RecordWrap recordWrap) {
        String identifier = recordIDProcessing.consolidatesIdentifiers(recordWrap);

        Resource aggregation = model.createResource(
                Namespace.NS_REPO_RESOURCE_AGGREGATION + identifier
        );
        aggregation.addProperty(RDF.type, ORE.Aggregation);

        return aggregation;
    }
}
