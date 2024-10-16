package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.graph.GraphResource;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE;

public class ContributorMapping {
    private static final String REFINEMENT_ADVISOR = "advisor";
    private static final String REFINEMENT_AUTHOR = "author";
    private static final String REFINEMENT_EDITOR = "editor";
    private static final String REFINEMENT_ILLUSTRATOR = "illustrator";
    private static final String REFINEMENT_OTHER = "other";

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
            case REFINEMENT_ADVISOR:
            case REFINEMENT_AUTHOR:
            case REFINEMENT_EDITOR:
            case REFINEMENT_ILLUSTRATOR:
            case REFINEMENT_OTHER:
                String relativeUri = GraphResource.generateURI("", EDM.Agent, value, true);
                String contributorUri = URIUtils.prepareUri(NS_REPO_RESOURCE, relativeUri);

                Resource contributor = model
                        .createResource(contributorUri)
                        .addProperty(RDF.type, EDM.Agent)
                        .addProperty(SKOS.prefLabel, value, language);

                if (!qualifier.equals(BasicRecord.EMPTY)
                        && !qualifier.equals(BasicRecord.NONE)
                        && !qualifier.equals(REFINEMENT_OTHER))
                    contributor.addProperty(SKOS.note, qualifier, Const.LANG_EN);

                providedCHO.addProperty(DC_11.contributor, contributor);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
