package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.IdentifierRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;

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

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
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
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DC_11.identifier, value, language);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
