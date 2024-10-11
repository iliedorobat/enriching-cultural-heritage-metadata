package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;
import ro.webdata.echo.translator.edm.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.TypeRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

public class TypeMapping {
    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                RDFConceptService.addConcept(model, providedCHO, EDM.hasType, value, language, null);
                break;
            case TypeRecord.SCHEME_DCMI_TYPE_VOCABULARY:
                value = Text.attachesSchemaToValue(qualifier, value);
                RDFConceptService.addConcept(model, providedCHO, EDM.hasType, value, language, null);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
