package ro.webdata.lido.mapping.core.descriptiveMetadata.objectClassificationWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.classificationWrap.ClassificationProcessing;
import ro.webdata.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.objectWorkTypeWrap.ObjectWorkTypeWrapProcessing;
import ro.webdata.lido.parser.core.wrap.objectClassificationWrap.ObjectClassificationWrap;

public class ObjectClassificationWrapProcessing {
    /**
     * Add the properties related to classification fields
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectClassificationWrap The wrapper for classification properties
     */
    public void processing(
            Model model,
            Resource providedCHO,
            ObjectClassificationWrap objectClassificationWrap
    ) {
        ClassificationProcessing classificationProcessing = new ClassificationProcessing();
        ObjectWorkTypeWrapProcessing objectWorkTypeWrapProcessing = new ObjectWorkTypeWrapProcessing();

        classificationProcessing.addClassificationWrap(
                model, providedCHO, objectClassificationWrap.getClassificationWrap());
        objectWorkTypeWrapProcessing.addObjectWorkTypeWrap(
                model, providedCHO, objectClassificationWrap.getObjectWorkTypeWrap());
    }
}
