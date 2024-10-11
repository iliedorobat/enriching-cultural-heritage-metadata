package ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.titleWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.translator.edm.lido.mapping.leaf.AppellationComplexTypeProcessing;
import ro.webdata.parser.xml.lido.core.set.titleSet.TitleSet;
import ro.webdata.parser.xml.lido.core.wrap.titleWrap.TitleWrap;

import java.util.ArrayList;

public class TitleWrapProcessing {
    /**
     * Add the title and labels to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param titleWrap The wrapper for title properties
     */
    public static void addTitleWrap(Model model, Resource providedCHO, TitleWrap titleWrap) {
        if (titleWrap != null) {
            ArrayList<TitleSet> titleSetList = titleWrap.getTitleSet();
            addTitleSet(model, providedCHO, titleSetList);
        }
    }

    /**
     * Add the title and labels to the provided CHO for all the <b>TitleSet</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param titleSetList The list with <b>TitleSet</b> objects
     */
    private static void addTitleSet(Model model, Resource providedCHO, ArrayList<TitleSet> titleSetList) {
        for (TitleSet titleSet : titleSetList) {
            addTitle(model, providedCHO, titleSet);
        }
    }

    /**
     * Add the title and labels to the provided CHO
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param titleSet The <b>TitleSet</b> object
     */
    private static void addTitle(Model model, Resource providedCHO, TitleSet titleSet) {
        AppellationComplexTypeProcessing.addTitleList(model, providedCHO, titleSet);
    }
}
