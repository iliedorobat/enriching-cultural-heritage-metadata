package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Text;
import ro.webdata.parser.xml.lido.core.attribute.LidoType;
import ro.webdata.parser.xml.lido.core.complex.textComplexType.TextComplexType;
import ro.webdata.parser.xml.lido.core.leaf.displayObjectMeasurements.DisplayObjectMeasurements;
import ro.webdata.translator.edm.approach.event.lido.commons.PropertyUtils;

public class TextComplexTypeProcessing {
    /**
     * Add the <b>obverseDescription</b> and the <b>reverseDescription</b> subProperties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param textComplexType <b>TextComplexType</b> object
     */
    public static void addDescription(
            Model model, Resource providedCHO, TextComplexType textComplexType, LidoType setType
    ) {
        String lang = textComplexType.getLang().getLang(),
                text = textComplexType.getText(),
                type = setType.getType();

        if (text != null) {
            Literal textLiteral = model.createLiteral(text, lang);
            if (textLiteral != null) {
                Property property = PropertyUtils.createSubProperty(model, DC_11.description, type, true);
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
    public static void addMeasurement(
            Model model, Resource providedCHO, DisplayObjectMeasurements displayObjectMeasurements
    ) {
        String text = displayObjectMeasurements.getText(),
                type = displayObjectMeasurements.getLabel().getLabel(),
                name = Text.toCamelCase(type);

        if (text != null) {
            Literal textLiteral = model.createLiteral(text);
            if (textLiteral != null) {
                Property property = PropertyUtils.createSubProperty(model, DC_11.description, name, true);
                providedCHO.addProperty(property, textLiteral);
            }
        }
    }
}
