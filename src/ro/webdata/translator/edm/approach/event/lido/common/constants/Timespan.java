package ro.webdata.translator.edm.approach.event.lido.common.constants;

import java.util.ArrayList;
import java.util.HashMap;

public class Timespan {
    public static ArrayList<HashMap<String, String>> TIMESPAN = new ArrayList();

    private static HashMap getTimespan(String language, String value) {
        HashMap hashMap = new HashMap();

        hashMap.put("lang", language);
        hashMap.put("value", value);

        return hashMap;
    }
}
