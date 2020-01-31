package ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.dspace.core.attribute.record.*;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.record.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DcMapping {
    public static void processing(Model model, Resource providedCHO, HashMap<String, ArrayList<DcValue>> dcValueMap) {
        for (Map.Entry<String, ArrayList<DcValue>> entry : dcValueMap.entrySet()) {
            dcValueProcessing(model, providedCHO, entry.getValue());
        }
    }

    private static void dcValueProcessing(Model model, Resource providedCHO, ArrayList<DcValue> dcValueList) {
        for (DcValue dcValue : dcValueList) {
            String elementName = dcValue.getElement().getValue();

            switch (elementName) {
                case ContributorRecord.ELEMENT:
                    ContributorMapping.processing(model, providedCHO, dcValue);
                    break;
                case CoverageRecord.ELEMENT:
                    CoverageMapping.processing(model, providedCHO, dcValue);
                    break;
                case CreatorRecord.ELEMENT:
                    CreatorMapping.processing(model, providedCHO, dcValue);
                    break;
                case DateRecord.ELEMENT:
                    DateMapping.processing(model, providedCHO, dcValue);
                    break;
                case DescriptionRecord.ELEMENT:
                    DescriptionMapping.processing(model, providedCHO, dcValue);
                    break;
                case FormatRecord.ELEMENT:
                    FormatMapping.processing(model, providedCHO, dcValue);
                    break;
                case IdentifierRecord.ELEMENT:
                    IdentifierMapping.processing(model, providedCHO, dcValue);
                    break;
                case LanguageRecord.ELEMENT:
                    LanguageMapping.processing(model, providedCHO, dcValue);
                    break;
                case PublisherRecord.ELEMENT:
                    PublisherMapping.processing(model, providedCHO, dcValue);
                    break;
                case RelationRecord.ELEMENT:
                    RelationMapping.processing(model, providedCHO, dcValue);
                    break;
                case RightsRecord.ELEMENT:
                    RightsMapping.processing(model, providedCHO, dcValue);
                    break;
                case SourceRecord.ELEMENT:
                    SourceMapping.processing(model, providedCHO, dcValue);
                    break;
                case SubjectRecord.ELEMENT:
                    SubjectMapping.processing(model, providedCHO, dcValue);
                    break;
                case TitleRecord.ELEMENT:
                    TitleMapping.processing(model, providedCHO, dcValue);
                    break;
                case TypeRecord.ELEMENT:
                    TypeMapping.processing(model, providedCHO, dcValue);
                    break;
                default:
                    break;
            }
        }
    }
}
