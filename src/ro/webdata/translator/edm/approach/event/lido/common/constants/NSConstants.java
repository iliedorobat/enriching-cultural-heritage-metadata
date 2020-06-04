package ro.webdata.translator.edm.approach.event.lido.common.constants;

//TODO: move the NSConstants to "src.ro.webdata.common.constants"
public class NSConstants {
    private static final String CC_LICENSES_LINK = "http://creativecommons.org";
    private static final String RIGHTS_LICENSES_LINK = "http://rightsstatements.org";
    public static final String URL_SEPARATOR = "/";

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

    private static final String NS_LIDO = "http://www.lido-schema.org/schema/v1.0/lido-v1.0-schema-listing.html#";
    private static final String NS_REPO = "http://opendata.cs.pub.ro/";

    public static final String NS_REPO_PROPERTY = NS_REPO + "property" + FileConstants.FILE_SEPARATOR;
    public static final String NS_REPO_RESOURCE = NS_REPO + "resource" + FileConstants.FILE_SEPARATOR;

    public static final String LINK_ID_AGENT = "agent" + FileConstants.FILE_SEPARATOR;
    public static final String LINK_ID_AGGREGATION = "aggregation" + FileConstants.FILE_SEPARATOR;
    public static final String LINK_ID_CHO = "cho" + FileConstants.FILE_SEPARATOR;
    public static final String LINK_ID_EVENT = "event" + FileConstants.FILE_SEPARATOR;
    public static final String LINK_ID_LICENSE = "license" + FileConstants.FILE_SEPARATOR;
    public static final String LINK_ID_ORGANIZATION = "organization" + FileConstants.FILE_SEPARATOR;
    public static final String LINK_ID_PLACE = "place" + FileConstants.FILE_SEPARATOR;
    public static final String LINK_ID_TIMESPAN = "timespan" + FileConstants.FILE_SEPARATOR;

    public static final String NS_REPO_RESOURCE_AGENT = NS_REPO_RESOURCE + LINK_ID_AGENT;
    public static final String NS_REPO_RESOURCE_AGGREGATION = NS_REPO_RESOURCE + LINK_ID_AGGREGATION;
    public static final String NS_REPO_RESOURCE_CHO = NS_REPO_RESOURCE + LINK_ID_CHO;
    public static final String NS_REPO_RESOURCE_EVENT = NS_REPO_RESOURCE + LINK_ID_EVENT;
    public static final String NS_REPO_RESOURCE_ORGANIZATION = NS_REPO_RESOURCE + LINK_ID_ORGANIZATION;
    public static final String NS_REPO_RESOURCE_PLACE = NS_REPO_RESOURCE + LINK_ID_PLACE;
    public static final String NS_REPO_RESOURCE_TIMESPAN = NS_REPO_RESOURCE + LINK_ID_TIMESPAN;
    public static final String NS_REPO_RESOURCE_TIMESPAN_UNKNOWN = NS_REPO_RESOURCE_TIMESPAN + "unknown" + FileConstants.FILE_SEPARATOR;
    public static final String NS_REPO_RESOURCE_TIMESPAN_UNKNOWN_CENTURY = NS_REPO_RESOURCE_TIMESPAN_UNKNOWN
            + URL_SEPARATOR + Constants.CENTURY_PLACEHOLDER;
    public static final String NS_REPO_RESOURCE_TIMESPAN_UNKNOWN_MILLENNIUM = NS_REPO_RESOURCE_TIMESPAN_UNKNOWN
            + URL_SEPARATOR + Constants.MILLENNIUM_PLACEHOLDER;

    public static final String NS_DBPEDIA_RESOURCE = "http://dbpedia.org/page/";
    public static final String NS_DBPEDIA_PROPERTY = "http://dbpedia.org/property/";
}
