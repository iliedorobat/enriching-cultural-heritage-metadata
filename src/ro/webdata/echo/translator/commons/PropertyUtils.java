package ro.webdata.echo.translator.commons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.validator.UrlValidator;

import java.io.StringWriter;
import java.util.ArrayList;

import static ro.webdata.echo.commons.accessor.MuseumAccessors.*;
import static ro.webdata.echo.commons.graph.Namespace.*;

public final class PropertyUtils {
    public static void addAgentUri(Model model, Resource museum, Property parentProperty, String modelProperty, String value, String lang) {
        Statement stmt = museum.getProperty(RDF.type);
        RDFNode rdfType = stmt.getObject();

        // EDM.Agent expects the value of dc:identifier and skos:node to be a literal,
        // but EDM.Organization expects a reference for each
        if (parentProperty.equals(DC_11.identifier) || parentProperty.equals(SKOS.note)) {
            if (rdfType.equals(EDM.Agent)) {
                addSubProperty(model, museum, DC_11.identifier, modelProperty, value, lang);
            } else if (rdfType.equals(FOAF.Organization)) {
                addUriProperty(model, museum, DC_11.identifier, value);
            }
        }
    }

    public static void addAgentUri(Model model, Resource museum, Property parentProperty, String modelProperty, JsonElement value, String lang) {
        if (value instanceof JsonPrimitive) {
            addAgentUri(model, museum, parentProperty, modelProperty, value.getAsString(), lang);
        }
    }

    public static void addAgentUris(Model model, Resource museum, Property parentProperty, String modelProperty, JsonObject jsonObject, String accessor, String lang) {
        try {
            JsonArray array = jsonObject.getAsJsonArray(accessor);
            for (JsonElement value : array) {
                addAgentUri(model, museum, parentProperty, modelProperty, value, lang);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void addSubProperty(Model model, Resource museum, Property parentProperty, String propertyName, String value, String lang) {
        if (value != null) {
            Literal literal = model.createLiteral(value, lang);
            Property property = createSubProperty(model, parentProperty, propertyName, false);
            museum.addProperty(property, literal);
        }
    }

    public static void addSubProperty(Model model, Resource museum, Property parentProperty, String propertyName, JsonElement value, String lang) {
        if (value instanceof JsonPrimitive) {
            addSubProperty(model, museum, parentProperty, propertyName, value.getAsString(), lang);
        }
    }

    public static void addSubProperties(Model model, Resource museum, Property parentProperty, String modelProperty, JsonObject jsonObject, String accessor, String lang) {
        try {
            JsonArray array = jsonObject.getAsJsonArray(accessor);
            for (JsonElement value : array) {
                addSubProperty(model, museum, parentProperty, modelProperty, value, lang);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static void addSubProperty(Model model, Resource museum, Property parentProperty, String propertyName, JsonObject object, String objectProperty, String lang) {
        try {
            JsonElement value = object.get(objectProperty);
            addSubProperty(model, museum, parentProperty, propertyName, value, lang);
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

    public static void addProperty(Model model, Resource museum, Property modelProperty, JsonElement value, String lang) {
        if (value instanceof JsonPrimitive && value.getAsString() != null) {
            Literal literal = model.createLiteral(value.getAsString(), lang);
            museum.addProperty(modelProperty, literal);
        }
    }

    public static void addUriProperty(Model model, Resource museum, Property property, JsonElement value) {
        if (value instanceof JsonPrimitive) {
            String link = value.getAsString();
            addUriProperty(model, museum, property, link);
        }
    }

    public static void addUriProperty(Model model, Resource museum, Property property, String link) {
        ArrayList<String> excepted = new ArrayList<>();

        if (UrlValidator.isValid(link, excepted)) {
            Resource uri = model.createResource(link);
            museum.addProperty(property, uri);
        } else {
            System.err.println("\"" + link + "\" is not a valid URL");
            museum.addProperty(property, link);
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
        File.write(sw, FileConst.PATH_OUTPUT_PROPERTIES_FILE, true);

        return subProperty;
    }

    private static String mapProperty(String property) {
        switch (property) {
            case BUILDING_DESCRIPTION:          return NS_DBPEDIA_ONTOLOGY + "construction";
            case BUILDING_LMI_CODE:             return NS_REPO_PROPERTY + "lmiCode";
            case CODE:                          return NS_REPO_PROPERTY + "cimec";
            case COLLECTION_IMPORTANCE:         return NS_REPO_PROPERTY + "importance";
            case COLLECTION_PICTURES:           return NS_DBPEDIA_ONTOLOGY + "picture";
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
            case FOUNDED:                       return NS_DBPEDIA_PROPERTY + "foundingDate";
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
            case PUBLICATIONS_MATERIAL:         return NS_DBPEDIA_ONTOLOGY + "publication";
            case MUSEUM_SUPERVISED_BY:          return NS_REPO_PROPERTY + "supervisedBy";
            case MUSEUM_SUPERVISOR_FOR:         return NS_REPO_PROPERTY + "supervisorFor";
            case TYPE:                          return NS_DBPEDIA_ONTOLOGY + "type";
            case "politicalEntity":             return NS_DBPEDIA_PROPERTY + "polity";
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
}
