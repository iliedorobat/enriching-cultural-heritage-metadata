package ro.webdata.echo.translator.edm.lido.mapping.leaf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.commons.MuseumUtils;
import ro.webdata.parser.xml.lido.core.complex.legalBodyRefComplexType.LegalBodyRefComplexType;
import ro.webdata.parser.xml.lido.core.leaf.appellationValue.AppellationValue;
import ro.webdata.parser.xml.lido.core.leaf.legalBodyID.LegalBodyID;
import ro.webdata.parser.xml.lido.core.leaf.legalBodyName.LegalBodyName;
import ro.webdata.parser.xml.lido.core.leaf.legalBodyWeblink.LegalBodyWeblink;

import java.util.ArrayList;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_ORGANIZATION;
import static ro.webdata.echo.translator.commons.Env.LANG_MAIN;

public class LegalBodyRefComplexTypeProcessing {
    public static Resource createLegalBodyRef(Model model, LegalBodyRefComplexType legalBodyRefComplexType) {
        Resource organization = null;

        if (legalBodyRefComplexType != null) {
            ArrayList<LegalBodyID> legalBodyIDList = legalBodyRefComplexType.getLegalBodyID();
            ArrayList<LegalBodyName> legalBodyNameList = legalBodyRefComplexType.getLegalBodyName();

            String name = getOrganizationName(legalBodyNameList);
            String resourceLink = NS_REPO_RESOURCE_ORGANIZATION + Text.sanitizeString(name, true);
            String cimecCode = MuseumUtils.getCimecCode(legalBodyIDList, MuseumUtils.enJsonArray);

            if (cimecCode != null) {
                resourceLink = MuseumUtils.generateMuseumId(cimecCode);
            }

            organization = model
                    .createResource(resourceLink)
                    .addProperty(RDF.type, EDM.Agent);
            addOrganizationIdentifier(model, organization, legalBodyIDList);

            if (cimecCode == null) {
                addOrganizationName(model, organization, legalBodyNameList);
            }
        }

        return organization;
    }

    /**
     * Add the organization identifiers (dc:identifier property) extracted from LegalBodyID objects
     * @param model The RDF graph
     * @param organization The organization
     * @param legalBodyIDList The list with <b>LegalBodyID</b> objects
     */
    public static void addOrganizationIdentifier(Model model, Resource organization, ArrayList<LegalBodyID> legalBodyIDList) {
        for (LegalBodyID legalBodyID : legalBodyIDList) {
            IdentifierComplexTypeProcessing.addIdentifier(model, organization, legalBodyID);
        }
    }

    /**
     * Add the organization names
     * @param model The RDF graph
     * @param organization The organization resource
     * @param legalBodyNameList The list with organization <b>LegalBodyName</b> objects
     */
    public static void addOrganizationName(Model model, Resource organization, ArrayList<LegalBodyName> legalBodyNameList) {
        int size = legalBodyNameList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(LegalBodyRefComplexTypeProcessing.class.getName() + ":" +
                        "\nThere has been received " + size + " \"lido:legalBodyName\" objects, " +
                        "but EDM accepts only one organization name object." +
                        "\n--- Only the first organization name will be registered! ---");
            }

            LegalBodyName legalBodyName = legalBodyNameList.get(0);
            ArrayList<AppellationValue> appellationValueList = legalBodyName.getAppellationValue();
            AppellationValue appellationValue = appellationValueList.get(0);

            String label = appellationValue.getLabel().getLabel();
            String lang = appellationValue.getLang().getLang();
            String title = appellationValue.getText();

            if (label != null) {
                Literal literal = model.createLiteral(label, lang);
                organization.addProperty(SKOS.altLabel, literal);
            }

            if (title != null) {
                Literal literal = model.createLiteral(title, lang);
                organization.addProperty(SKOS.prefLabel, literal);
            }
        }
    }

    /**
     * Add the organization identifiers (dc:identifier property) extracted from LegalBodyWeblink objects
     * @param model The RDF graph
     * @param organization The organization
     * @param legalBodyWeblinkList The list with <b>LegalBodyWeblink</b> objects
     */
    public static void addOrganizationWeblink(Model model, Resource organization, ArrayList<LegalBodyWeblink> legalBodyWeblinkList) {
        for (LegalBodyWeblink legalBodyWeblink : legalBodyWeblinkList) {
            organization.addProperty(DC_11.identifier, legalBodyWeblink.getText());
        }
    }

    /**
     * Get the first name occurrence of the Organization
     * @param legalBodyNameList The list with LegalBodyName objects
     * @return the Organization name or <b>null</b>
     */
    public static String getOrganizationName(ArrayList<LegalBodyName> legalBodyNameList) {
        int size = legalBodyNameList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(LegalBodyRefComplexTypeProcessing.class.getName() + ":" +
                        "\nThere has been received " + size + " \"lido:legalBodyName\" objects," +
                        " but EDM accepts only one organization name object." +
                        "\n--- Only the first organization name will be parsed! ---");
            }

            LegalBodyName legalBodyName = legalBodyNameList.get(0);
            ArrayList<AppellationValue> appellationValues = legalBodyName.getAppellationValue();

            AppellationValue appellationValue = getAppellationValue(appellationValues, LANG_MAIN);
            if (appellationValue == null) {
                appellationValue = getAppellationValue(appellationValues, Const.LANG_EN);
            }
            if (appellationValue == null) {
                appellationValue = getAppellationValue(appellationValues, null);
            }
            if (appellationValue != null) {
                return appellationValue.getText();
            }
        }

        return null;
    }

    /**
     * Generate a resource identifier for an organization
     * @param legalBodyNameList The list with <b>LegalBodyName</b> objects
     * @return The organization resource link
     */
    public static String getOrganizationLink(ArrayList<LegalBodyName> legalBodyNameList) {
        int size = legalBodyNameList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(LegalBodyRefComplexTypeProcessing.class.getName() + ":" +
                        "\nThere has been received " + size + " \"lido:legalBodyName\" objects," +
                        " but a Resource can have only one id." +
                        "\n--- Only the first organization id will be parsed! ---");
            }

            String name = getOrganizationName(legalBodyNameList);
            return NS_REPO_RESOURCE_ORGANIZATION + Text.sanitizeString(name, true);
        }

        return null;
    }

    /**
     * Get the first AppellationValue for a specified language
     * @param appellationValues The <b>AppellationValue</b> list
     * @param inputLang the specified language
     * @return the <b>AppellationValue</b> object for the specified language or <b>null</b>
     */
    private static AppellationValue getAppellationValue(ArrayList<AppellationValue> appellationValues, String inputLang) {
        for (AppellationValue appellationValue : appellationValues) {
            String lang = appellationValue.getLang().getLang();

            if (lang != null && lang.equals(inputLang))
                return appellationValue;

            if (lang == null && inputLang == null)
                return appellationValue;
        }

        return null;
    }
}
