package ro.webdata.lido.mapping.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class SVCS {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String NS = "http://rdfs.org/sioc/services#";

    public static final Resource Service;
    public static final Property has_service;
    public static final Property max_results;
    public static final Property results_format;
    public static final Property service_of;
    public static final Property service_definition;
    public static final Property service_endpoint;
    public static final Property service_protocol;

    public static String getURI() {
        return "http://rdfs.org/sioc/services#";
    }

    static {
        Service = m.createResource(NS + "Service");
        has_service = m.createProperty(NS + "has_service");
        max_results = m.createProperty(NS + "max_results");
        results_format = m.createProperty(NS + "results_format");
        service_of = m.createProperty(NS + "service_of");
        service_definition = m.createProperty(NS + "service_definition");
        service_endpoint = m.createProperty(NS + "service_endpoint");
        service_protocol = m.createProperty(NS + "service_protocol");
    }
}
