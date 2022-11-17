package ro.webdata.echo.translator.edm.lido.mapping.core.category;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.lido.core.leaf.category.Category;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;

import java.util.ArrayList;

public class CategoryProcessing {
    public static void mapEntries(
            Model model,
            Resource providedCHO,
            Category category
    ) {
        addCategories(model, providedCHO, category.getTerm());
    }

    /**
     * Add the category for every term category of a provided CHO
     * @param model The RDF graph
     * @param providedCHO The RDF CHO
     * @param termList The list with "lido:term" elements
     */
    private static void addCategories(Model model, Resource providedCHO, ArrayList<Term> termList) {
        for (Term term : termList) {
            addCategory(model, providedCHO, term);
        }
    }

    /**
     * Add the category for a term category of a provided CHO
     * @param model The RDF graph
     * @param providedCHO The RDF CHO
     * @param term The "lido:term" element
     */
    private static void addCategory(Model model, Resource providedCHO, Term term) {
        String lang = term.getLang().getLang();
        String text = term.getText();

        if (text != null) {
            Literal literal = model.createLiteral(text, lang);
            RDFConceptService.addConcept(model, providedCHO, DC_11.type, literal, null);
        }
    }
}
