package ro.webdata.translator.edm.approach.event.lido.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

//TODO: add all the resources and properties
public class ODRL {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String NS = "http://www.w3.org/ns/odrl/2/";

    public static final Property inheritFrom;

    public static String getURI() {
        return "http://www.w3.org/ns/odrl/2/";
    }

    static {
        inheritFrom = m.createProperty(NS + "inheritFrom");
    }
}
