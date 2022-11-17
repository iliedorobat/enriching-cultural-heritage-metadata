package ro.webdata.echo.translator.edm.lido.commons;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.translator.commons.PropertyUtils;

import static ro.webdata.echo.commons.graph.Namespace.NS_DBPEDIA_RESOURCE;

public final class ResourceUtils {
    /**
     *
     * @param model The RDF graph
     * @param resource The provider, dataProvider or intermediateProvider
     * @param property RDF graph property
     * @param label Mapping label
     */
    public static void addUriProperty(Model model, Resource resource, Property property, String label) {
        String uri = getUri(label);
        PropertyUtils.addUriProperty(model, resource, property, uri);
    }

    public static String getUri(String label) {
        switch (label) {
            case "country":             return NS_DBPEDIA_RESOURCE + "Country";
            case "cultural heritage":   return NS_DBPEDIA_RESOURCE + "Cultural_heritage";
            case "culture":             return NS_DBPEDIA_RESOURCE + "Culture";
            case "education":           return NS_DBPEDIA_RESOURCE + "Education";
            case "higher education":    return NS_DBPEDIA_RESOURCE + "Higher_education";
            case "public institution":  return NS_DBPEDIA_RESOURCE + "Government_agency";
            case "Romania":             return NS_DBPEDIA_RESOURCE + "Romania";
            default:                    return label;
        }
    }
}
