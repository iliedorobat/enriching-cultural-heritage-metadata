package ro.webdata.lido.convert.edm.processing.leaf.eventComplexType.eventMaterialsTech;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.convert.edm.common.TextUtils;
import ro.webdata.lido.convert.edm.common.constants.FileConstatnts;
import ro.webdata.lido.convert.edm.common.constants.NSConstants;
import ro.webdata.lido.parser.core.leaf.displayMaterialsTech.DisplayMaterialsTech;
import ro.webdata.lido.parser.core.leaf.eventMaterialsTech.EventMaterialsTech;

import java.util.ArrayList;

public class EventMaterialsTechProcessing {
    public ArrayList<Resource> addEventMaterialsTechList(
            Model model, Resource providedCHO, ArrayList<EventMaterialsTech> eventMaterialsTechList) {
        ArrayList<Resource> output = new ArrayList<>();

        for (int i = 0; i < eventMaterialsTechList.size(); i++) {
            EventMaterialsTech eventMaterialsTech = eventMaterialsTechList.get(i);

            ArrayList<DisplayMaterialsTech> displayMaterialsTechList = eventMaterialsTech.getDisplayMaterialsTech();
            for (int j = 0; j < displayMaterialsTechList.size(); j++) {
                DisplayMaterialsTech displayMaterialsTech = displayMaterialsTechList.get(j);
                String lang = displayMaterialsTech.getLang().getLang();
                String label = displayMaterialsTech.getLabel().getLabel();
                String text = displayMaterialsTech.getText();

                Literal literal = model.createLiteral(text, lang);
                Resource resource = model.createResource(
                        NSConstants.NS_REPO_RESOURCE
                        + FileConstatnts.FILE_SEPARATOR + TextUtils.sanitizeString(TextUtils.toCamelCase(label))
                        + FileConstatnts.FILE_SEPARATOR + TextUtils.sanitizeString(TextUtils.toCamelCase(text))
                );
                resource.addProperty(RDF.type, SKOS.Concept);
                resource.addProperty(SKOS.prefLabel, literal);
                resource.addProperty(SKOS.note, label);

                output.add(resource);
            }
        }

        return output;
    }
}
