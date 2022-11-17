package ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectClassificationWrap.classificationWrap;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMType;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.echo.translator.edm.lido.commons.constants.CHOType;
import ro.webdata.echo.translator.edm.lido.commons.constants.LIDOType;
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
    public static void addClassificationWrap(Model model, Resource providedCHO, ClassificationWrap classificationWrap) {
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
    private static void addClassificationList(
            Model model, Resource providedCHO, ArrayList<Classification> classificationList
    ) {
        for (Classification classification : classificationList) {
            addClassificationTermList(model, providedCHO, classification);
        }
    }

    /**
     * Add classification to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param classification <b>Classification</b> object
     */
    private static void addClassificationTermList(Model model, Resource providedCHO, Classification classification) {
        ArrayList<Term> termList = classification.getTerm();
        String type = classification.getType().getType();

        for (Term term : termList) {
            String lang = term.getLang().getLang();

            if (type != null && type.equals(LIDOType.EUROPEANA_TYPE)) {
                addClassificationTerm(model, providedCHO, term, EDM.type);

                if (type.equals(EDMType.VALUE_TEXT))
                    System.err.println(ClassificationProcessing.class.getName() + ":" +
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
     */
    private static void addClassificationTerm(
            Model model, Resource providedCHO, Term term, Property type
    ) {
        String lang = term.getLang().getLang();
        String text = term.getText();
        boolean isEDMType = EDMType.contains(text);

        if (text != null) {
            if (type != null && type.equals(EDM.type)) {
                if (isEDMType) {
                    Literal literal = model.createLiteral(text);
                    providedCHO.addProperty(EDM.type, literal);
                }
            } else {
                Literal literal = model.createLiteral(text, lang);
                boolean isSuperType = CHOType.isSubject(text, lang);
                Property property = isSuperType
                        ? EDM.hasType
                        : DC_11.subject;
                RDFConceptService.addConcept(model, providedCHO, property, literal, null);
            }
        }
    }
}
