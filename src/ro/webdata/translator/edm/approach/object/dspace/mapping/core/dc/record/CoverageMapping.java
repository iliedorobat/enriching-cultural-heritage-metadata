package ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.CoverageRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.object.dspace.commons.PrintMessages;

public class CoverageMapping {
    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                providedCHO.addProperty(DC_11.coverage, value, language);
                break;
            case CoverageRecord.REFINEMENT_SPATIAL:
                providedCHO.addProperty(DCTerms.spatial, value, language);
                break;
            case CoverageRecord.REFINEMENT_TEMPORAL:
                providedCHO.addProperty(DCTerms.temporal, value, language);
                break;
            case CoverageRecord.SCHEME_SPATIAL_DCMI_BOX:
            case CoverageRecord.SCHEME_SPATIAL_DCMI_POINT:
            case CoverageRecord.SCHEME_SPATIAL_ISO_3166:
            case CoverageRecord.SCHEME_SPATIAL_TGN:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DCTerms.spatial, value, language);
                break;
            case CoverageRecord.SCHEME_TEMPORAL_DCMI_PERIOD:
            case CoverageRecord.SCHEME_TEMPORAL_W3C_DTF:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DCTerms.temporal, value, language);
                break;
            default:
                PrintMessages.elementWarning(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
