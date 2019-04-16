package ro.webdata.lido.convert.edm.core.category;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.lido.parser.core.leaf.category.Category;
import ro.webdata.lido.parser.core.leaf.term.Term;

import java.util.ArrayList;

public class CategoryProcessing {
    public void processing(
            Model model,
            Resource providedCHO,
            Category category
    ) {
        addCategories(providedCHO, category.getTerm());
    }

    /**
     * Add the category for every term category of a provided CHO
     * @param providedCHO The RDF CHO
     * @param termList The list with "lido:term" elements
     */
    private void addCategories(Resource providedCHO, ArrayList<Term> termList) {
        for (int i = 0; i < termList.size(); i++){
            addCategory(providedCHO, termList.get(i));
        }
    }

    /**
     * Add the category for a term category of a provided CHO
     * @param providedCHO The RDF CHO
     * @param term The "lido:term" element
     */
    private void addCategory(Resource providedCHO, Term term) {
        String lang = term.getLang().getLang();
        String text = term.getText();
        providedCHO.addProperty(DC_11.type, text, lang);
    }
}
