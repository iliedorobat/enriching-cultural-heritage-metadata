package ro.webdata.lido.convert.edm.common.constants;

import java.util.ArrayList;

public class EDMConstants {
    /**
     * Accepted values for <b>edm:type</b> property
     */
    public static ArrayList<String> EDM_TYPES = new ArrayList();

    public static String EDM_TYPE_3D = "3D";
    public static String EDM_TYPE_IMAGE = "IMAGE";
    public static String EDM_TYPE_SOUND = "SOUND";
    public static String EDM_TYPE_TEXT = "TEXT";
    public static String EDM_TYPE_VIDEO = "VIDEO";

    /**
     * The role for institutions
     * @implNote https://europeana.atlassian.net/browse/EMA-978?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel
     */
    public static String ROLE_DATA_PROVIDER = "data provider";
    /**
     * The role for hub or direct provider
     * @implNote https://europeana.atlassian.net/browse/EMA-978?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel
     */
    public static String ROLE_PROVIDER = "provider";
    /**
     * The role for Europeana
     * @implNote https://europeana.atlassian.net/browse/EMA-978?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel
     */
    public static String ROLE_EUROPEANA = "europeana";

    static {
        EDM_TYPES.add(EDM_TYPE_3D);
        EDM_TYPES.add(EDM_TYPE_IMAGE);
        EDM_TYPES.add(EDM_TYPE_SOUND);
        EDM_TYPES.add(EDM_TYPE_TEXT);
        EDM_TYPES.add(EDM_TYPE_VIDEO);
    }
}
