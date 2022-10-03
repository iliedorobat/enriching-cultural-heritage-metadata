package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventMaterialsTech;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.parser.xml.lido.core.leaf.displayMaterialsTech.DisplayMaterialsTech;
import ro.webdata.parser.xml.lido.core.leaf.eventMaterialsTech.EventMaterialsTech;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.URIUtils;

import java.util.ArrayList;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE;

public class EventMaterialsTechProcessing {
    public static ArrayList<Resource> addEventMaterialsTechList(
            Model model, Resource providedCHO, ArrayList<EventMaterialsTech> eventMaterialsTechList
    ) {
        ArrayList<Resource> output = new ArrayList<>();

        for (EventMaterialsTech eventMaterialsTech : eventMaterialsTechList) {
            ArrayList<DisplayMaterialsTech> displayMaterialsTechList = eventMaterialsTech.getDisplayMaterialsTech();

            for (DisplayMaterialsTech displayMaterialsTech : displayMaterialsTechList) {
                String lang = displayMaterialsTech.getLang().getLang();
                String label = displayMaterialsTech.getLabel().getLabel();
                String text = displayMaterialsTech.getText();

                Literal literal = model.createLiteral(text, lang);
                String uri = URIUtils.prepareUri(NS_REPO_RESOURCE, Text.sanitizeString(Text.toCamelCase(label))
                        + File.FILE_SEPARATOR + Text.sanitizeString(text));

                Resource resource = model
                        .createResource(uri)
                        .addProperty(RDF.type, SKOS.Concept)
                        .addProperty(SKOS.prefLabel, literal)
                        .addProperty(SKOS.note, label);

                output.add(resource);
            }
        }

        return output;
    }
}
