package ro.webdata.echo.translator.edm.lido.mapping.core.administrativeMetadata.RecordWrapProcessing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMRoles;
import ro.webdata.echo.translator.commons.PropertyUtils;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_ORGANIZATION;
import static ro.webdata.echo.translator.edm.lido.commons.ResourceUtils.addUriProperty;

/**
 * Generate the Europeana Intermediate Provider (the institution which curates the data)
 */
public class UpbProvider {
    public static Resource generateOrganization(Model model) {
        String relativeUri = PlaceType.COUNTRY + ":Romania"
                + File.FILE_SEPARATOR
                + Text.sanitizeString("Politehnica University of Bucharest");
        String providerLink = URIUtils.prepareUri(NS_REPO_RESOURCE_ORGANIZATION, relativeUri);

        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        addUriProperty(model, provider, DC_11.identifier, "https://upb.ro/en/");
        addUriProperty(model, provider, FOAF.homepage, "https://upb.ro/en/");
        addUriProperty(model, provider, FOAF.logo, "https://upb.ro/upb-identitate-vizuala-logo/");

        provider.addProperty(SKOS.prefLabel, "Politehnica University of Bucharest", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Universitatea Politehnica din București", Const.LANG_RO);

        provider.addProperty(EDM.acronym, "PUB", Const.LANG_EN);
        provider.addProperty(EDM.acronym, "UPB", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "academic research", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "cercetare academică", Const.LANG_RO);

        addUriProperty(model, provider, EDM.organizationDomain, "education");
        addUriProperty(model, provider, EDM.organizationSector, "higher education");
        addUriProperty(model, provider, EDM.geographicLevel, "country");
        addUriProperty(model, provider, EDM.country, "Romania");

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_DATA_PROVIDER, Const.LANG_EN);

        return provider;
    }

    public static Resource generateAgent(Model model) {
        String relativeUri = PlaceType.COUNTRY + ":Romania"
                + File.FILE_SEPARATOR
                + Text.sanitizeString("Politehnica University of Bucharest");
        String providerLink = URIUtils.prepareUri(NS_REPO_RESOURCE_ORGANIZATION, relativeUri);

        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, EDM.Agent);

        addUriProperty(model, provider, DC_11.identifier, "https://upb.ro/en/");
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "homepage", "https://upb.ro/en/", null);
        PropertyUtils.addSubProperty(model, provider, DC_11.identifier, "logo", "https://upb.ro/upb-identitate-vizuala-logo/", null);

        provider.addProperty(SKOS.prefLabel, "Politehnica University of Bucharest", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Universitatea Politehnica din București", Const.LANG_RO);

        PropertyUtils.addSubProperty(model, provider, SKOS.altLabel, "acronym", "PUB", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.altLabel, "acronym", "UPB", Const.LANG_RO);

        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationScope", "academic research", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationScope", "cercetare academică", Const.LANG_RO);

        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationDomain", "education", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "organizationSector", "higher education", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "geographicLevel", "country", Const.LANG_EN);
        PropertyUtils.addSubProperty(model, provider, SKOS.note, "country", "Romania", Const.LANG_EN);

        PropertyUtils.addSubProperty(model, provider, SKOS.note, "europeanaRole", EDMRoles.ROLE_DATA_PROVIDER, Const.LANG_EN);

        return provider;
    }
}
