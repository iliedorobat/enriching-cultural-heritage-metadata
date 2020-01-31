package ro.webdata.translator.edm.approach.object.dspace.mapping.core.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.DateRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.object.dspace.common.PrintMessages;
import ro.webdata.translator.edm.approach.object.dspace.common.constants.EnvConstants;

public class DateMapping {
    private static final String REFINEMENT_ACCESSIONED = "accessioned";
    private static final String REFINEMENT_COPYRIGHT = "copyright";
    private static final String REFINEMENT_UPDATED = "updated";

    public static void processing(Model model, Resource providedCHO, DcValue dcValue) {
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
                providedCHO.addProperty(DC_11.date, value);
                break;
            case DateRecord.REFINEMENT_CREATED:
                providedCHO.addProperty(DCTerms.created, value);
                break;
            case DateRecord.REFINEMENT_ISSUED:
                providedCHO.addProperty(DCTerms.issued, value);
                break;
            default:
                PrintMessages.elementWarning(EnvConstants.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
