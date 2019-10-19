package ro.webdata.lido.mapping.processing.timespan.ro.regex;

/**
 * Regular expressions for those time intervals that point to
 * an age (epoch) time period
 */
public class AgeRegex {
    private static final String REGEX_OR = TimespanRegex.REGEX_OR;
    private static final String TEXT_START = TimespanRegex.TEXT_START;
    private static final String TEXT_END = TimespanRegex.TEXT_END;

    /** http://dbpedia.org/page/Pleistocene */
    private static final String PLEISTOCENE_AGE = TEXT_START + "pleistocen" + TEXT_END;
    /** http://dbpedia.org/page/Mesolithic */
    private static final String MESOLITHIC_AGE = TEXT_START + "epipaleolitic" + TEXT_END;
    /** http://dbpedia.org/page/Chalcolithic */
    private static final String CHALCOLITHIC_AGE = TEXT_START + "eneolitic" + TEXT_END;
    /** http://dbpedia.org/page/Neolithic */
    private static final String NEOLITHIC_AGE = TEXT_START + "neolitic[\\w]*" + TEXT_END;
    /** http://dbpedia.org/page/Bronze_Age */
    private static final String BRONZE_AGE = TEXT_START + "(bronz|bronzului|tarzii)" + TEXT_END;

    /** http://dbpedia.org/page/Aurignacian */
    private static final String AURIGNACIAN_CULTURE = TEXT_START + "aurignacian" + TEXT_END;
    /** http://dbpedia.org/page/Hallstatt_culture */
    private static final String HALLSTATT_CULTURE = TEXT_START + "hallstatt" + TEXT_END;
    /** http://dbpedia.org/page/Middle_Ages */
    private static final String MIDDLE_AGE = TEXT_START + "(medieval|medievala)" + TEXT_END;
    /** http://dbpedia.org/page/Modern_history */
    private static final String MODERN_AGE = TEXT_START + "moderna" + TEXT_END;

    /** http://dbpedia.org/page/Ptolemaic_dynasty */
    private static final String PTOLEMAIC_DYNASTY = TEXT_START + "ptolem[\\w]+" + TEXT_END;
    /** http://dbpedia.org/page/Roman_Empire */
    private static final String ROMAN_EMPIRE_AGE = TEXT_START + "romana" + TEXT_END;
    /** http://dbpedia.org/page/Nerva–Antonine_dynasty */
    private static final String NERVA_ANTONINE_DYNASTY = TEXT_START + "(antoninian[\\w]*|hadrian)" + TEXT_END;
    /** http://dbpedia.org/page/Renaissance */
    //TODO: check "69 p.chr./renaștere"
    private static final String RENAISSANCE = TEXT_START + "renastere" + TEXT_END;
    /** http://dbpedia.org/page/French_Consulate */
    private static final String FRENCH_CONSULATE_AGE = TEXT_START + "perioada consulatului francez" + TEXT_END;

    /** http://dbpedia.org/page/World_War_I */
    private static final String WW_I_PERIOD = TEXT_START + "primul razboi mondial" + TEXT_END;
    /** http://dbpedia.org/page/Interwar_period */
    private static final String INTERWAR_PERIOD = TEXT_START + "interbelica" + TEXT_END;
    /** http://dbpedia.org/page/World_War_II */
    private static final String WW_II_PERIOD = TEXT_START + "al (doilea|ii-lea) razboi mondial" + TEXT_END;

    public static final String AGE_OPTIONS = "(" + PLEISTOCENE_AGE + ")"
                + REGEX_OR + "(" + MESOLITHIC_AGE + ")"
                + REGEX_OR + "(" + CHALCOLITHIC_AGE + ")"
                + REGEX_OR + "(" + NEOLITHIC_AGE + ")"
                + REGEX_OR + "(" + BRONZE_AGE + ")"
                + REGEX_OR + "(" + MIDDLE_AGE + ")"
                + REGEX_OR + "(" + MODERN_AGE + ")"
                + REGEX_OR + "(" + HALLSTATT_CULTURE + ")"
                + REGEX_OR + "(" + AURIGNACIAN_CULTURE + ")"
                + REGEX_OR + "(" + PTOLEMAIC_DYNASTY + ")"
                + REGEX_OR + "(" + ROMAN_EMPIRE_AGE + ")"
                + REGEX_OR + "(" + NERVA_ANTONINE_DYNASTY + ")"
                + REGEX_OR + "(" + RENAISSANCE + ")"
                + REGEX_OR + "(" + FRENCH_CONSULATE_AGE + ")"
                + REGEX_OR + "(" + WW_I_PERIOD + ")"
                + REGEX_OR + "(" + INTERWAR_PERIOD + ")"
                + REGEX_OR + "(" + WW_II_PERIOD
            + ")";
}
