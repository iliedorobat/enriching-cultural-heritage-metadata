package ro.webdata.lido.convert.edm.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class EDM {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String NS = "http://www.europeana.eu/schemas/edm/";

    public static final Resource Agent;
    public static final Resource EuropeanaAggregation;
    public static final Resource EuropeanaObject;
    public static final Resource Event;
    public static final Resource InformationResource;
    public static final Resource NonInformationResource;
    public static final Resource PhysicalThing;
    public static final Resource Place;
    public static final Resource ProvidedCHO;
    public static final Resource TimeSpan;
    public static final Resource WebResource;

    public static final Property aggregatedCHO;
    public static final Property begin;
    /**
     * This property is deprecated and <b>edm:datasetName</b> should be used instead
     * @deprecated
     */
    @Deprecated
    public static final Property collectionName;
    public static final Property country;
    public static final Property currentLocation;
    public static final Property datasetName;
    public static final Property dataProvider;
    public static final Property end;
    public static final Property intermediateProvider;
    public static final Property europeanaProxy;
    public static final Property happenedAt;
    public static final Property hasMet;
    public static final Property hasType;
    public static final Property hasView;
    public static final Property incorporates;
    /**
     * This property is deprecated and replaced by an implementation of the
     * <b>Web Annotation Data Model</b> to support annotations in Europeana
     * @deprecated
     */
    @Deprecated
    public static final Property isAnnotationOf;
    public static final Property isDerivativeOf;
    public static final Property isNextInSequence;
    public static final Property isRelatedTo;
    public static final Property isRepresentationOf;
    public static final Property isShownAt;
    public static final Property isShownBy;
    public static final Property isSimilarTo;
    public static final Property isSuccessorOf;
    public static final Property landingPage;
    public static final Property language;
    public static final Property object;
    public static final Property occurredAt;
    public static final Property preview;
    public static final Property provider;
    public static final Property realizes;
    public static final Property rights;
    public static final Property type;
    public static final Property ugc;
    /**
     * This property is integrated only for backward compatibility with ESE
     * and should not be used
     * @deprecated
     */
    @Deprecated
    public static final Property unstored;
    /**
     * This property is integrated in EDM only for backward compatibility with ESE
     * @deprecated
     */
    @Deprecated
    public static final Property uri;
    /**
     * This property is deprecated and replaced by an implementation of the
     * <b>Web Annotation Data Model</b> to support annotations in Europeana
     * @deprecated
     */
    @Deprecated
    public static final Property userTag;
    public static final Property wasPresentAt;
    public static final Property year;

    public EDM() {}

    public static String getURI() {
        return "http://www.europeana.eu/schemas/edm/";
    }

    static {
        Agent = m.createResource(NS + "Agent");
        EuropeanaAggregation = m.createResource(NS + "EuropeanaAggregation");
        EuropeanaObject = m.createResource(NS + "EuropeanaObject");
        Event = m.createResource(NS + "Event");
        InformationResource = m.createResource(NS + "InformationResource");
        NonInformationResource = m.createResource(NS + "NonInformationResource");
        PhysicalThing = m.createResource(NS + "PhysicalThing");
        Place = m.createResource(NS + "Place");
        ProvidedCHO = m.createResource(NS + "ProvidedCHO");
        TimeSpan = m.createResource(NS + "TimeSpan");
        WebResource = m.createResource(NS + "WebResource");
        aggregatedCHO = m.createProperty(NS + "aggregatedCHO");
        begin = m.createProperty(NS + "begin");
        collectionName = m.createProperty(NS + "collectionName");
        country = m.createProperty(NS + "country");
        currentLocation = m.createProperty(NS + "currentLocation");
        datasetName = m.createProperty(NS + "datasetName");
        dataProvider = m.createProperty(NS + "dataProvider");
        end = m.createProperty(NS + "end");
        intermediateProvider = m.createProperty(NS + "intermediateProvider");
        europeanaProxy = m.createProperty(NS + "europeanaProxy");
        happenedAt = m.createProperty(NS + "happenedAt");
        hasMet = m.createProperty(NS + "hasMet");
        hasType = m.createProperty(NS + "hasType");
        hasView = m.createProperty(NS + "hasView");
        incorporates = m.createProperty(NS + "incorporates");
        isAnnotationOf = m.createProperty(NS + "isAnnotationOf");
        isDerivativeOf = m.createProperty(NS + "isDerivativeOf");
        isNextInSequence = m.createProperty(NS + "isNextInSequence");
        isRelatedTo = m.createProperty(NS + "isRelatedTo");
        isRepresentationOf = m.createProperty(NS + "isRepresentationOf");
        isShownAt = m.createProperty(NS + "isShownAt");
        isShownBy = m.createProperty(NS + "isShownBy");
        isSimilarTo = m.createProperty(NS + "isSimilarTo");
        isSuccessorOf = m.createProperty(NS + "isSuccessorOf");
        landingPage = m.createProperty(NS + "landingPage");
        language = m.createProperty(NS + "language");
        object = m.createProperty(NS + "object");
        //TODO: occurredAt or occuredAt:
        // https://github.com/europeana/corelib/wiki/EDMObjectTemplatesProviders#edmEvent
        occurredAt = m.createProperty(NS + "occurredAt");
        preview = m.createProperty(NS + "preview");
        provider = m.createProperty(NS + "provider");
        realizes = m.createProperty(NS + "realizes");
        rights = m.createProperty(NS + "rights");
        type = m.createProperty(NS + "type");
        ugc = m.createProperty(NS + "ugc");
        unstored = m.createProperty(NS + "unstored");
        uri = m.createProperty(NS + "uri");
        userTag = m.createProperty(NS + "userTag");
        wasPresentAt = m.createProperty(NS + "wasPresentAt");
        year = m.createProperty(NS + "year");
    }
}
