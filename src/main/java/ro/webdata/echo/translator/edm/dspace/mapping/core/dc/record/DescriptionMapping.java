package ro.webdata.echo.translator.edm.dspace.mapping.core.dc.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.translator.edm.dspace.commons.PrintMessages;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.attribute.record.DescriptionRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

public class DescriptionMapping {
    private static final String REFINEMENT_SPONSORSHIP = "sponsorship";
    private static final String REFINEMENT_STATEMENT_OF_RESPONSIBILITY = "statementofresponsibility";
    private static final String REFINEMENT_VERSION = "version";
    private static final String REFINEMENT_PROVENANCE = "provenance";
    private static final String SCHEME_URI = "uri";

    public static void mapEntries(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
            case DescriptionRecord.REFINEMENT_ABSTRACT:
            case REFINEMENT_SPONSORSHIP:
            case REFINEMENT_STATEMENT_OF_RESPONSIBILITY:
            case REFINEMENT_VERSION:
                providedCHO.addProperty(DC_11.description, value, language);
                break;
            case DescriptionRecord.REFINEMENT_TABLE_OF_CONTENTS:
                providedCHO.addProperty(DCTerms.tableOfContents, value, language);
                break;
            case REFINEMENT_PROVENANCE:
                providedCHO.addProperty(DCTerms.provenance, value, language);
                break;
            case SCHEME_URI:
                value = Text.attachesSchemaToValue(qualifier, value);
                providedCHO.addProperty(DC_11.description, value, language);
                break;
            default:
                PrintMessages.invalidElement(Const.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
