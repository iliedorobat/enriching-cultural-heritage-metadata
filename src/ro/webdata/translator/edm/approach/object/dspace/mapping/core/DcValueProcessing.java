package ro.webdata.translator.edm.approach.object.dspace.mapping.core;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.dspace.core.attribute.record.*;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.object.dspace.mapping.core.record.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DcValueProcessing {
    public static void processing(Model model, Resource providedCHO, HashMap<String, ArrayList<DcValue>> dcValueMap) {
        for (Map.Entry<String, ArrayList<DcValue>> entry : dcValueMap.entrySet()) {
            addCHOProperties(model, providedCHO, entry.getValue());
        }
    }

    private static void addCHOProperties(Model model, Resource providedCHO, ArrayList<DcValue> dcValueList) {
        for (DcValue dcValue : dcValueList) {
            String elementName = dcValue.getElement().getValue();

            switch (elementName) {
                case ContributorRecord.ELEMENT:
                    ContributorMapping.processing(model, providedCHO, dcValue);
                    break;
                //TODO: case CoverageRecord.ELEMENT: break;
                case CreatorRecord.ELEMENT:
                    CreatorMapping.processing(model, providedCHO, dcValue);
                    break;
                case DateRecord.ELEMENT:
                    DateMapping.processing(model, providedCHO, dcValue);
                    break;
                case DescriptionRecord.ELEMENT:
                    DescriptionMapping.processing(model, providedCHO, dcValue);
                    break;
                //TODO: case FormatRecord.ELEMENT: break; // EDM.type => EDMConstants.EDM_TYPES
                //TODO: case IdentifierRecord.ELEMENT: break;
                case LanguageRecord.ELEMENT:
                    LanguageMapping.processing(model, providedCHO, dcValue);
                    break;
                case PublisherRecord.ELEMENT:
                    PublisherMapping.processing(model, providedCHO, dcValue);
                    break;
                //TODO: case RelationRecord.ELEMENT: break;
                case RightsRecord.ELEMENT:
                    RightsMapping.processing(model, providedCHO, dcValue);
                    break;
                case SourceRecord.ELEMENT:
                    SourceMapping.processing(model, providedCHO, dcValue);
                    break;
                case SubjectRecord.ELEMENT:
                    SubjectMapping.processing(model, providedCHO, dcValue);
                    break;
                //TODO: case ThumbRecord.ELEMENT: break;
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
