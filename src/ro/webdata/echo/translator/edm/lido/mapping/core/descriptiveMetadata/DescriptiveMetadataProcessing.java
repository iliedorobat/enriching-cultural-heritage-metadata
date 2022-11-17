package ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.eventWrap.EventWrapProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.ObjectClassificationWrapProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.ObjectIdentificationWrapProcessing;

public class DescriptiveMetadataProcessing {
    public static void mapEntries(
           Model model,
           Resource providedCHO,
           DescriptiveMetadata descriptiveMetadata
    ) {
        if (descriptiveMetadata != null) {
            ObjectClassificationWrapProcessing.mapEntries(
                    model, providedCHO, descriptiveMetadata.getObjectClassificationWrap()
            );
            ObjectIdentificationWrapProcessing.mapEntries(
                    model, providedCHO, descriptiveMetadata.getObjectIdentificationWrap()
            );
            EventWrapProcessing.mapEntries(
                    model, providedCHO, descriptiveMetadata.getEventWrap()
            );
        }
    }
}
