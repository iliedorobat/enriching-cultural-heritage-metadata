package ro.webdata.translator.edm.approach.event.lido.commons;

import ro.webdata.echo.commons.graph.vocab.constraints.EDMType;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.CHOType;

public final class Validators {
    /**
     * Check of the value is one of the EDMConstants.EDM_TYPES values
     * @param value
     * @return
     */
    public static boolean isEDMType(String value) {
        if (EDMType.VALUES.indexOf(value) != -1)
            return true;

        return false;
    }

    /**
     * Check if the value is one of the CHOType.SUBJECTS values
     * @param value
     * @return
     */
    public static boolean isSubject(String value) {
        for (int i = 0; i < CHOType.SUBJECTS.size(); i++) {
            String superType = CHOType.SUBJECTS.get(i).get("value");
            if (superType.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
