package ro.webdata.echo.translator.edm.dspace.mapping.core.dc;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record.*;
import ro.webdata.parser.xml.dspace.core.attribute.record.*;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DcMapping {
    public static void mapEntries(Model model, Resource providedCHO, HashMap<String, ArrayList<DcValue>> dcValueMap) {
        for (Map.Entry<String, ArrayList<DcValue>> entry : dcValueMap.entrySet()) {
            mapEntries(model, providedCHO, entry.getValue());
        }
    }

    private static void mapEntries(Model model, Resource providedCHO, ArrayList<DcValue> dcValueList) {
        for (DcValue dcValue : dcValueList) {
            String elementName = dcValue.getElement().getValue();

            switch (elementName) {
                case ContributorRecord.ELEMENT:
                    ContributorMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case CoverageRecord.ELEMENT:
                    CoverageMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case CreatorRecord.ELEMENT:
                    CreatorMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case DateRecord.ELEMENT:
                    DateMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case DescriptionRecord.ELEMENT:
                    DescriptionMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case FormatRecord.ELEMENT:
                    FormatMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case IdentifierRecord.ELEMENT:
                    IdentifierMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case LanguageRecord.ELEMENT:
                    LanguageMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case PublisherRecord.ELEMENT:
                    PublisherMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case RelationRecord.ELEMENT:
                    RelationMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case RightsRecord.ELEMENT:
                    RightsMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case SourceRecord.ELEMENT:
                    SourceMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case SubjectRecord.ELEMENT:
                    SubjectMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case TitleRecord.ELEMENT:
                    TitleMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                case TypeRecord.ELEMENT:
                    TypeMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                default:
                    break;
            }
        }
    }
}
