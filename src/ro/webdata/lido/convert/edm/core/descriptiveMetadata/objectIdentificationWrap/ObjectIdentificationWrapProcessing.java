package ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectIdentificationWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectIdentificationWrap.displayStateEditionWrap.DisplayStateEditionWrapProcessing;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectIdentificationWrap.objectDescriptionWrap.ObjectDescriptionWrapProcessing;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectIdentificationWrap.objectMeasurementsWrap.ObjectMeasurementsWrapProcessing;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectIdentificationWrap.repositoryWrap.RepositoryWrapProcessing;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectIdentificationWrap.titleWrap.TitleWrapProcessing;
import ro.webdata.lido.parser.core.wrap.displayStateEditionWrap.DisplayStateEditionWrap;
import ro.webdata.lido.parser.core.wrap.objectDescriptionWrap.ObjectDescriptionWrap;
import ro.webdata.lido.parser.core.wrap.objectIdentificationWrap.ObjectIdentificationWrap;
import ro.webdata.lido.parser.core.wrap.objectMeasurementsWrap.ObjectMeasurementsWrap;
import ro.webdata.lido.parser.core.wrap.repositoryWrap.RepositoryWrap;
import ro.webdata.lido.parser.core.wrap.titleWrap.TitleWrap;

public class ObjectIdentificationWrapProcessing {
    /**
     * Add the properties related to object identification fields
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectIdentificationWrap The wrapper for object identification properties
     */
    public void processing(
            Model model,
            Resource providedCHO,
            ObjectIdentificationWrap objectIdentificationWrap
    ) {
        TitleWrapProcessing titleWrapProcessing = new TitleWrapProcessing();
        RepositoryWrapProcessing repositoryWrapProcessing = new RepositoryWrapProcessing();
        DisplayStateEditionWrapProcessing displayStateEditionWrapProcessing = new DisplayStateEditionWrapProcessing();
        ObjectDescriptionWrapProcessing objectDescriptionWrapProcessing = new ObjectDescriptionWrapProcessing();
        ObjectMeasurementsWrapProcessing objectMeasurementsWrapProcessing = new ObjectMeasurementsWrapProcessing();

        titleWrapProcessing.addTitleWrap(
                model, providedCHO, objectIdentificationWrap.getTitleWrap());
        repositoryWrapProcessing.addRepositoryWrap(
                model, providedCHO, objectIdentificationWrap.getRepositoryWrap());
        displayStateEditionWrapProcessing.addDisplayStateEditionWrap(
                model, providedCHO, objectIdentificationWrap.getDisplayStateEditionWrap());
        objectDescriptionWrapProcessing.addObjectDescriptionWrap(
                model, providedCHO, objectIdentificationWrap.getObjectDescriptionWrap());
        objectMeasurementsWrapProcessing.addObjectMeasurementsWrap(
                model, providedCHO, objectIdentificationWrap.getObjectMeasurementsWrap());
    }


}
