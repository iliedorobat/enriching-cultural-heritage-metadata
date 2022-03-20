package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.eventWrap.EventWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.ObjectClassificationWrapProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.ObjectIdentificationWrapProcessing;

public class DescriptiveMetadataProcessing {
    public static void mapEntries(
           Model model,
           Resource providedCHO,
           DescriptiveMetadata descriptiveMetadata
    ) {
        if (descriptiveMetadata != null) {
            ObjectClassificationWrapProcessing.processing(
                    model, providedCHO, descriptiveMetadata.getObjectClassificationWrap()
            );
            ObjectIdentificationWrapProcessing.processing(
                    model, providedCHO, descriptiveMetadata.getObjectIdentificationWrap()
            );
            EventWrapProcessing.processing(
                    model, providedCHO, descriptiveMetadata.getEventWrap()
            );
        }
    }
}
