package ro.webdata.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.displayStateEditionWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.lido.mapping.common.PropertyUtils;
import ro.webdata.lido.parser.core.leaf.displayState.DisplayState;
import ro.webdata.lido.parser.core.wrap.displayStateEditionWrap.DisplayStateEditionWrap;

import java.util.ArrayList;

public class DisplayStateEditionWrapProcessing {
    /**
     *
     * Add the <b>displayState</b> subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayStateEditionWrap The wrapper for display state edition properties
     */
    public void addDisplayStateEditionWrap(
            Model model, Resource providedCHO, DisplayStateEditionWrap displayStateEditionWrap) {
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
    private void addDisplayStateEditionSet(
            Model model, Resource providedCHO, ArrayList<DisplayState> displayStateList) {
        for (int i = 0; i < displayStateList.size(); i++) {
            addDisplayState(model, providedCHO, displayStateList.get(i));
        }
    }

    /**
     * Add the <b>displayState</b> subProperty to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayState The <b>DisplayState</b> object
     */
    private void addDisplayState(Model model, Resource providedCHO, DisplayState displayState) {
        Property property = PropertyUtils.createSubProperty(model, "Display State", DC_11.description);
        providedCHO.addProperty(property, displayState.getText());
    }
}
