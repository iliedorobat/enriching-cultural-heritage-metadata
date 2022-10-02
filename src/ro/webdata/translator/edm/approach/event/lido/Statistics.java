package ro.webdata.translator.edm.approach.event.lido;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.ORE;
import ro.webdata.translator.commons.FileConst;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @deprecated Needs to be updated...
 */
public class Statistics {
    private static final String FINDING_EVENT = "http://opendata.cs.pub.ro/resource/event/finding";
    private static final String PRODUCTION_EVENT = "http://opendata.cs.pub.ro/resource/event/production";
    private static final String COLLECTING_EVENT = "http://opendata.cs.pub.ro/resource/event/collecting";

    private static final String AUTHOR_AUTHOR = "http://opendata.cs.pub.ro/resource/agent/author";
    private static final String AUTHOR_PRODUCES = "http://opendata.cs.pub.ro/resource/agent/producer";
    private static final String AUTHOR_CREATOR = "http://opendata.cs.pub.ro/resource/agent/creator";
    private static final String AUTHOR_ISSUER = "http://opendata.cs.pub.ro/resource/agent/issuer";
    private static final String DISCOVERER = "http://opendata.cs.pub.ro/resource/agent/discoverer";
    private static final String COLLECTOR = "http://opendata.cs.pub.ro/resource/agent/collector";
    private static final String PAYER_PAYMASTER = "http://opendata.cs.pub.ro/resource/agent/paymaster";
    private static final String PAYER_MONEYER = "http://opendata.cs.pub.ro/resource/agent/moneyer";
    private static final String ENGRAVER = "http://opendata.cs.pub.ro/resource/agent/engraver";

    public static void main(String[] args) {
        System.out.println(FileConst.PATH_OUTPUT_DATASET_FILE);
        Model model = ModelFactory.createDefaultModel();
        model.read("files/output/dataset.rdf");

        ResIterator iterator = model.listSubjects();
        int heritageObjects = 0;
        int organizations = 0;
        int webResources = 0;
        int aggregations = 0;
        int concepts = 0;
        int places = 0;

        int agents = 0;
        int authors = 0;
        int discoverers = 0;
        int collectors = 0;
        int payers = 0;
        int engravers = 0;

        int events = 0;
        int timeSpans = 0;
        int findingEvent = 0;
        int productionEvent = 0;
        int collectingEvent = 0;

        while (iterator.hasNext()) {
            RDFNode node = iterator.next();

            if (node.isResource()) {
                Statement statement = node.asResource().getProperty(RDF.type);

                if (statement != null) {
                    String objectUri = statement.getObject().asResource().getURI();

                    if (objectUri.equals(EDM.ProvidedCHO.getURI()))
                        heritageObjects++;

                    if (objectUri.equals(EDM.Agent.getURI())) {
                        String subjectUri = statement.getSubject().getURI();

                        if (subjectUri.indexOf(AUTHOR_AUTHOR) != -1 ||
                                subjectUri.indexOf(AUTHOR_PRODUCES) != -1 ||
                                subjectUri.indexOf(AUTHOR_CREATOR) != -1 ||
                                subjectUri.indexOf(AUTHOR_ISSUER) != -1
                        ) {
                            authors++;
                        }

                        if (subjectUri.indexOf(DISCOVERER) != -1) {
                            discoverers++;
                        }

                        if (subjectUri.indexOf(COLLECTOR) != -1) {
                            collectors++;
                        }

                        if (subjectUri.indexOf(PAYER_MONEYER) != -1 ||
                                subjectUri.indexOf(PAYER_PAYMASTER) != -1) {
                            payers++;
                        }

                        if (subjectUri.indexOf(ENGRAVER) != -1) {
                            engravers++;
                        }

                        agents++;
                    }

                    if (objectUri.equals(FOAF.Organization.getURI()))
                        organizations++;

                    if (objectUri.equals(EDM.Event.getURI())) {
                        String subjectUri = statement.getSubject().getURI();

                        if (subjectUri.indexOf(FINDING_EVENT) != -1)
                            findingEvent++;
                        if (subjectUri.indexOf(PRODUCTION_EVENT) != -1)
                            productionEvent++;
                        if (subjectUri.indexOf(COLLECTING_EVENT) != -1)
                            collectingEvent++;

                        events++;
                    }

                    if (objectUri.equals(EDM.TimeSpan.getURI()))
                        timeSpans++;

                    if (objectUri.equals(EDM.WebResource.getURI()))
                        webResources++;

                    if (objectUri.equals(ORE.Aggregation.getURI()))
                        aggregations++;

                    if (objectUri.equals(SKOS.Concept.getURI()))
                        concepts++;

                    if (objectUri.equals(EDM.Place.getURI()))
                        places++;
                }
            }
        }

        System.out.println("CHO: " + heritageObjects
                + "\nPeople: " + agents
                + "\nOrganizations: " + organizations
                + "\nEvents: " + events
                + "\nTimeSpans: " + timeSpans
                + "\nWeb representations: " + webResources
                + "\nAggregations: " + aggregations
                + "\nPlaces: " + places
                + "\nConcepts: " + concepts
        );

        System.out.println("\nAuthors: " + authors
                + "\nDiscoverers: " + discoverers
                + "\nCollectors: " + collectors
                + "\nPayers: " + payers
                + "\nEngraver: " + engravers
        );

        System.out.println("\nFinding Events: " + findingEvent
                + "\nProduction Events: " + productionEvent
                + "\nCollecting Events: " + collectingEvent
        );
    }

    private static void printResourceTypes(Set<String> eventTypes) {
        Iterator setIterator = eventTypes.iterator();
        while (setIterator.hasNext()) {
            System.out.println(setIterator.next());
        }
    }

    /**
     *
     * @param iterator
     * @param resourceType EDM.Event or EDM.Agent
     * @return
     */
    private static Set<String> getResourceTypes(ResIterator iterator, Resource resourceType) {
        Set<String> resourceTypes = new HashSet();

        while (iterator.hasNext()) {
            RDFNode node = iterator.next();

            if (node.isResource()) {
                Statement statement = node.asResource().getProperty(RDF.type);

                if (statement != null) {
                    String objectUri = statement.getObject().asResource().getURI();

                    if (objectUri.equals(resourceType.getURI())) {
                        String subjectUri = statement.getSubject().getURI();
                        int index = subjectUri.lastIndexOf("/");
                        String mainUri = subjectUri.substring(0, index);
                        resourceTypes.add(mainUri);
                        System.out.println(subjectUri);
                    }
                }
            }
        }

        return resourceTypes;
    }
}
