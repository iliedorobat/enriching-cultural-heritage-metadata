package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.SubjectRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

public class SubjectMapping {
    private static final String REFINEMENT_CLASSIFICATION = "classification";
    private static final String REFINEMENT_OTHER = "other";

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
            case REFINEMENT_OTHER:
                RDFConceptService.addConcept(model, providedCHO, DC_11.subject, value, language, null);
                break;
            case REFINEMENT_CLASSIFICATION:
                RDFConceptService.addConcept(model, providedCHO, EDM.hasType, value, language, null);
                break;
            case SubjectRecord.SCHEME_DDC:
            case SubjectRecord.SCHEME_LCC:
            case SubjectRecord.SCHEME_LCSH:
            case SubjectRecord.SCHEME_MESH:
            case SubjectRecord.SCHEME_UDC:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DC_11.subject, value, language);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
