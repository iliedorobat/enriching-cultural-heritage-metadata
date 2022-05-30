package ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.FormatRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.object.dspace.commons.PrintMessages;

public class FormatMapping {
    private static final String REFINEMENT_MIMETYPE = "mimetype";

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
            case FormatRecord.REFINEMENT_EXTENT:
            case FormatRecord.REFINEMENT_MEDIUM:
            case REFINEMENT_MIMETYPE:
                providedCHO.addProperty(DC_11.format, value, language);
                break;
            case FormatRecord.SCHEME_MEDIUM_IMT:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DC_11.format, value, language);
                break;
            default:
                PrintMessages.elementWarning(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
