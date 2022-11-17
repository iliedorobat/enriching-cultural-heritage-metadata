package ro.webdata.echo.translator.edm.approach.object.dspace.mapping.core.europeana.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMType;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.approach.object.dspace.commons.PrintMessages;

public class TypeMapping {
    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        if (!EDMType.VALUES.contains(value)) {
            PrintMessages.invalidEdmType(Const.OPERATION_MAPPING, providedCHO, value);
        }

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                providedCHO.addProperty(EDM.type, value, Const.LANG_EN);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
