package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.translator.commons.PropertyUtils;
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
    public static void addDescription(
            Model model, Resource providedCHO, TextComplexType textComplexType, LidoType setType
    ) {
        String lang = textComplexType.getLang().getLang(),
                text = textComplexType.getText(),
                type = setType.getType();

        if (text != null) {
            Literal literal = model.createLiteral(text, lang);
            Property property = PropertyUtils.createSubProperty(model, DC_11.description, type, true);
            providedCHO.addProperty(property, literal);
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
        String lang = displayObjectMeasurements.getLang().getLang(),
                text = displayObjectMeasurements.getText(),
                type = displayObjectMeasurements.getLabel().getLabel(),
                name = Text.toCamelCase(type);

        if (text != null) {
            // The group "(,\s)" is used to prevent splitting a measurement like "lungime fus - 2,40 m"
            for (String value : Text.toList(text, "(;)|(,\\s)")) {
                addMeasurement(model, providedCHO, value, lang, name);
            }
        }
    }

    /**
     * Add a specific measurement subProperty (<b>weight</b>, <b>diameter</b> etc.) to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param text The value of the measurement
     * @param lang The language used
     * @param propertyName The name of the new property
     */
    private static void addMeasurement(Model model, Resource providedCHO, String text, String lang, String propertyName) {
        Literal literal = model.createLiteral(text, lang);
        Property property = PropertyUtils.createSubProperty(model, DC_11.description, propertyName, true);
        providedCHO.addProperty(property, literal);
    }
}
