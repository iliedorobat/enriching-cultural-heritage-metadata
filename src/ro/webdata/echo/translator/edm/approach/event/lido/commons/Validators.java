package ro.webdata.echo.translator.edm.approach.event.lido.commons;

import ro.webdata.echo.commons.graph.vocab.constraints.EDMType;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.constants.CHOType;

import java.util.Set;

public final class Validators {
    /**
     * Check of the value is one of the EDMConstants.EDM_TYPES values
     * @param value
     * @return
     */
    public static boolean isEDMType(String value) {
        return EDMType.VALUES.contains(value);
    }

    /**
     * Check if the value is one of the CHOType.SUBJECTS values
     * @param value
     * @param lang
     * @return
     */
    public static boolean isSubject(String value, String lang) {
        Set<String> subjects = CHOType.SUBJECTS.get(lang);

        return subjects != null && subjects.contains(value);
    }
}
