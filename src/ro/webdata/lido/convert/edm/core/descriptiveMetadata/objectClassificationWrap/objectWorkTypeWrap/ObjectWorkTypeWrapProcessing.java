package ro.webdata.lido.convert.edm.core.descriptiveMetadata.objectClassificationWrap.objectWorkTypeWrap;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.lido.convert.edm.common.PropertyUtils;
import ro.webdata.lido.convert.edm.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.objectWorkType.ObjectWorkType;
import ro.webdata.lido.parser.core.leaf.term.Term;
import ro.webdata.lido.parser.core.wrap.objectWorkTypeWrap.ObjectWorkTypeWrap;

import java.util.ArrayList;

public class ObjectWorkTypeWrapProcessing {
    /**
     * Add thw work classification to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectWorkTypeWrap
     */
    public void addObjectWorkTypeWrap(
            Model model, Resource providedCHO, ObjectWorkTypeWrap objectWorkTypeWrap) {
        if (objectWorkTypeWrap != null) {
            ArrayList<ObjectWorkType> objectWorkTypeList = objectWorkTypeWrap.getObjectWorkType();
            addObjectWorkTypeList(model, providedCHO, objectWorkTypeList);
        }
    }

    /**
     * Add thw work classification to the provided CHO for all the <b>ObjectWorkType</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectWorkTypeList
     */
    private void addObjectWorkTypeList(
            Model model, Resource providedCHO, ArrayList<ObjectWorkType> objectWorkTypeList) {
            for (int i = 0; i < objectWorkTypeList.size(); i++) {
                addObjectWorkTypeTermList(model, providedCHO, objectWorkTypeList.get(i));
            }
    }

    /**
     * Add the work classification to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectWorkType
     */
    private void addObjectWorkTypeTermList(Model model, Resource providedCHO, ObjectWorkType objectWorkType) {
        ArrayList<Term> termList = objectWorkType.getTerm();
        for (int i = 0; i < termList.size(); i++) {
            Term term = termList.get(i);
            addObjectWorkTypeTerm(model, providedCHO, term);
        }
    }

    /**
     * Add the <b>edm:isRelatedTo</b> property for the case of
     * <b>lido:objectWorkType</b> terms
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param term
     */
    private void addObjectWorkTypeTerm(Model model, Resource providedCHO, Term term) {
        String lang = term.getLang().getLang();
        String text = term.getText();

        if (text != null) {
            Literal textLiteral = model.createLiteral(text, lang);
            providedCHO.addProperty(DC_11.subject, textLiteral);
        }
    }
}
