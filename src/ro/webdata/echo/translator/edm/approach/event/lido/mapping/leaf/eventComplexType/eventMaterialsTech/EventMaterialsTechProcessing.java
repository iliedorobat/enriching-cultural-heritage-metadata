package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventMaterialsTech;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.lido.core.leaf.displayMaterialsTech.DisplayMaterialsTech;
import ro.webdata.parser.xml.lido.core.leaf.eventMaterialsTech.EventMaterialsTech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EventMaterialsTechProcessing {
    public static ArrayList<Resource> addEventMaterialsTechList(
            Model model, Resource resourceEvent, ArrayList<EventMaterialsTech> eventMaterialsTechList
    ) {
        ArrayList<Resource> output = new ArrayList<>();

        for (EventMaterialsTech eventMaterialsTech : eventMaterialsTechList) {
            ArrayList<DisplayMaterialsTech> displayMaterialsTechList = eventMaterialsTech.getDisplayMaterialsTech();

            for (DisplayMaterialsTech displayMaterialsTech : displayMaterialsTechList) {
                String lang = displayMaterialsTech.getLang().getLang();
                String category = displayMaterialsTech.getLabel().getLabel();
                String text = displayMaterialsTech.getText();

                if (text != null) {
                    addMaterialTechItems(model, resourceEvent, text, lang, category, output);
                }
            }
        }

        return output;
    }

    private static void addMaterialTechItems(Model model, Resource resourceEvent, String text, String lang, String category, ArrayList<Resource> output) {
        for (String value : Text.toList(text, null)) {
            addMaterialTechItem(model, resourceEvent, value, lang, category, output);
        }
    }

    private static void addMaterialTechItem(Model model, Resource resourceEvent, String item, String lang, String category, ArrayList<Resource> output) {
        Literal literal = model.createLiteral(item, lang);
        Resource resource = RDFConceptService.addConcept(model, null, SKOS.prefLabel, literal, category);

        if (resource != null) {
            output.add(resource);
        } else {
            resourceEvent.addProperty(EDM.isRelatedTo, literal);
        }
    }
}
