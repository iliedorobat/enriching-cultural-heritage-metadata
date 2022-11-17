package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventMaterialsTech;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.RDFConceptService;
import ro.webdata.parser.xml.lido.core.leaf.displayMaterialsTech.DisplayMaterialsTech;
import ro.webdata.parser.xml.lido.core.leaf.eventMaterialsTech.EventMaterialsTech;

import java.util.ArrayList;

public class EventMaterialsTechProcessing {
    public static void addEventMaterialsTechList(
            Model model, Resource resourceEvent, Property property, ArrayList<EventMaterialsTech> eventMaterialsTechList
    ) {
        for (EventMaterialsTech eventMaterialsTech : eventMaterialsTechList) {
            ArrayList<DisplayMaterialsTech> displayMaterialsTechList = eventMaterialsTech.getDisplayMaterialsTech();

            for (DisplayMaterialsTech displayMaterialsTech : displayMaterialsTechList) {
                String lang = displayMaterialsTech.getLang().getLang();
                String category = displayMaterialsTech.getLabel().getLabel();
                String text = displayMaterialsTech.getText();

                if (text != null) {
                    addMaterialTechItems(model, resourceEvent, property, text, lang, category);
                }
            }
        }
    }

    private static void addMaterialTechItems(Model model, Resource resourceEvent, Property property, String text, String lang, String category) {
        for (String value : Text.toList(text, null)) {
            addMaterialTechItem(model, resourceEvent, property, value, lang, category);
        }
    }

    private static void addMaterialTechItem(Model model, Resource resourceEvent, Property property, String item, String lang, String category) {
        Literal literal = model.createLiteral(item, lang);
        Resource resource = RDFConceptService.addConcept(model, null, SKOS.prefLabel, literal, category);

        if (resource != null) {
            resourceEvent.addProperty(property, resource);
        } else {
            resourceEvent.addProperty(property, literal);
        }
    }
}
