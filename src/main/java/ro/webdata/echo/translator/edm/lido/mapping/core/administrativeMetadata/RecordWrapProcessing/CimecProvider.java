package ro.webdata.echo.translator.edm.lido.mapping.core.administrativeMetadata.RecordWrapProcessing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMRoles;
import ro.webdata.echo.translator.commons.PropertyUtils;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_ORGANIZATION;
import static ro.webdata.echo.translator.edm.lido.commons.ResourceUtils.addUriProperty;

/**
 * Generate the Europeana Provider (the aggregator which Europeana harvests for data)
 * One of the following: https://pro.europeana.eu/page/aggregators
 */
public class CimecProvider {
    private static final String CIMEC_LINK = "http://www.cimec.ro";

    public static Resource generateOrganization(Model model) {
        String relativeUri = PlaceType.COUNTRY + ":Romania"
                + Namespace.URL_SEPARATOR
                + Text.sanitizeString("Institutul Național al Patrimoniului", true);
        String providerLink = URIUtils.prepareUri(NS_REPO_RESOURCE_ORGANIZATION, relativeUri);

        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        addUriProperty(model, provider, DC_11.identifier, CIMEC_LINK);
        addUriProperty(model, provider, FOAF.homepage, CIMEC_LINK);
        addUriProperty(model, provider, FOAF.logo, "https://cimec.ro/wp-content/uploads/2021/08/logo-650-2.jpg");

        provider.addProperty(SKOS.prefLabel, "National Heritage Institute", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Institutul Național al Patrimoniului", Const.LANG_RO);

        provider.addProperty(EDM.acronym, "CIMEC", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "research, protect and restore the cultural heritage of Romania", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "cercetarea, protejarea și restaurarea patirmoniului cultural al României", Const.LANG_RO);

        addUriProperty(model, provider, EDM.organizationDomain, "public institution");
        addUriProperty(model, provider, EDM.organizationSector, "cultural heritage");
        addUriProperty(model, provider, EDM.geographicLevel, "country");
        addUriProperty(model, provider, EDM.country, "Romania");

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_PROVIDER, Const.LANG_EN);

        return provider;
    }

    public static Resource generateAgent(Model model) {
        String relativeUri = PlaceType.COUNTRY + ":Romania"
                + Namespace.URL_SEPARATOR
                + Text.sanitizeString("Institutul Național al Patrimoniului", true);
        String providerLink = URIUtils.prepareUri(NS_REPO_RESOURCE_ORGANIZATION, relativeUri);

        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, EDM.Agent);

        provider.addProperty(DC_11.identifier, CIMEC_LINK);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "homepage", CIMEC_LINK, null);
        PropertyUtils.addSubProperty(model, provider, DC_11.identifier, "logo", "https://cimec.ro/wp-content/uploads/2021/08/logo-650-2.jpg", null);

        provider.addProperty(SKOS.prefLabel, "National Heritage Institute", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Institutul Național al Patrimoniului", Const.LANG_RO);

        PropertyUtils.addSubProperty(model, provider, SKOS.altLabel, "acronym", "CIMEC", Const.LANG_RO);

        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationScope", "research, protect and restore the cultural heritage of Romania", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationScope", "cercetarea, protejarea și restaurarea patirmoniului cultural român", Const.LANG_RO);

        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationDomain", "public institution", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationSector", "cultural heritage", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "geographicLevel", "country", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "country", "Romania", Const.LANG_EN);

        PropertyUtils.addSubProperty(model, provider, SKOS.note, "europeanaRole", EDMRoles.ROLE_PROVIDER, Const.LANG_EN);

        return provider;
    }
}
