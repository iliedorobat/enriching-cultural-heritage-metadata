package ro.webdata.echo.translator.edm.approach.event.lido.commons;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

public class RDFConceptService {
    public static Resource addConcept(Model model, Resource providedCHO, Property property, String text, String lang) {
        Resource concept = RDFConceptService.createConcept(model, text, lang);

        if (lang.equalsIgnoreCase("en") && concept != null)
            providedCHO.addProperty(property, concept);
        else
            providedCHO.addProperty(property, text, lang);

        return concept;
    }

    public static Resource addConcept(Model model, Resource providedCHO, Property property, Literal literal) {
        Resource concept = RDFConceptService.createConcept(model, literal);

        if (literal.getLanguage().equalsIgnoreCase("en") && concept != null)
            providedCHO.addProperty(property, concept);
        else
            providedCHO.addProperty(property, literal);

        return concept;
    }

    public static Resource createConcept(Model model, String concept, String lang) {
        ConceptService.add(concept);
        String uri = ConceptService.getConceptUri(concept);

        return model.createResource(uri)
                .addProperty(RDF.type, SKOS.Concept)
                .addProperty(SKOS.prefLabel, concept, lang);
    }

    public static Resource createConcept(Model model, Literal literal) {
        ConceptService.add(literal.getString());
        String uri = ConceptService.getConceptUri(literal.getString());

        if (!literal.getLanguage().equalsIgnoreCase("en") || uri == null) {
            return null;
        }

        return model.createResource(uri)
                .addProperty(RDF.type, SKOS.Concept)
                .addProperty(SKOS.prefLabel, literal);
    }
}
