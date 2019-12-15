package ro.webdata.translator.edm.approach.event.lido.common;

import ro.webdata.translator.edm.approach.event.lido.common.constants.CHOType;
import ro.webdata.translator.edm.approach.event.lido.common.constants.EDMConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class Validators {
    private static ArrayList<HashMap<String, String>> SUBJECTS = CHOType.SUBJECTS;

    /**
     * Check of the value is one of the EDMConstants.EDM_TYPES values
     * @param value
     * @return
     */
    public static boolean isEDMType(String value) {
        if (EDMConstants.EDM_TYPES.indexOf(value) != -1)
            return true;

        return false;
    }

    /**
     * Check if the value is one of the CHOType.SUBJECTS values
     * @param value
     * @return
     */
    public static boolean isSubject(String value) {
        for (int i = 0; i < SUBJECTS.size(); i++) {
            String superType = SUBJECTS.get(i).get("value");
            if (superType.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
