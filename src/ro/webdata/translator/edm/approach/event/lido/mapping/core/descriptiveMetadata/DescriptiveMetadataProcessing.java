package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.eventWrap.EventWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.ObjectClassificationWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.ObjectIdentificationWrapProcessing;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;

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
