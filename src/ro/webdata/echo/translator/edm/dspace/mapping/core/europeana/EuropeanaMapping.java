package ro.webdata.echo.translator.edm.dspace.mapping.core.europeana;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.ORE;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;
import ro.webdata.echo.translator.edm.dspace.mapping.core.europeana.record.IsShownByMapping;
import ro.webdata.echo.translator.edm.dspace.mapping.core.europeana.record.ProviderMapping;
import ro.webdata.echo.translator.edm.dspace.mapping.core.europeana.record.TypeMapping;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;
import ro.webdata.echo.translator.edm.dspace.mapping.core.europeana.record.IsShownAtMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EuropeanaMapping {
    // TODO: the elements and the qualifiers should be lowercase
    //  suppress the element value to be lowercase (in the parser)
    private static final String ELEMENT_IS_SHOWN_AT = "isShownAt";
    private static final String ELEMENT_IS_SHOWN_BY = "isShownBy";
    private static final String ELEMENT_PROVIDER = "provider";
    private static final String ELEMENT_TYPE= "type";

    public static void mapEntries(
            Model model,
            Resource providedCHO,
            HashMap<String, ArrayList<DcValue>> dcValueMap
    ) {
        Resource aggregation = generateAggregation(model, providedCHO);

        for (Map.Entry<String, ArrayList<DcValue>> entry : dcValueMap.entrySet()) {
            mapEntries(model, providedCHO, aggregation, entry.getValue());
        }

        if (!hasEdmType(dcValueMap)) {
            PrintMessages.missingEdmType(Const.OPERATION_MAPPING, providedCHO);
        }
    }

    private static void mapEntries(
            Model model,
            Resource providedCHO,
            Resource aggregation,
            ArrayList<DcValue> dcValueList
    ) {
        for (DcValue dcValue : dcValueList) {
            String elementName = dcValue.getElement().getValue();

            switch (elementName) {
                case ELEMENT_IS_SHOWN_AT:
                    IsShownAtMapping.mapEntries(model, providedCHO, aggregation, dcValue);
                    break;
                case ELEMENT_IS_SHOWN_BY:
                    IsShownByMapping.mapEntries(model, providedCHO, aggregation, dcValue);
                    break;
                case ELEMENT_PROVIDER:
                    ProviderMapping.mapEntries(model, providedCHO, aggregation, dcValue);
                    break;
                case ELEMENT_TYPE:
                    TypeMapping.mapEntries(model, providedCHO, dcValue);
                    break;
                default:
                    break;
            }
        }
    }

    // TODO: add edm:dataProvider and edm:rights
    private static Resource generateAggregation(Model model, Resource providedCHO) {
        Resource aggregation = null;
        String providedCHOUri = providedCHO.getURI();
        String aggregationUri = URIUtils.prepareUri(providedCHOUri, Namespace.LINK_ID_AGGREGATION);

        aggregation = model
                .createResource(aggregationUri)
                .addProperty(RDF.type, ORE.Aggregation)
                .addProperty(EDM.aggregatedCHO, providedCHO);

        return aggregation;
    }

    private static boolean hasEdmType(HashMap<String, ArrayList<DcValue>> dcValueMap) {
        for (Map.Entry<String, ArrayList<DcValue>> entry : dcValueMap.entrySet()) {
            if (entry.getKey().equals(ELEMENT_TYPE)) {
                return true;
            }
        }

        return false;
    }
}
