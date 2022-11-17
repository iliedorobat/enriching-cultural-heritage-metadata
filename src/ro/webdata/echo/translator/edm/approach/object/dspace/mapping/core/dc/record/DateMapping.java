package ro.webdata.echo.translator.edm.approach.object.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.DateRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.approach.object.dspace.commons.PrintMessages;

public class DateMapping {
    private static final String REFINEMENT_ACCESSIONED = "accessioned";
    private static final String REFINEMENT_COPYRIGHT = "copyright";
    private static final String REFINEMENT_UPDATED = "updated";

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
            case DateRecord.REFINEMENT_AVAILABLE:
            case DateRecord.REFINEMENT_MODIFIED:
            case DateRecord.REFINEMENT_VALID:
            case REFINEMENT_ACCESSIONED:
            case REFINEMENT_COPYRIGHT:
            case REFINEMENT_UPDATED:
                providedCHO.addProperty(DC_11.date, value, language);
                break;
            case DateRecord.REFINEMENT_CREATED:
                providedCHO.addProperty(DCTerms.created, value, language);
                break;
            case DateRecord.REFINEMENT_ISSUED:
                providedCHO.addProperty(DCTerms.issued, value, language);
                break;
            case DateRecord.SCHEME_TEMPORAL_DCMI_PERIOD:
            case DateRecord.SCHEME_TEMPORAL_W3C_DTF:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DC_11.date, value, language);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
