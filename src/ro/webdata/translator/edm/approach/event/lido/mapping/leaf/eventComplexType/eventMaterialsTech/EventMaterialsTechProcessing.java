package ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventMaterialsTech;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.parser.xml.lido.core.leaf.displayMaterialsTech.DisplayMaterialsTech;
import ro.webdata.parser.xml.lido.core.leaf.eventMaterialsTech.EventMaterialsTech;

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
                        Namespace.NS_REPO_RESOURCE + Text.sanitizeString(Text.toCamelCase(label))
                        + File.FILE_SEPARATOR + Text.sanitizeString(text)
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
