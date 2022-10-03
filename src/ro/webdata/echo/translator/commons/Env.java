package ro.webdata.echo.translator.commons;

import ro.webdata.echo.commons.Const;

import java.util.List;
import java.util.Optional;

public final class Env {
    public static final String EDM_APPROACH_EVENT_CENTRIC = "EVENT_CENTRIC";
    public static final String EDM_APPROACH_OBJECT_CENTRIC = "OBJECT_CENTRIC";
    public static final String DATA_TYPE_CIMEC = "CIMEC";
    public static final String DATA_TYPE_DSPACE = "DSPACE";
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
        } else if (args.contains("--dataType=" + DATA_TYPE_DSPACE)) {
            return DATA_TYPE_DSPACE;
        } else if (args.contains("--dataType=" + DATA_TYPE_LIDO)) {
            return DATA_TYPE_LIDO;
        }
        return null;
    }

    public static String getInputTime(List<String> args) {
        if (normalizeTimeExpression(args)) {
            Optional<String> result = args.stream()
                    .filter(item -> item.contains("--expression="))
                    .findFirst();
            if (result.isPresent()) {
                return result.get().replace("--expression=", "");
            }
        }
        return null;
    }

    public static boolean isDemo(List<String> args) {
        return args.contains("--demo");
    }

    public static boolean normalizeTimeExpression(List<String> args) {
        for (String arg : args) {
            if (arg.contains("--expression=")) {
                return true;
            }
        }
        return false;
    }
}
