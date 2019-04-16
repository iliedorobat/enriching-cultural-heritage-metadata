package ro.webdata.lido.convert.edm.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

//TODO: integrate into EDM from LIDO Parser
public class EDM2 {private static final Model m = ModelFactory.createDefaultModel();
    public static final String NS = "http://www.europeana.eu/schemas/edm/";

    public static final Property acronym;
    public static final Property europeanaRole;
    public static final Property geographicLevel;
    public static final Property organizationDomain;
    public static final Property organizationScope;
    public static final Property organizationSector;

    public EDM2() {}

    public static String getURI() {
        return "http://www.europeana.eu/schemas/edm/";
    }

    static {
        acronym = m.createProperty(NS + "acronym");
        europeanaRole = m.createProperty(NS + "europeanaRole");
        geographicLevel = m.createProperty(NS + "geographicLevel");
        organizationDomain = m.createProperty(NS + "organizationDomain");
        organizationScope = m.createProperty(NS + "organizationScope");
        organizationSector = m.createProperty(NS + "organizationSector");
    }
}
