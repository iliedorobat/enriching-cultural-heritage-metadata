package ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.graph.GraphResource;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

public class CreatorMapping {
    public static void processing(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        String creatorUri = GraphResource.generateURI(
                Namespace.NS_REPO_RESOURCE, EDM.Agent, value
        );
        Resource contributor = model
                .createResource(creatorUri)
                .addProperty(RDF.type, EDM.Agent)
                .addProperty(SKOS.prefLabel, value, language);

        if (!qualifier.equals(BasicRecord.EMPTY) && !qualifier.equals(BasicRecord.NONE))
            contributor.addProperty(SKOS.note, qualifier, language);

        providedCHO.addProperty(DC_11.creator, contributor);
    }
}
