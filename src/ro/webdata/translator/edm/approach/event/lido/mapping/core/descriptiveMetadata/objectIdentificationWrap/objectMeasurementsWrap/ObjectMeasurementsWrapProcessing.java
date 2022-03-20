package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.objectMeasurementsWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.lido.core.leaf.displayObjectMeasurements.DisplayObjectMeasurements;
import ro.webdata.parser.xml.lido.core.set.objectMeasurementsSet.ObjectMeasurementsSet;
import ro.webdata.parser.xml.lido.core.wrap.objectMeasurementsWrap.ObjectMeasurementsWrap;
import ro.webdata.translator.edm.approach.event.lido.mapping.leaf.TextComplexTypeProcessing;

import java.util.ArrayList;

public class ObjectMeasurementsWrapProcessing {
    /**
     * TODO: https://dbpedia.org/ontology/weight
     * TODO: https://dbpedia.org/ontology/diameter
     * Add measurements subProperties (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectMeasurementsWrap The wrapper for object measurements properties
     */
    public static void addObjectMeasurementsWrap(
            Model model, Resource providedCHO, ObjectMeasurementsWrap objectMeasurementsWrap
    ) {
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
    private static void addObjectMeasurementsSet(
            Model model, Resource providedCHO, ArrayList<ObjectMeasurementsSet> objectMeasurementsSetList
    ) {
        for (ObjectMeasurementsSet objectDescriptionSet : objectMeasurementsSetList) {
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
    private static void addObjectMeasurementsList(
            Model model, Resource providedCHO, ArrayList<DisplayObjectMeasurements> displayObjectMeasurements
    ) {
        for (DisplayObjectMeasurements displayObjectMeasurement : displayObjectMeasurements) {
            addObjectMeasurements(model, providedCHO, displayObjectMeasurement);
        }
    }

    /**
     * Add measurements subProperties (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayObjectMeasurements <b>DisplayObjectMeasurements</b> object
     */
    private static void addObjectMeasurements(
            Model model, Resource providedCHO, DisplayObjectMeasurements displayObjectMeasurements
    ) {
        TextComplexTypeProcessing.addMeasurement(model, providedCHO, displayObjectMeasurements);
    }
}
