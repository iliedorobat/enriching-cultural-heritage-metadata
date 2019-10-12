package ro.webdata.lido.convert.edm.common;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.RDFS;
import ro.webdata.lido.convert.edm.common.constants.FileConstants;
import ro.webdata.lido.convert.edm.common.constants.NSConstants;

public class PropertyUtils {
    /**
     * Create a subProperty
     * @param model The RDF graph
     * @param name The name of the subProperty
     * @param mainProperty The parent property
     * @return Property - the new created subProperty
     */
    //TODO: createSubProperty(Model model, Property mainProperty, String name)
    public static Property createSubProperty(Model model, String name, Property mainProperty) {
        String subPropertyName = TextUtils.prepareCamelCaseText(name);
        Property subProperty = model.createProperty(
                NSConstants.NS_REPO_PROPERTY
                + FileConstants.FILE_SEPARATOR + subPropertyName
        );
        subProperty.addProperty(RDFS.subPropertyOf, mainProperty);
        return subProperty;
    }
}
