package ro.webdata.lido.convert.edm.core.descriptiveMetadata;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.eventWrap.EventWrapProcessing;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectClassificationWrap.ObjectClassificationWrapProcessing;
import ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectIdentificationWrap.ObjectIdentificationWrapProcessing;
import ro.webdata.lido.parser.core.leaf.descriptiveMetadata.DescriptiveMetadata;

public class DescriptiveMetadataProcessing {
    private static ObjectClassificationWrapProcessing objectClassificationWrapProcessing = new ObjectClassificationWrapProcessing();
    private static ObjectIdentificationWrapProcessing objectIdentificationWrapProcessing = new ObjectIdentificationWrapProcessing();
    private static EventWrapProcessing eventWrapProcessing = new EventWrapProcessing();

    public void processing(
           Model model,
           Resource providedCHO,
           DescriptiveMetadata descriptiveMetadata
    ) {
        objectClassificationWrapProcessing.processing(
                model, providedCHO, descriptiveMetadata.getObjectClassificationWrap()
        );
        objectIdentificationWrapProcessing.processing(
                model, providedCHO, descriptiveMetadata.getObjectIdentificationWrap()
        );
        eventWrapProcessing.processing(
                model, providedCHO, descriptiveMetadata.getEventWrap()
        );
    }
}
