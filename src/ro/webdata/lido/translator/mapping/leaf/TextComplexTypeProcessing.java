package ro.webdata.lido.translator.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.lido.translator.common.PropertyUtils;
import ro.webdata.lido.translator.common.TextUtils;
import ro.webdata.parser.xml.lido.core.attribute.LidoType;
import ro.webdata.parser.xml.lido.core.complex.textComplexType.TextComplexType;
import ro.webdata.parser.xml.lido.core.leaf.displayObjectMeasurements.DisplayObjectMeasurements;

public class TextComplexTypeProcessing {
    /**
     * Add the <b>obverseDescription</b> and the <b>reverseDescription</b> subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param textComplexType <b>TextComplexType</b> object
     */
    public void addDescription(
            Model model, Resource providedCHO, TextComplexType textComplexType, LidoType setType) {
        String lang = textComplexType.getLang().getLang(),
                text = textComplexType.getText(),
                type = setType.getType();

        if (text != null) {
            Literal textLiteral = model.createLiteral(text, lang);
            if (textLiteral != null) {
                Property property = PropertyUtils.createSubProperty(model, type, DC_11.description);
                providedCHO.addProperty(property, textLiteral);
            }
        }
    }

    /**
     * Add measurements subProperties (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param displayObjectMeasurements <b>DisplayObjectMeasurements</b> object
     */
    public void addMeasurement(
            Model model, Resource providedCHO, DisplayObjectMeasurements displayObjectMeasurements) {
        String text = displayObjectMeasurements.getText(),
                type = displayObjectMeasurements.getLabel().getLabel(),
                name = TextUtils.toCamelCase(type);

        if (text != null) {
            Literal textLiteral = model.createLiteral(text);
            if (textLiteral != null) {
                Property property = PropertyUtils.createSubProperty(model, name, DC_11.description);
                providedCHO.addProperty(property, textLiteral);
            }
        }
    }
}
