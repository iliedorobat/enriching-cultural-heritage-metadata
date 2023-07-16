package ro.webdata.echo.translator.edm.lido.commons;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

public class RDFConceptService {
    public static Resource addConcept(Model model, Resource providedCHO, Property property, String text, String lang, String conceptCategory) {
        Literal literal = model.createLiteral(text, lang);
        return addConcept(model, providedCHO, property, literal, conceptCategory);
    }

    public static Resource addConcept(Model model, Resource providedCHO, Property property, Literal literal, String conceptCategory) {
        Resource concept = RDFConceptService.createConcept(model, literal, conceptCategory);

        if (providedCHO != null) {
            if (literal.getLanguage().equalsIgnoreCase("en") && concept != null)
                providedCHO.addProperty(property, concept);
            // TODO: revisit
            else if (literal.getLanguage().equalsIgnoreCase("en_us") && concept != null)
                providedCHO.addProperty(property, concept);
            else
                providedCHO.addProperty(property, literal);
        }

        return concept;
    }

    public static Resource createConcept(Model model, String concept, String lang, String conceptCategory) {
        Literal literal = model.createLiteral(concept, lang);
        return createConcept(model, literal, conceptCategory);
    }

    public static Resource createConcept(Model model, Literal literal, String conceptCategory) {
        ConceptService.add(literal);
        String uri = ConceptService.getConceptUri(literal.getString());

        if (uri == null) {
            return null;
        }

        if (isDBpediaUri(uri) && !literal.getLanguage().equalsIgnoreCase("en")) {
            return null;
        }
        // TODO: revisit
        else if (isDBpediaUri(uri) && !literal.getLanguage().equalsIgnoreCase("en_us")) {
            return null;
        }

        return model.createResource(uri)
                .addProperty(RDF.type, SKOS.Concept);
    }

    private static boolean isDBpediaUri(String uri) {
        return uri.startsWith("http://dbpedia.org/resource/") || uri.startsWith("https://dbpedia.org/resource/");
    }
}
