package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.LanguageRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;

public class LanguageMapping {
    private static final String SCHEME_ISO = "iso";
    private static final String SCHEME_RFC_3066 = "rfc3066";

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                providedCHO.addProperty(DC_11.language, value, language);
                break;
            case LanguageRecord.SCHEME_ISO_639_2:
            case LanguageRecord.SCHEME_RFC_1766:
            case SCHEME_ISO:
            case SCHEME_RFC_3066:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DC_11.language, value, language);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
