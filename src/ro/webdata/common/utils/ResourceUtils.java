package ro.webdata.common.utils;

import org.apache.jena.rdf.model.Resource;
import ro.webdata.common.constants.FileConstants;
import ro.webdata.common.constants.TextUtils;
import ro.webdata.translator.edm.approach.event.lido.common.constants.NSConstants;

public class ResourceUtils {
    /**
     * Generate an URI based on the namespace
     * @param namespace The namespace (see NSConstants)
     * @param resourceName The name of the resource (a country, a name, a concept etc.)
     * @return The generated URI
     */
    public static String generateURI(String namespace, String resourceName) {
        return namespace + FileConstants.FILE_SEPARATOR + TextUtils.sanitizeString(resourceName);
    }

    /**
     * Generate an URI
     * @param namespace The namespace (E.g.: NSConstants.SIMPLE_NS_REPO_RESOURCE)
     * @param resource The resource (E.g.: EDM.Agent)
     * @param resourceName The name of the resource (E.g.: Ilie, Romania, etc.)
     * @return The generated URI
     */
    public static String generateURI(String namespace, Resource resource, String resourceName) {
        return namespace + FileConstants.FILE_SEPARATOR
                + resource.getLocalName() + FileConstants.FILE_SEPARATOR
                + TextUtils.sanitizeString(resourceName);
    }

    /**
     * Generate an encoded URI using the DBPedia namespace
     * @param resourceName The name of resource (a country, a name, a concept etc.)
     * @return The DBPedia encoded URI
     */
    public static String generateDBPediaURI(String resourceName) {
        return NSConstants.NS_DBPEDIA_PAGE
                + FileConstants.FILE_SEPARATOR
                + TextUtils.encodeURI(resourceName);
    }
}
