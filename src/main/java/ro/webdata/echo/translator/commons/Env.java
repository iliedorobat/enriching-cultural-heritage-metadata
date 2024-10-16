package ro.webdata.echo.translator.commons;

import ro.webdata.echo.commons.Const;

import java.util.List;
import java.util.Optional;

public final class Env {
    private static final String AGENT_TYPE = "Agent";

    public static final String DATA_TYPE_CIMEC = "CIMEC";
    public static final String DATA_TYPE_DSPACE = "DSPACE";
    public static final String DATA_TYPE_LIDO = "LIDO";
    /** Flag used to enable creation of FOAF.Organization instances */
    public static final boolean IS_ORGANIZATION = AGENT_TYPE.equals("Organization");

    /**
     * The native dataset language
     * FIXME: change the main language as you need
     */
    public static final String LANG_MAIN = Const.LANG_RO;

    public static final boolean PRINT_RDF_RESULTS = false;
    public static final boolean IS_PRINT_ENABLED = true;

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

    public static boolean museumsCollector(List<String> args) {
        return args.contains("--museumsCollector");
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
