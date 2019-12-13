package ro.webdata.lido.translator.mapping.core.descriptiveMetadata.objectClassificationWrap.classificationWrap;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.lido.translator.common.Validators;
import ro.webdata.lido.translator.common.constants.EDMConstants;
import ro.webdata.lido.translator.common.constants.LIDOConstants;
import ro.webdata.lido.translator.vocabulary.EDM;
import ro.webdata.parser.xml.lido.core.leaf.classification.Classification;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;
import ro.webdata.parser.xml.lido.core.wrap.classificationWrap.ClassificationWrap;

import java.util.ArrayList;

public class ClassificationProcessing {
    /**
     * Add classification to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param classificationWrap The wrapper for classification properties
     */
    public void addClassificationWrap(Model model, Resource providedCHO, ClassificationWrap classificationWrap) {
        if (classificationWrap != null) {
            ArrayList<Classification> classificationList = classificationWrap.getClassification();
            addClassificationList(model, providedCHO, classificationList);
        }
    }

    /**
     * Add classification to the provided CHO for all the <b>Classification</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param classificationList The list with <b>Classification</b> objects
     */
    private void addClassificationList(
            Model model, Resource providedCHO, ArrayList<Classification> classificationList) {
        for (int i = 0; i < classificationList.size(); i++) {
            addClassificationTermList(model, providedCHO, classificationList.get(i));
        }
    }

    /**
     * Add classification to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param classification <b>Classification</b> object
     */
    private void addClassificationTermList(Model model, Resource providedCHO, Classification classification) {
        ArrayList<Term> termList = classification.getTerm();
        String type = classification.getType().getType();

        for (int i = 0; i < termList.size(); i++) {
            Term term = termList.get(i);
            String lang = term.getLang().getLang();

            if (type != null && type.equals(LIDOConstants.LIDO_TYPE_EUROPEANA_TYPE)) {
                addClassificationTerm(model, providedCHO, term, EDM.type);

                if (type == EDMConstants.EDM_TYPE_TEXT)
                    System.err.println(this.getClass().getName() + ":" +
                            "\nFor the TEXT type must be provided the \"dc:language\" property");
            } else {
                addClassificationTerm(model, providedCHO, term, null);
            }
        }
    }

    /**
     * Add the type property for the following cases:<br/>
     *  - <b>EDM.type</b> if <b>lido:type="europeana:type"</b><br/>
     *  - <b>EDM.hasType</b> if the text is one of the <b>CHOType.SUBJECTS</b><br/>
     *  - <b>DC.type</b> for other cases
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param term
     * @param type
     */
    private void addClassificationTerm(
            Model model, Resource providedCHO, Term term, Property type) {
        String lang = term.getLang().getLang();
        String text = term.getText();
        boolean isEDMType = Validators.isEDMType(text);

        if (type != null && type.equals(EDM.type)) {
            if (isEDMType) {
                Literal textLiteral = model.createLiteral(text);
                providedCHO.addProperty(EDM.type, textLiteral);
            }
        } else {
            Literal textLiteral = model.createLiteral(text, lang);
            boolean isSuperType = Validators.isSubject(text);

            if (isSuperType)
                providedCHO.addProperty(EDM.hasType, textLiteral);
            else
                providedCHO.addProperty(DC_11.subject, textLiteral);
        }
    }
}
