package ro.webdata.translator.edm.approach.event.lido.commons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.validator.UrlValidator;
import ro.webdata.translator.commons.FileConstants;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;
import static ro.webdata.echo.commons.graph.Namespace.*;

//TODO: move the class to ro.webdata.translator.commons
public final class PropertyUtils {
    private static final List<String> EXCEPTED_URLS = Arrays.asList(
            "http://ghidulmuzeelor.cimec.ro/idEN.asp?k=2083&-Muzeul-Popa-Popa`s-TIMISOARA-Timis",
            "http://ghidulmuzeelor.cimec.ro/id.asp?k=2083&-Muzeul-Popa-Popa`s-TIMISOARA-Timis"
    );

    public static void addProperties(Model model, Resource museum, Property parentProperty, String modelProperty, JsonObject jsonObject, String accessor, String lang) {
        try {
            JsonArray array = jsonObject.getAsJsonArray(accessor);
            for (JsonElement value : array) {
                addProperty(model, museum, parentProperty, modelProperty, value, lang);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void addProperty(Model model, Resource museum, Property parentProperty, String propertyName, JsonObject object, String objectProperty, String lang) {
        try {
            JsonElement value = object.get(objectProperty);
            addProperty(model, museum, parentProperty, propertyName, value, lang);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void addProperty(Model model, Resource museum, Property modelProperty, JsonObject object, String objectProperty, String lang) {
        try {
            JsonElement value = object.get(objectProperty);
            addProperty(model, museum, modelProperty, value, lang);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void addProperty(Model model, Resource museum, Property parentProperty, String propertyName, JsonElement value, String lang) {
        if (value instanceof JsonPrimitive) {
            Property property = createSubProperty(model, parentProperty, propertyName, false);
            museum.addProperty(property, value.getAsString(), lang);
        }
    }

    public static void addProperty(Model model, Resource museum, Property modelProperty, JsonElement value, String lang) {
        if (value instanceof JsonPrimitive) {
            museum.addProperty(modelProperty, value.getAsString(), lang);
        }
    }

    public static void addUriProperty(Model model, Resource museum, Property property, JsonElement value) {
        if (value instanceof JsonPrimitive) {
            String link = value.getAsString();

            // EXCEPTED_URLS
            // Suppress the URL check as long as the url works. "`" is not a valid character for a URL
            // and the expression <validator.isValid(url)> will be evaluated as being false
            if (UrlValidator.isValid(link, EXCEPTED_URLS)) {
                Resource uri = model.createResource(link);
                museum.addProperty(property, uri);
            } else {
                System.err.println("\"" + link + "\" is not a valid URL");
            }
        }
    }

    public static void addUriProperties(Model model, Resource museum, Property parentProperty, String modelProperty, JsonObject jsonObject, String accessor) {
        try {
            JsonArray array = jsonObject.getAsJsonArray(accessor);
            for (JsonElement value : array) {
                if (value instanceof JsonPrimitive) {
                    Property property = createSubProperty(model, parentProperty, modelProperty, false);
                    addUriProperty(model, museum, property, value);
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a subProperty
     * @param model The RDF graph
     * @param mainProperty The parent property
     * @param name The name of the new subProperty
     * @param prepareCamelCase Specify it the subProperty should be normalized
     * @return Property - the new created subProperty
     */
    public static Property createSubProperty(Model model, Property mainProperty, String name, boolean prepareCamelCase) {
        String subPropertyName = prepareCamelCase
                ? Text.prepareCamelCaseText(name)
                : name;

        Property subProperty = model.createProperty(
                mapProperty(subPropertyName)
        );
        subProperty.addProperty(RDFS.subPropertyOf, mainProperty);

        StringWriter sw = new StringWriter()
                .append(mainProperty.toString())
                .append("|")
                .append(subProperty.toString())
                .append("\n");
        File.write(sw, FileConstants.PATH_OUTPUT_PROPERTIES_FILE, true);

        return subProperty;
    }

    private static String mapProperty(String property) {
        switch (property) {
            case BUILDING_LMI_CODE:             return NS_REPO_PROPERTY + LMI_CODE;
            case CODE:                          return NS_REPO_PROPERTY + "cimec";
            case COLLECTION_IMPORTANCE:         return NS_REPO_PROPERTY + IMPORTANCE;
            case COLLECTION_PICTURES:           return NS_DBPEDIA_PROPERTY + "photo";
            case COLLECTION_GENERAL_PROFILE:    return NS_REPO_PROPERTY + "generalProfile";
            case COLLECTION_MAIN_PROFILE:       return NS_REPO_PROPERTY + "mainProfile";
            case CONTACT_PERSON_NAME:           return NS_REPO_PROPERTY + "employee";
            case CONTACT_PERSON_POSITION:       return NS_REPO_PROPERTY + "employeePosition";
            case CONTACT_DIRECTOR:              return NS_DBPEDIA_PROPERTY + "director";
            case CONTACT_EMAIL:                 return NS_DBPEDIA_PROPERTY + "email";
            case CONTACT_FAX:                   return NS_DBPEDIA_PROPERTY + "fax";
            case CONTACT_SOCIAL_MEDIA:          return NS_DBPEDIA_PROPERTY + "socialMedia";
            case CONTACT_PHONE:                 return NS_DBPEDIA_PROPERTY + "telephoneNumber";
            case CONTACT_TIME_TABLE:            return NS_DBPEDIA_PROPERTY + "schedule";
            case CONTACT_VIRTUAL_TOUR:          return NS_REPO_PROPERTY + "virtualTour";
            case CONTACT_WEB:                   return NS_DBPEDIA_PROPERTY + "website";
            case DESCRIPTION_DETAILS:           return NS_DBPEDIA_PROPERTY + "details";
            case DESCRIPTION_HISTORIC:          return NS_DBPEDIA_PROPERTY + "historic";
            case DESCRIPTION_SUMMARY:           return NS_DBPEDIA_PROPERTY + "summary";
            case MUSEUM_ACCREDITATION:          return NS_DBPEDIA_PROPERTY + "accreditation";
            case LOCATION_ACCESS:               return NS_DBPEDIA_ONTOLOGY + "access";
            case LOCATION_ADDRESS:              return NS_DBPEDIA_ONTOLOGY + "address";
            case LOCATION_ADM_UNIT:             return NS_DBPEDIA_PROPERTY + "administrativeDivision";
            case LOCATION_COMMUNE:              return NS_DBPEDIA_PROPERTY + "commune";
            case LOCATION_COUNTY:               return NS_DBPEDIA_PROPERTY + "county";
            case LOCATION_GEO_LATITUDE:         return NS_DBPEDIA_PROPERTY + "latitude";
            case LOCATION_GEO_LONGITUDE:        return NS_DBPEDIA_PROPERTY + "longitude";
            case LOCATION_LOCALITY_CODE:        return NS_REPO_PROPERTY + "siruta";
            case LOCATION_LOCALITY_NAME:        return NS_DBPEDIA_PROPERTY + "locality";
            case LOCATION_ZIP_CODE:             return NS_DBPEDIA_ONTOLOGY + "postalCode";
            case PUBLICATIONS_MATERIAL:         return NS_DBPEDIA_PROPERTY + "publication";
            case MUSEUM_SUPERVISED_BY:          return NS_REPO_PROPERTY + SUPERVISED_BY;
            case MUSEUM_SUPERVISOR_FOR:         return NS_REPO_PROPERTY + SUPERVISOR_FOR;
            default:
                switch (property.toLowerCase()) {
                    case "biotope":     return NS_REPO_PROPERTY + "biotope";
                    case "colour":      return NS_DBPEDIA_ONTOLOGY + "colourName";
                    case "diameter":    return NS_DBPEDIA_ONTOLOGY + "diameter";
                    case "dimensions":  return NS_DBPEDIA_PROPERTY + "size";
                    case "fineness":    return NS_REPO_PROPERTY + "fineness";
                    case "height":      return NS_DBPEDIA_ONTOLOGY + "height";
                    case "length":      return NS_DBPEDIA_ONTOLOGY + "length";
                    case "sex":         return NS_DBPEDIA_ONTOLOGY + "sex";
                    case "weight":      return NS_DBPEDIA_ONTOLOGY + "weight";
                    case "width":       return NS_DBPEDIA_ONTOLOGY + "width";
                    default:            return NS_REPO_PROPERTY + property;
                }
        }
    }

    /**
     * TODO: replace the method
     * Create a subProperty
     * @param model The RDF graph
     * @param name The name of the subProperty
     * @param mainProperty The parent property
     * @return Property - the new created subProperty
     * @deprecated
     */
    //TODO: createSubProperty(Model model, Property mainProperty, String name)
    public static Property createSubProperty(Model model, String name, Property mainProperty) {
        //TODO: remove prepareCamelCaseText? (example of property: "lmiCode")
        String subPropertyName = Text.prepareCamelCaseText(name);

        String namespace = getPropertyNamespace(subPropertyName);
        Property subProperty = model.createProperty(namespace + subPropertyName);
        subProperty.addProperty(RDFS.subPropertyOf, mainProperty);

        StringWriter sw = new StringWriter()
                .append(mainProperty.toString())
                .append("|")
                .append(subProperty.toString())
                .append("\n");
        File.write(sw, FileConstants.PATH_OUTPUT_PROPERTIES_FILE, true);

        return subProperty;
    }

    /**
     * TODO: replace the method
     * Get the property namespace
     * @param property The name of the property
     * @return The namespace of the property
     * @deprecated
     */
    private static String getPropertyNamespace(String property) {
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
