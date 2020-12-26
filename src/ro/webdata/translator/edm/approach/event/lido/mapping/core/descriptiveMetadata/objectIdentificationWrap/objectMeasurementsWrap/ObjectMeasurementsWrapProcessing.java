package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.objectMeasurementsWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.TextComplexTypeProcessing;
import ro.webdata.parser.xml.lido.core.leaf.displayObjectMeasurements.DisplayObjectMeasurements;
import ro.webdata.parser.xml.lido.core.set.objectMeasurementsSet.ObjectMeasurementsSet;
import ro.webdata.parser.xml.lido.core.wrap.objectMeasurementsWrap.ObjectMeasurementsWrap;

import java.util.ArrayList;

public class ObjectMeasurementsWrapProcessing {
    private static TextComplexTypeProcessing textComplexTypeProcessing = new TextComplexTypeProcessing();

    /**
     * TODO: http://dbpedia.org/page/Weight
     * TODO: http://dbpedia.org/page/Diameter
     * Add measurements subProperties (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectMeasurementsWrap The wrapper for object measurements properties
     */
    public void addObjectMeasurementsWrap(
            Model model, Resource providedCHO, ObjectMeasurementsWrap objectMeasurementsWrap) {
        if (objectMeasurementsWrap != null) {
            ArrayList<ObjectMeasurementsSet> objectMeasurementsSetList = objectMeasurementsWrap.getObjectMeasurementsSet();
            addObjectMeasurementsSet(model, providedCHO, objectMeasurementsSetList);
        }
    }

    /**
     * Add measurements subProperties (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * for all the <b>ObjectMeasurementsSet</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectMeasurementsSetList The list with <b>ObjectMeasurementsSet</b> objects
     */
    private void addObjectMeasurementsSet(
            Model model, Resource providedCHO, ArrayList<ObjectMeasurementsSet> objectMeasurementsSetList) {
        for (int i = 0; i < objectMeasurementsSetList.size(); i++) {
            ObjectMeasurementsSet objectDescriptionSet = objectMeasurementsSetList.get(i);
            ArrayList<DisplayObjectMeasurements> displayObjectMeasurements = objectDescriptionSet.getDisplayObjectMeasurements();
            addObjectMeasurementsList(model, providedCHO, displayObjectMeasurements);
        }
    }

    /**
     * Add measurements subProperties (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * for all the <b>DisplayObjectMeasurements</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayObjectMeasurements The list with <b>DisplayObjectMeasurements</b> objects
     */
    private void addObjectMeasurementsList(
            Model model, Resource providedCHO, ArrayList<DisplayObjectMeasurements> displayObjectMeasurements) {
        for (int i = 0; i < displayObjectMeasurements.size(); i++) {
            addObjectMeasurements(model, providedCHO, displayObjectMeasurements.get(i));
        }
    }

    /**
     * Add measurements subProperties (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayObjectMeasurements <b>DisplayObjectMeasurements</b> object
     */
    private void addObjectMeasurements(
            Model model, Resource providedCHO, DisplayObjectMeasurements displayObjectMeasurements) {
        textComplexTypeProcessing.addMeasurement(model, providedCHO, displayObjectMeasurements);
    }
}
