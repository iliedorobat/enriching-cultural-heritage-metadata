package ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.common.constants.TextUtils;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.IdentifierRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.object.dspace.common.PrintMessages;
import ro.webdata.translator.edm.approach.object.dspace.common.constants.EnvConstants;

public class IdentifierMapping {
    private static final String REFINEMENT_CITATION = "citation";
    private static final String REFINEMENT_GOVDOC = "govdoc";
    private static final String REFINEMENT_OTHER = "other";
    private static final String SCHEME_ISBN = "isbn";
    private static final String SCHEME_ISMN = "ismn";
    private static final String SCHEME_ISSN = "issn";
    private static final String SCHEME_SICI = "sici";
    private static final String SCHEME_SLUG = "slug";
    private static final String SCHEME_T100 = "t100";

    public static void processing(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
            case IdentifierRecord.SCHEME_URI:
            case REFINEMENT_CITATION:
            case REFINEMENT_GOVDOC:
            case REFINEMENT_OTHER:
                providedCHO.addProperty(DC_11.identifier, value, language);
                break;
            case SCHEME_ISBN:
            case SCHEME_ISMN:
            case SCHEME_ISSN:
            case SCHEME_SICI:
            case SCHEME_SLUG:
            case SCHEME_T100:
                value = TextUtils.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DC_11.identifier, value, language);
                break;
            default:
                PrintMessages.elementWarning(EnvConstants.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
