package ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectClassificationWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.classificationWrap.ClassificationProcessing;
import ro.webdata.parser.xml.lido.core.wrap.objectClassificationWrap.ObjectClassificationWrap;
import ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.objectWorkTypeWrap.ObjectWorkTypeWrapProcessing;

public class ObjectClassificationWrapProcessing {
    /**
     * Add the properties related to classification fields
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectClassificationWrap The wrapper for classification properties
     */
    public static void mapEntries(
            Model model,
            Resource providedCHO,
            ObjectClassificationWrap objectClassificationWrap
    ) {
        ClassificationProcessing.addClassificationWrap(
                model, providedCHO, objectClassificationWrap.getClassificationWrap()
        );
        ObjectWorkTypeWrapProcessing.addObjectWorkTypeWrap(
                model, providedCHO, objectClassificationWrap.getObjectWorkTypeWrap()
        );
    }
}
