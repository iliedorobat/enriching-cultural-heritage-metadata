package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.culture;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.lido.core.leaf.culture.Culture;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;

import java.util.ArrayList;

public class CultureProcessing {
    public static ArrayList<Literal> getCultureList(Model model, Resource providedCHO, ArrayList<Culture> lidoCultureList) {
        ArrayList<Literal> cultureList = new ArrayList<>();

        for (Culture culture : lidoCultureList) {
            cultureList.addAll(
                    getCultureList(model, culture)
            );
        }

        return cultureList;
    }

    /**
     * Get the culture in different languages for a specified <b>lido:culture</b> object
     * @param model The RDF graph
     * @param culture The <b>lido:culture</b> object
     * @return The culture list for a <b>lido:culture</b> object in different languages
     */
    private static ArrayList<Literal> getCultureList(Model model, Culture culture) {
        ArrayList<Literal> output = new ArrayList<>();
        ArrayList<Term> termList = culture.getTerm();

        for (Term term : termList) {
            String lang = term.getLang().getLang();
            String text = term.getText();

            if (text != null) {
                Literal literal = model.createLiteral(text, lang);
                output.add(literal);
            }
        }

        return output;
    }
}
