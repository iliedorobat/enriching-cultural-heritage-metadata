package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.objectDescriptionWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.TextComplexTypeProcessing;
import ro.webdata.parser.xml.lido.core.attribute.LidoType;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveNoteValue.DescriptiveNoteValue;
import ro.webdata.parser.xml.lido.core.set.objectDescriptionSet.ObjectDescriptionSet;
import ro.webdata.parser.xml.lido.core.wrap.objectDescriptionWrap.ObjectDescriptionWrap;

import java.util.ArrayList;

public class ObjectDescriptionWrapProcessing {
    private static TextComplexTypeProcessing textComplexTypeProcessing = new TextComplexTypeProcessing();

    /**
     * Add the obverseDescription and reverseDescription subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectDescriptionWrap The wrapper for Object Description properties
     */
    public void addObjectDescriptionWrap(
            Model model, Resource providedCHO, ObjectDescriptionWrap objectDescriptionWrap) {
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
    private void addObjectDescriptionSet(
            Model model, Resource providedCHO, ArrayList<ObjectDescriptionSet> objectDescriptionSetList) {
        for (int i = 0; i < objectDescriptionSetList.size(); i++) {
            ObjectDescriptionSet objectDescriptionSet = objectDescriptionSetList.get(i);
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
    private void addDescriptiveNoteValueList(
            Model model, Resource providedCHO, ArrayList<DescriptiveNoteValue> descriptiveNoteValueList,
            LidoType setType) {
        for (int i = 0; i < descriptiveNoteValueList.size(); i++) {
            addDescriptiveNoteValue(model, providedCHO, descriptiveNoteValueList.get(i), setType);
        }
    }

    /**
     * Add the obverseDescription and reverseDescription subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param descriptiveNoteValue <b>DescriptiveNoteValue</b> object
     * @param setType The LidoType property of the <b>ObjectDescriptionSet</b> object
     */
    private void addDescriptiveNoteValue(
            Model model, Resource providedCHO, DescriptiveNoteValue descriptiveNoteValue, LidoType setType) {
        textComplexTypeProcessing.addDescription(model, providedCHO, descriptiveNoteValue, setType);
    }
}
