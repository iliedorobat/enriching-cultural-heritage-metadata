package ro.webdata.translator.edm.approach.event.lido.commons;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.RDFS;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.translator.commons.FileConstants;

import java.io.StringWriter;

public final class PropertyUtils {
    /**
     * Create a subProperty
     * @param model The RDF graph
     * @param name The name of the subProperty
     * @param mainProperty The parent property
     * @return Property - the new created subProperty
     */
    //TODO: createSubProperty(Model model, Property mainProperty, String name)
    public static Property createSubProperty(Model model, String name, Property mainProperty) {
        String subPropertyName = Text.prepareCamelCaseText(name);
        String namespace = prepareNamespace(subPropertyName);
        Property subProperty = model.createProperty(namespace + subPropertyName);
        subProperty.addProperty(RDFS.subPropertyOf, mainProperty);

        //TODO: store the data into a buffer and write it on the disc only once
        StringWriter sw = new StringWriter()
                .append(mainProperty.toString())
                .append("|")
                .append(subProperty.toString())
                .append("\n");
        File.write(sw, FileConstants.PATH_OUTPUT_PROPERTIES_FILE, true);

        return subProperty;
    }

    /**
     * Get the property namespace
     * @param property The name of the property
     * @return The namespace of the property
     */
    private static String prepareNamespace(String property) {
        switch (property.toLowerCase()) {
            case "biotope":
            case "colour":
            case "diameter":
            case "dimensions":
            case "fineness":
            case "height":
            case "length":
            case "sex":
            case "weight":
            case "width":
                return Namespace.NS_DBPEDIA_RESOURCE;
            default:
                return Namespace.NS_REPO_PROPERTY;
        }
    }
}
