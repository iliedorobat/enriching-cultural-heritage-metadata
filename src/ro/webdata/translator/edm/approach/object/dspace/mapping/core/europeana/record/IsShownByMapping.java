package ro.webdata.translator.edm.approach.object.dspace.mapping.core.europeana.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.translator.edm.approach.object.dspace.common.PrintMessages;
import ro.webdata.translator.edm.approach.object.dspace.common.constants.EnvConstants;

public class IsShownByMapping {
    public static void processing(Model model, Resource providedCHO, Resource aggregation, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                aggregation.addProperty(EDM.isShownBy, value, language);
                break;
            default:
                PrintMessages.elementWarning(EnvConstants.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
