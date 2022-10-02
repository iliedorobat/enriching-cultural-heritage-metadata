package ro.webdata.translator.commons;

import ro.webdata.echo.commons.Const;

import java.util.List;

public final class Env {
    public static final String EDM_APPROACH_EVENT_CENTRIC = "EVENT_CENTRIC";
    public static final String EDM_APPROACH_OBJECT_CENTRIC = "OBJECT_CENTRIC";
    public static final String DATA_TYPE_CIMEC = "CIMEC";
    public static final String DATA_TYPE_DSTORAGE = "DSTORAGE";
    public static final String DATA_TYPE_LIDO = "LIDO";

    /**
     * The native dataset language
     * FIXME: change the main language as you need
     */
    public static final String LANG_MAIN = Const.LANG_RO;

    public static final boolean PRINT_RDF_RESULTS = false;
    public static final boolean IS_PRINT_ENABLED = true;

    public static String getApproach(List<String> args) {
        if (args.contains("--approach=" + EDM_APPROACH_EVENT_CENTRIC)) {
            return EDM_APPROACH_EVENT_CENTRIC;
        } else if (args.contains("--approach=" + EDM_APPROACH_OBJECT_CENTRIC)) {
            return EDM_APPROACH_OBJECT_CENTRIC;
        }
        return null;
    }

    public static String getDataType(List<String> args) {
        if (args.contains("--dataType=" + DATA_TYPE_CIMEC)) {
            return DATA_TYPE_CIMEC;
        } else if (args.contains("--dataType=" + DATA_TYPE_DSTORAGE)) {
            return DATA_TYPE_DSTORAGE;
        } else if (args.contains("--dataType=" + DATA_TYPE_LIDO)) {
            return DATA_TYPE_LIDO;
        }
        return null;
    }

    public static boolean isDemo(List<String> args) {
        return args.contains("--demo");
    }
}
