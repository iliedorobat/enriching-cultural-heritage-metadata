package ro.webdata.lido.convert.edm.common.constants;

public class NSConstants {
    private static final String CC_LICENSES_LINK = "http://creativecommons.org";
    private static final String RIGHTS_LICENSES_LINK = "http://rightsstatements.org";

    // https://pro.europeana.eu/page/available-rights-statements
    public static final String[] LICENSES_NAME = {
            "PDM", "NoC-NC", "NoC-OKLR", "CC0", "CC BY", "CC BY-SA", "CC BY-ND",
            "CC BY-NC", "CC BY-NC-SA", "CC BY-NC-ND", "InC", "InC-EDU", "InC-EU-OW", "CNE"
    };
    public static final String[] LICENSES_LINK = {
            CC_LICENSES_LINK + "/publicdomain/mark/1.0/",
            CC_LICENSES_LINK + "/publicdomain/zero/1.0/",
            CC_LICENSES_LINK + "/licenses/by/4.0/",
            CC_LICENSES_LINK + "/licenses/by-sa/4.0/",
            CC_LICENSES_LINK + "/licenses/by-nd/4.0/",
            CC_LICENSES_LINK + "/licenses/by-nc/4.0/",
            CC_LICENSES_LINK + "/licenses/by-nc-sa/4.0/",
            CC_LICENSES_LINK + "/licenses/by-nc-nd/4.0/",
            RIGHTS_LICENSES_LINK + "vocab/NoC-NC/1.0/",
            RIGHTS_LICENSES_LINK + "vocab/NoC-OKLR/1.0/",
            RIGHTS_LICENSES_LINK + "vocab/InC/1.0/",
            RIGHTS_LICENSES_LINK + "vocab/InC-EDU/1.0/",
            RIGHTS_LICENSES_LINK + "vocab/InC-OW-EU/1.0/",
            RIGHTS_LICENSES_LINK + "vocab/CNE/1.0/"
    };

    public static final String NS_LIDO = "http://www.lido-schema.org/schema/v1.0/lido-v1.0-schema-listing.html#";
    public static final String NS_REPO = "http://opendata.cs.pub.ro";
    public static final String NS_REPO_PROPERTY = NS_REPO + "/property";
    public static final String NS_REPO_RESOURCE = NS_REPO + "/resource";
    public static final String LINK_ID_AGENT = "/agent";
    public static final String LINK_ID_AGGREGATION = "/aggregation";
    public static final String LINK_ID_CHO = "/cho";
    public static final String LINK_ID_EVENT = "/event";
    public static final String LINK_ID_LICENSE = "/license";
    public static final String LINK_ID_ORGANIZATION = "/organization";

    public static final String NS_REPO_RESOURCE_AGENT = NS_REPO_RESOURCE + LINK_ID_AGENT;
    public static final String NS_REPO_RESOURCE_CHO = NS_REPO_RESOURCE + LINK_ID_CHO;
    public static final String NS_REPO_RESOURCE_EVENT = NS_REPO_RESOURCE + LINK_ID_EVENT;
    public static final String NS_REPO_RESOURCE_ORGANIZATION = NS_REPO_RESOURCE + LINK_ID_ORGANIZATION;

    public static final String NS_DBPEDIA_PAGE = "http://dbpedia.org/page";
    public static final String NS_DBPEDIA_PROPERTY = "http://dbpedia.org/property";
}
