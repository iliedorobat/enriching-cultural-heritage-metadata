package ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.displayStateEditionWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.parser.xml.lido.core.leaf.displayState.DisplayState;
import ro.webdata.parser.xml.lido.core.wrap.displayStateEditionWrap.DisplayStateEditionWrap;
import ro.webdata.translator.edm.approach.event.lido.commons.PropertyUtils;

import java.util.ArrayList;

public class DisplayStateEditionWrapProcessing {
    /**
     *
     * Add the <b>displayState</b> subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayStateEditionWrap The wrapper for display state edition properties
     */
    public static void addDisplayStateEditionWrap(
            Model model, Resource providedCHO, DisplayStateEditionWrap displayStateEditionWrap
    ) {
        if (displayStateEditionWrap != null) {
            ArrayList<DisplayState> displayStateList = displayStateEditionWrap.getDisplayState();
            addDisplayStateEditionSet(model, providedCHO, displayStateList);
        }
    }

    /**
     * Add the <b>displayState</b> subProperties to the provided CHO for all the <b>DisplayState</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayStateList The list with <b>DisplayState</b> objects
     */
    private static void addDisplayStateEditionSet(
            Model model, Resource providedCHO, ArrayList<DisplayState> displayStateList
    ) {
        for (DisplayState displayState : displayStateList) {
            addDisplayState(model, providedCHO, displayState);
        }
    }

    /**
     * Add the <b>displayState</b> subProperty to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayState The <b>DisplayState</b> object
     */
    private static void addDisplayState(Model model, Resource providedCHO, DisplayState displayState) {
        Property property = PropertyUtils.createSubProperty(model, DC_11.description, "Display State", true);
        providedCHO.addProperty(property, displayState.getText());
    }
}
