package ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.objectDescriptionWrap.ObjectDescriptionWrapProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.objectMeasurementsWrap.ObjectMeasurementsWrapProcessing;
import ro.webdata.parser.xml.lido.core.wrap.objectIdentificationWrap.ObjectIdentificationWrap;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.displayStateEditionWrap.DisplayStateEditionWrapProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.repositoryWrap.RepositoryWrapProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.titleWrap.TitleWrapProcessing;

public class ObjectIdentificationWrapProcessing {
    /**
     * Add the properties related to object identification fields
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectIdentificationWrap The wrapper for object identification properties
     */
    public static void mapEntries(
            Model model,
            Resource providedCHO,
            ObjectIdentificationWrap objectIdentificationWrap
    ) {
        TitleWrapProcessing.addTitleWrap(
                model, providedCHO, objectIdentificationWrap.getTitleWrap()
        );
        RepositoryWrapProcessing.addRepositoryWrap(
                model, providedCHO, objectIdentificationWrap.getRepositoryWrap()
        );
        DisplayStateEditionWrapProcessing.addDisplayStateEditionWrap(
                model, providedCHO, objectIdentificationWrap.getDisplayStateEditionWrap()
        );
        ObjectDescriptionWrapProcessing.addObjectDescriptionWrap(
                model, providedCHO, objectIdentificationWrap.getObjectDescriptionWrap()
        );
        ObjectMeasurementsWrapProcessing.addObjectMeasurementsWrap(
                model, providedCHO, objectIdentificationWrap.getObjectMeasurementsWrap()
        );
    }


}
