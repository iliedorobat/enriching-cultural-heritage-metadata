package ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.objectWorkTypeWrap;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.lido.core.leaf.objectWorkType.ObjectWorkType;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;
import ro.webdata.parser.xml.lido.core.wrap.objectWorkTypeWrap.ObjectWorkTypeWrap;

import java.util.ArrayList;

public class ObjectWorkTypeWrapProcessing {
    /**
     * Add thw work classification to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectWorkTypeWrap
     */
    public static void addObjectWorkTypeWrap(
            Model model, Resource providedCHO, ObjectWorkTypeWrap objectWorkTypeWrap
    ) {
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
    private static void addObjectWorkTypeList(
            Model model, Resource providedCHO, ArrayList<ObjectWorkType> objectWorkTypeList
    ) {
        for (ObjectWorkType objectWorkType : objectWorkTypeList) {
            addObjectWorkTypeTermList(model, providedCHO, objectWorkType);
        }
    }

    /**
     * Add the work classification to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param objectWorkType
     */
    private static void addObjectWorkTypeTermList(Model model, Resource providedCHO, ObjectWorkType objectWorkType
    ) {
        ArrayList<Term> termList = objectWorkType.getTerm();
        for (Term term : termList) {
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
    private static void addObjectWorkTypeTerm(Model model, Resource providedCHO, Term term) {
        String lang = term.getLang().getLang();
        String text = term.getText();

        if (text != null) {
            Literal literal = model.createLiteral(text, lang);
            RDFConceptService.addConcept(model, providedCHO, DC_11.subject, literal, null);
        }
    }
}
