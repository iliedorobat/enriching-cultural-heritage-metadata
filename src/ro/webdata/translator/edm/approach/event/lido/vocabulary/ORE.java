package ro.webdata.translator.edm.approach.event.lido.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class ORE {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String NS = "http://www.openarchives.org/ore/terms/";

    public static final Resource Aggregation;
    public static final Resource AggregatedResource;
    public static final Resource Proxy;
    public static final Resource ResourceMap;
    public static final Property aggregates;
    public static final Property isAggregatedBy;
    public static final Property describes;
    public static final Property isDescribedBy;
    public static final Property lineage;
    public static final Property proxyFor;
    public static final Property proxyIn;
    public static final Property similarTo;

    public static String getURI() {
        return "http://www.openarchives.org/ore/terms/";
    }

    static {
        Aggregation = m.createResource(NS + "Aggregation");
        AggregatedResource = m.createResource(NS + "AggregatedResource");
        Proxy = m.createResource(NS + "Proxy");
        ResourceMap = m.createResource(NS + "ResourceMap");
        aggregates = m.createProperty(NS + "aggregates");
        isAggregatedBy = m.createProperty(NS + "isAggregatedBy");
        describes = m.createProperty(NS + "describes");
        isDescribedBy = m.createProperty(NS + "isDescribedBy");
        lineage = m.createProperty(NS + "lineage");
        proxyFor = m.createProperty(NS + "proxyFor");
        proxyIn = m.createProperty(NS + "proxyIn");
        similarTo = m.createProperty(NS + "similarTo");
    }
}
