package ro.webdata.echo.translator.edm.lido.mapping.core.administrativeMetadata.RecordWrapProcessing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.commons.Env;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.RecordRightsProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.RecordSourceProcessing;
import ro.webdata.parser.xml.lido.core.wrap.recordWrap.RecordWrap;

public class RecordWrapProcessing {
    public static void mapEntries(
            Model model,
            Resource aggregation,
            Resource providedCHO,
            RecordWrap recordWrap
    ) {
        Resource dataProvider = RecordSourceProcessing.generateDataProvider(model, recordWrap.getRecordSource());
        Resource provider = Env.IS_ORGANIZATION
                ? CimecProvider.generateOrganization(model)
                : CimecProvider.generateAgent(model);
        Resource intermediateProvider = Env.IS_ORGANIZATION
                ? UpbProvider.generateOrganization(model)
                : UpbProvider.generateAgent(model);
        Resource license = RecordRightsProcessing.getLicense(model, recordWrap.getRecordRights());

        aggregation.addProperty(EDM.aggregatedCHO, providedCHO);
        aggregation.addProperty(EDM.provider, provider);
        aggregation.addProperty(EDM.intermediateProvider, intermediateProvider);
        aggregation.addProperty(EDM.dataProvider, dataProvider);
        aggregation.addProperty(EDM.rights, license);
    }
}
