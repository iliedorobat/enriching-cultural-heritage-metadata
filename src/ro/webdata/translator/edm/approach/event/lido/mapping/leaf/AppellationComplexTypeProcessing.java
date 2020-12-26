package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.parser.xml.lido.core.complex.appellationComplexType.AppellationComplexType;
import ro.webdata.parser.xml.lido.core.leaf.appellationValue.AppellationValue;

import java.util.ArrayList;
import java.util.HashMap;

public class AppellationComplexTypeProcessing {
    private static final String LABEL = "label";
    private static final String LANGUAGE = "language";
    private static final String PREF = "pref";
    private static final String TEXT = "text";

    /**
     * For all <b>AppellationValue</b> objects related to <b>AppellationComplexType</b> object
     * add the <b>DC.title</b> and the <b>DCTerms.alternative</b> (label) properties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param appellationComplexType <b>AppellationComplexType</b> object
     */
    public void addTitleList(Model model, Resource providedCHO, AppellationComplexType appellationComplexType) {
        ArrayList<HashMap<String, String>> valuesList = getAppellationValuesList(appellationComplexType);
        HashMap<String, String> hashMap;

        for (int i = 0; i < valuesList.size(); i++) {
            hashMap = valuesList.get(i);
            addTitle(model, providedCHO, hashMap);
        }
    }

    /**
     * Map the AppellationValue objects into an array of HashMap's
     * @param appellationComplexType <b>AppellationValue</b> object
     * @return <b>ArrayList< HashMap< String, String > ></b>
     */
    public ArrayList<HashMap<String, String>> getAppellationValuesList(AppellationComplexType appellationComplexType) {
        ArrayList<AppellationValue> appellationValueList = appellationComplexType.getAppellationValue();
        ArrayList<HashMap<String, String>> hashMapList = new ArrayList<>();
        AppellationValue appellationValue;
        HashMap<String, String> hashMap;

        for (int i = 0; i < appellationValueList.size(); i++) {
            appellationValue = appellationValueList.get(i);
            hashMap = new HashMap<>();

            hashMap.put(LABEL, appellationValue.getLabel().getLabel());
            hashMap.put(LANGUAGE, appellationValue.getLang().getLang());
            hashMap.put(PREF, appellationValue.getPref().getPref());
            hashMap.put(TEXT, appellationValue.getText());

            hashMapList.add(hashMap);
        }

        return hashMapList;
    }

    /**
     * Add the <b>DC.title</b> and the <b>DCTerms.alternative</b> (label) properties to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param hashMap A map with title, label and language
     */
    private void addTitle(Model model, Resource providedCHO, HashMap<String, String> hashMap) {
        String label = hashMap.get(LABEL),
                language = hashMap.get(LANGUAGE),
                pref = hashMap.get(PREF),
                text = hashMap.get(TEXT);
        Literal labelLiteral, prefLiteral, titleLiteral;

        if (label != null) {
            labelLiteral = model.createLiteral(label, language);
            if (labelLiteral != null)
                providedCHO.addProperty(DCTerms.alternative, labelLiteral);
        }

        if (pref != null) {
            prefLiteral = model.createLiteral(pref, language);
            if (prefLiteral != null)
                providedCHO.addProperty(DCTerms.alternative, prefLiteral);
        }

        if (text != null) {
            titleLiteral = model.createLiteral(text, language);
            if (titleLiteral != null)
                providedCHO.addProperty(DC_11.title, titleLiteral);
        }
    }
}
