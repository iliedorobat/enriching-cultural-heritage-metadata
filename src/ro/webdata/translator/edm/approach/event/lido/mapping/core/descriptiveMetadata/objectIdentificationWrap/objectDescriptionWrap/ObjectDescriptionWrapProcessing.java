package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.objectDescriptionWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.lido.core.attribute.LidoType;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveNoteValue.DescriptiveNoteValue;
import ro.webdata.parser.xml.lido.core.set.objectDescriptionSet.ObjectDescriptionSet;
import ro.webdata.parser.xml.lido.core.wrap.objectDescriptionWrap.ObjectDescriptionWrap;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.TextComplexTypeProcessing;

import java.util.ArrayList;

public class ObjectDescriptionWrapProcessing {
    /**
     * Add the obverseDescription and reverseDescription subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectDescriptionWrap The wrapper for Object Description properties
     */
    public static void addObjectDescriptionWrap(
            Model model, Resource providedCHO, ObjectDescriptionWrap objectDescriptionWrap
    ) {
        if (objectDescriptionWrap != null) {
            ArrayList<ObjectDescriptionSet> objectDescriptionSetList = objectDescriptionWrap.getObjectDescriptionSet();
            addObjectDescriptionSet(model, providedCHO, objectDescriptionSetList);
        }
    }

    /**
     * Add the obverseDescription and reverseDescription subProperties to the provided CHO
     * for all the <b>ObjectDescriptionSet</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectDescriptionSetList The list with <b>ObjectDescriptionSet</b> objects
     */
    private static void addObjectDescriptionSet(
            Model model, Resource providedCHO, ArrayList<ObjectDescriptionSet> objectDescriptionSetList
    ) {
        for (ObjectDescriptionSet objectDescriptionSet : objectDescriptionSetList) {
            ArrayList<DescriptiveNoteValue> descriptiveNoteValueList = objectDescriptionSet.getDescriptiveNoteValue();
            addDescriptiveNoteValueList(model, providedCHO, descriptiveNoteValueList, objectDescriptionSet.getType());
        }
    }

    /**
     * Add the obverseDescription and reverseDescription subProperties to the provided CHO
     * for all the <b>DescriptiveNoteValue</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param descriptiveNoteValueList Array with <b>DescriptiveNoteValue</b> objects
     * @param setType The LidoType property of the <b>ObjectDescriptionSet</b> object
     */
    private static void addDescriptiveNoteValueList(
            Model model, Resource providedCHO, ArrayList<DescriptiveNoteValue> descriptiveNoteValueList, LidoType setType
    ) {
        for (DescriptiveNoteValue descriptiveNoteValue : descriptiveNoteValueList) {
            addDescriptiveNoteValue(model, providedCHO, descriptiveNoteValue, setType);
        }
    }

    /**
     * Add the obverseDescription and reverseDescription subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param descriptiveNoteValue <b>DescriptiveNoteValue</b> object
     * @param setType The LidoType property of the <b>ObjectDescriptionSet</b> object
     */
    private static void addDescriptiveNoteValue(
            Model model, Resource providedCHO, DescriptiveNoteValue descriptiveNoteValue, LidoType setType
    ) {
        TextComplexTypeProcessing.addDescription(model, providedCHO, descriptiveNoteValue, setType);
    }
}
