package ro.webdata.lido.translator.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class EDM_TEST {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String NS = "http://www.europeana.eu/schemas/edm/";

    public static final Resource Agent;
    public static final Resource ProvidedCHO;
    public static final Property currentLocation;
    public static final Property hasType;

    public EDM_TEST() {}

    public static String getURI() {
        return "http://www.europeana.eu/schemas/edm/";
    }

    static {
        Agent = m.createResource(NS + "Agent");
        ProvidedCHO = m.createResource(NS + "ProvidedCHO");
        currentLocation = m.createProperty(NS + "currentLocation");
        hasType = m.createProperty(NS + "hasType");
    }
}
