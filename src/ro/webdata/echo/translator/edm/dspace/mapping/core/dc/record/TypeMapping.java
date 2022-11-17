package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.TypeRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;

public class TypeMapping {
    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                providedCHO.addProperty(EDM.hasType, value, language);
                break;
            case TypeRecord.SCHEME_DCMI_TYPE_VOCABULARY:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(EDM.hasType, value, language);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
