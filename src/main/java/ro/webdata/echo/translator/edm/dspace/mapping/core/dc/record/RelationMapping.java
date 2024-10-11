package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.dspace.core.attribute.record.RelationRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

public class RelationMapping {
    private static final String REFINEMENT_IS_BASED_ON = "isbasedon";
    private static final String REFINEMENT_IS_PART_OF_SERIES = "ispartofseries";

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case RelationRecord.REFINEMENT_HAS_FORMAT:
                RDFConceptService.addConcept(model, providedCHO, DCTerms.hasFormat, value, language, null);
                break;
            case RelationRecord.REFINEMENT_HAS_PART:
                providedCHO.addProperty(DCTerms.hasPart, value, language);
                break;
            case RelationRecord.REFINEMENT_HAS_VERSION:
                providedCHO.addProperty(DCTerms.hasVersion, value, language);
                break;
            case RelationRecord.REFINEMENT_IS_FORMAT_OF:
                providedCHO.addProperty(DCTerms.isFormatOf, value, language);
                break;
            case RelationRecord.REFINEMENT_IS_PART_OF:
                providedCHO.addProperty(DCTerms.isPartOf, value, language);
                break;
            case RelationRecord.REFINEMENT_IS_REFERENCED_BY:
                providedCHO.addProperty(DCTerms.isReferencedBy, value, language);
                break;
            case RelationRecord.REFINEMENT_IS_REPLACED_BY:
                providedCHO.addProperty(DCTerms.isReplacedBy, value, language);
                break;
            case RelationRecord.REFINEMENT_IS_REQUIRED_BY:
                providedCHO.addProperty(DCTerms.isRequiredBy, value, language);
                break;
            case RelationRecord.REFINEMENT_IS_VERSION_OF:
                providedCHO.addProperty(DCTerms.isVersionOf, value, language);
                break;
            case RelationRecord.REFINEMENT_REFERENCES:
                providedCHO.addProperty(DCTerms.references, value, language);
                break;
            case RelationRecord.REFINEMENT_REPLACES:
                providedCHO.addProperty(DCTerms.replaces, value, language);
                break;
            case RelationRecord.REFINEMENT_REQUIRES:
                providedCHO.addProperty(DCTerms.requires, value, language);
                break;
            case RelationRecord.SCHEME_URI:
                providedCHO.addProperty(DC_11.description, value, language);
                break;
            case REFINEMENT_IS_BASED_ON:
                providedCHO.addProperty(EDM.isDerivativeOf, value, language);
                break;
            case REFINEMENT_IS_PART_OF_SERIES:
                providedCHO.addProperty(EDM.isNextInSequence, value, language);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
