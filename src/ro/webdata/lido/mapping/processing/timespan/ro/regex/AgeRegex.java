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
    public static final String PLEISTOCENE_AGE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "pleistocen" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Mesolithic */
    public static final String MESOLITHIC_AGE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "epipaleolitic" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Chalcolithic */
    public static final String CHALCOLITHIC_AGE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "eneolitic" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Neolithic */
    public static final String NEOLITHIC_AGE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "neolitic[\\w]*" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Bronze_Age */
    public static final String BRONZE_AGE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "bronz" + REGEX_OR + "bronzului" + REGEX_OR + "tarzii" + ")" + TEXT_END + ")";

    /** http://dbpedia.org/page/Aurignacian */
    public static final String AURIGNACIAN_CULTURE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "aurignacian" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Hallstatt_culture */
    public static final String HALLSTATT_CULTURE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "hallstatt" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Middle_Ages */
    public static final String MIDDLE_AGES = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "(medieval" + REGEX_OR + "medievala)" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Modern_history */
    public static final String MODERN_AGES = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "moderna" + ")" + TEXT_END + ")";

    /** http://dbpedia.org/page/Ptolemaic_dynasty */
    public static final String PTOLEMAIC_DYNASTY = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "ptolem[\\w]+" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Roman_Empire */
    public static final String ROMAN_EMPIRE_AGE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "romana" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Nerva–Antonine_dynasty */
    public static final String NERVA_ANTONINE_DYNASTY = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "antoninian[\\w]*" + REGEX_OR + "hadrian" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Renaissance */
    public static final String RENAISSANCE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "renastere" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/French_Consulate */
    public static final String FRENCH_CONSULATE_AGE = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "perioada consulatului francez" + ")" + TEXT_END + ")";

    /** http://dbpedia.org/page/World_War_I */
    public static final String WW_I_PERIOD = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "primul razboi mondial" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/Interwar_period */
    public static final String INTERWAR_PERIOD = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "interbelica" + ")" + TEXT_END + ")";
    /** http://dbpedia.org/page/World_War_II */
    public static final String WW_II_PERIOD = TimespanRegex.CASE_INSENSITIVE
            + "(" + TEXT_START + "(" + "al (doilea" + REGEX_OR + "ii-lea) razboi mondial" + ")" + TEXT_END + ")";

    public static final String[] AGE_OPTIONS = {
            PLEISTOCENE_AGE,
            MESOLITHIC_AGE,
            CHALCOLITHIC_AGE,
            NEOLITHIC_AGE,
            BRONZE_AGE,
            MIDDLE_AGES,
            MODERN_AGES,
            HALLSTATT_CULTURE,
            AURIGNACIAN_CULTURE,
            PTOLEMAIC_DYNASTY,
            ROMAN_EMPIRE_AGE,
            NERVA_ANTONINE_DYNASTY,
            RENAISSANCE,
            FRENCH_CONSULATE_AGE,
            WW_I_PERIOD,
            INTERWAR_PERIOD,
            WW_II_PERIOD
    };
}
