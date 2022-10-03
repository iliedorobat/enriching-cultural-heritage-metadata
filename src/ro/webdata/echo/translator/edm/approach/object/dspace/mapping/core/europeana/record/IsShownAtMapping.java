package ro.webdata.echo.translator.edm.approach.object.dspace.mapping.core.europeana.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.approach.object.dspace.commons.PrintMessages;

public class IsShownAtMapping {
    public static void mapEntries(Model model, Resource providedCHO, Resource aggregation, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                aggregation.addProperty(EDM.isShownAt, value, language);
                break;
            default:
                PrintMessages.elementWarning(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
