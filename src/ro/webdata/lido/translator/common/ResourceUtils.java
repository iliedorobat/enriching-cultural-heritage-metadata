package ro.webdata.lido.translator.common;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.translator.common.constants.Constants;
import ro.webdata.lido.translator.common.constants.EDMConstants;
import ro.webdata.lido.translator.common.constants.FileConstants;
import ro.webdata.lido.translator.common.constants.NSConstants;
import ro.webdata.lido.translator.vocabulary.EDM;
import ro.webdata.lido.translator.vocabulary.EDM2;

public class ResourceUtils {
    /**
     * Generate the Europeana Provider (the institution which Europeana is harvesting for data)
     * @param model The RDF graph
     * @return The Europeana Provider
     */
    public static Resource generateProvider(Model model) {
        String providerLink = NSConstants.NS_REPO_RESOURCE_ORGANIZATION
                + TextUtils.sanitizeString("/Politehnica University of Bucharest");
        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        provider.addProperty(DC_11.identifier, "https://upb.ro/en/", Constants.LANG_EN);
        provider.addProperty(DC_11.identifier, "https://upb.ro/", Constants.LANG_RO);

        provider.addProperty(FOAF.homepage, "https://upb.ro/en/", Constants.LANG_EN);
        provider.addProperty(FOAF.homepage, "https://upb.ro/", Constants.LANG_RO);

        provider.addProperty(SKOS.prefLabel, "Politehnica University of Bucharest", Constants.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Universitatea Politehnica din București", Constants.LANG_RO);

        provider.addProperty(EDM2.acronym, "PUB", Constants.LANG_EN);
        provider.addProperty(EDM2.acronym, "UPB", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationScope, "academic research", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationScope, "cercetare academică", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationDomain, "education", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationDomain, "învățământ", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationSector, "higher education", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationSector, "învățământ superior", Constants.LANG_RO);

        provider.addProperty(EDM2.geographicLevel, "county", Constants.LANG_EN);
        provider.addProperty(EDM2.geographicLevel, "județ", Constants.LANG_RO);

        ResourceUtils.addProviderCountry(provider);

        provider.addProperty(EDM2.europeanaRole, EDMConstants.ROLE_DATA_PROVIDER, Constants.LANG_EN);

        return provider;
    }

    /**
     * Generate the Europeana Intermediate Provider (the institution where are data stored)
     * @param model The RDF graph
     * @return The Europeana Intermediate Provider
     */
    public static Resource generateIntermediateProvider(Model model) {
        String providerLink = NSConstants.NS_REPO_RESOURCE_ORGANIZATION
                + TextUtils.sanitizeString("/Romanian Open Data Portal");
        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        provider.addProperty(DC_11.identifier, Constants.DATA_GOV_LINK_EN, Constants.LANG_EN);
        provider.addProperty(DC_11.identifier, Constants.DATA_GOV_LINK_RO, Constants.LANG_RO);

        provider.addProperty(FOAF.homepage, Constants.DATA_GOV_LINK_EN, Constants.LANG_EN);
        provider.addProperty(FOAF.homepage, Constants.DATA_GOV_LINK_RO, Constants.LANG_RO);

        provider.addProperty(SKOS.prefLabel, "data.gov.ro", Constants.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "data.gov.ro", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationScope, "centralization of open data published " +
                "by Romanian institutions according to the principles and standards in the field", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationScope, "centralizarea datelor deschise publicate " +
                "de instituțiile din România conform principiilor și standardelor în domeniu", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationDomain, "data publishing", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationDomain, "publicarea datelor", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationSector, "open data", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationSector, "date deschise", Constants.LANG_RO);

        provider.addProperty(EDM2.geographicLevel, "country", Constants.LANG_EN);
        provider.addProperty(EDM2.geographicLevel, "țară", Constants.LANG_RO);

        ResourceUtils.addProviderCountry(provider);

        provider.addProperty(EDM2.europeanaRole, EDMConstants.ROLE_DATA_PROVIDER, Constants.LANG_EN);

        return provider;
    }

    /**
     * Add Romania country for a provider from Romania
     * @param provider The provider, dataProvider or intermediateProvider
     */
    //TODO: automatize
    public static void addProviderCountry(Resource provider) {
        provider.addProperty(EDM.country, "Romania", Constants.LANG_EN);
        provider.addProperty(EDM.country, "România", Constants.LANG_RO);
    }

    /**
     * Generate a URI based on the namespace
     * @param namespace The namespace (see NSConstants)
     * @param resourceName The name of resource (a country, a name, a concept etc.)
     * @return The generated URI
     */
    public static String generateURI(String namespace, String resourceName) {
        return namespace + FileConstants.FILE_SEPARATOR + TextUtils.sanitizeString(resourceName);
    }

    /**
     * Generate an encoded URI using the DBPedia namespace
     * @param resourceName The name of resource (a country, a name, a concept etc.)
     * @return The DBPedia encoded URI
     */
    public static String generateDBPediaURI(String resourceName) {
        return NSConstants.NS_DBPEDIA_PAGE + TextUtils.encodeURI(resourceName);
    }
}
