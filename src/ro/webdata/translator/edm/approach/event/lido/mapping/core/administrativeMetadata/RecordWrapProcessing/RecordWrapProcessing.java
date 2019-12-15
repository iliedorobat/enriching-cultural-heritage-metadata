package ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.RecordWrapProcessing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.translator.edm.approach.event.lido.common.ResourceUtils;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.RecordRightsProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.RecordSourceProcessing;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.parser.xml.lido.core.wrap.recordWrap.RecordWrap;

public class RecordWrapProcessing {
    private static RecordSourceProcessing recordSourceProcessing = new RecordSourceProcessing();
    private static RecordRightsProcessing recordRightsProcessing = new RecordRightsProcessing();

    public void addRecordWrap(
            Model model,
            Resource aggregation,
            Resource providedCHO,
            RecordWrap recordWrap
    ) {
        Resource dataProvider = recordSourceProcessing.generateDataProvider(
                model, recordWrap.getRecordSource()
        );
        Resource provider = ResourceUtils.generateProvider(model);
        Resource intermediateProvider = ResourceUtils.generateIntermediateProvider(model);
        Resource license = recordRightsProcessing.getLicense(
                model, recordWrap.getRecordRights()
        );

        aggregation.addProperty(EDM.aggregatedCHO, providedCHO);
        aggregation.addProperty(EDM.provider, provider);
        aggregation.addProperty(EDM.intermediateProvider, intermediateProvider);
        aggregation.addProperty(EDM.dataProvider, dataProvider);
        aggregation.addProperty(EDM.rights, license);
    }
}
