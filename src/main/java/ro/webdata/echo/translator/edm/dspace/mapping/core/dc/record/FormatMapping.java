package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.FormatRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

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
                RDFConceptService.addConcept(model, providedCHO, DC_11.format, value, language, null);
                break;
            case FormatRecord.SCHEME_MEDIUM_IMT:
                value = Text.attachesSchemaToValue(qualifier, value);
                RDFConceptService.addConcept(model, providedCHO, DC_11.format, value, language, null);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
