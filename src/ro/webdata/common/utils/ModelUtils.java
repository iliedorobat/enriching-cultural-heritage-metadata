package ro.webdata.common.utils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.translator.edm.approach.event.lido.common.constants.NSConstants;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.ORE;

public class ModelUtils {
    public static final String SYNTAX_N3 = "N3";
    public static final String SYNTAX_RDF_XML = "RDF/XML";
    public static final String SYNTAX_TURTLE = "turtle";

    public static Model generateModel() {
        return ModelFactory.createDefaultModel()
                .setNsPrefix("dbpedia", NSConstants.NS_DBPEDIA_RESOURCE)
                .setNsPrefix("dc", DC_11.getURI())
                .setNsPrefix("dcterms", DCTerms.getURI())
                .setNsPrefix("edm", EDM.getURI())
                .setNsPrefix("foaf", FOAF.getURI())
                .setNsPrefix("openData", NSConstants.NS_REPO_PROPERTY)
                .setNsPrefix("ore", ORE.getURI())
                .setNsPrefix("skos", SKOS.getURI());
    }
}
