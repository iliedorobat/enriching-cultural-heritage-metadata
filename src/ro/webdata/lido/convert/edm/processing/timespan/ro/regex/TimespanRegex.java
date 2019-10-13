package ro.webdata.lido.convert.edm.processing.timespan.ro.regex;

/**
 * By using the <b>[?<=sentence]</b> construction, it will be matched
 * any text after "sentence" ("sentence" is a word as example).
 */
public class TimespanRegex {
    public static final String REGEX_OR = "|";
    public static final String REGEX_PUNCTUATION = "[\\.,;\\?!\\- ]";
    public static final String REGEX_PUNCTUATION_UNLIMITED = REGEX_PUNCTUATION + "*";
    public static final String REGEX_INTERVAL_DELIMITER = "([ ]*-[ ]*)";
    public static final String REGEX_DATE_INTERVAL_SEPARATOR = "([ ]+-[ ]+)";
    public static final String REGEX_DATE_SEPARATOR = "[\\./\\- ]+";
    public static final String TEXT_START =
            "("
                + "?<=" + "("
                    + "^" + REGEX_OR + "\\A" + REGEX_OR + "[\\.,;\\?!\\-( ]+"
                + ")"
            + ")";
    public static final String TEXT_END =
            "("
                + "?=" + "("
                    + "$" + REGEX_OR + "\\z" + REGEX_OR + "[\\.,;\\?!\\-) ]+"
                + ")"
            + ")";
    public static final String CHRISTUM_NOTATION = "(" + "[adip][\\. ]*(ch[r]*|hr)[\\. ]*" + ")";
    public static final String CHRISTUM_SUFFIX_NOTATION = "([ ]*" + CHRISTUM_NOTATION + ")*";

    private static final String CHR_NOTATION = "(ch[r]*|hr)[\\. ]*";
    //TODO: replace CHRISTUM_NOTATION
    // Anno Domini (After Christ)
    public static final String AGE_AD = TEXT_START + "("
                + "(" + "e.n[\\.]{0,1}" + ")"
                + REGEX_OR + "(" + "[dp][\\. ]*" + CHR_NOTATION + ")"
            + ")" + TEXT_END;
    //TODO: replace CHRISTUM_NOTATION
    // Before Christ
    public static final String AGE_BC = TEXT_START + "("
                + "i.hr."
                + REGEX_OR + "(" + "[ai][\\. ]*" + CHR_NOTATION + ")"
            + ")" + TEXT_END;

    public static final String MONTHS_RO =
            "("
                + "ianuarie" + REGEX_OR + "ian[\\.]"
                + REGEX_OR + "februarie" + REGEX_OR + "fevruarie" + REGEX_OR + "feb[\\.]"
                + REGEX_OR + "martie" + REGEX_OR + "mart[\\.]"
                + REGEX_OR + "aprilie" + REGEX_OR + "apr[\\.]"
                + REGEX_OR + "mai"
                + REGEX_OR + "iunie" + REGEX_OR + "iumie" + REGEX_OR + "iun[\\.]"
                + REGEX_OR + "iulie" + REGEX_OR + "iul[\\.]"
                + REGEX_OR + "august" + REGEX_OR + "aug[\\.]"
                + REGEX_OR + "septembrie" + REGEX_OR + "sept[\\.]"
                + REGEX_OR + "octombrie" + REGEX_OR + "0ctombrie" + REGEX_OR + "oct[\\.]"
                + REGEX_OR + "noiembrie" + REGEX_OR + "noimbrie" + REGEX_OR + "nov[\\.]"
                + REGEX_OR + "decembrie" + REGEX_OR + "decembre" + REGEX_OR + "dec[\\.]"
            + ")";

    private static final String AGES_ARABIC_GROUP = "(" + TEXT_START + "\\d+" + TEXT_END + ")";
    public static final String AGES_ARABIC_NOTATION =
            "("
                + AGES_ARABIC_GROUP + "([- ]*lea){0,1}"
                + "("
                    + REGEX_PUNCTUATION_UNLIMITED + CHRISTUM_NOTATION
                + "){0,1}"
            + ")";
    private static final String AGES_ROMAN_GROUP = "(" + TEXT_START + "[ivxlcdm]+" + TEXT_END + ")";
    public static final String AGES_ROMAN_NOTATION =
            "("
                + AGES_ROMAN_GROUP + "([- ]*lea){0,1}"
                + "("
                    + REGEX_PUNCTUATION_UNLIMITED + CHRISTUM_NOTATION
                + "){0,1}"
            + ")";

    public static final String CENTURY_NOTATION = "(" +  "(sec[\\w]*)([\\., ]+al[\\. ]+)*" + ")";
    public static final String MILLENNIUM_NOTATION = "(" + "(mil[\\w]*)([\\. ]+al[ ]+)*" + ")";

    private static final String FIRST_HALF_STRING_REGEX = "(" + "prim[a]*[\\. ]+(jum|part)" + ")";
    private static final String SECOND_HALF_STRING_REGEX =  "(" + "a[ ]+(doua|(ii[-a]*))[\\. ]+(jum|part)" + ")";
    private static final String REGEX_A_AL_POSTFIX = "(" + "[\\w]*[\\.]*([\\. ]+(a|al))*" + ")";

    public static final String FIRST_HALF =
            "("
                + TEXT_START + "("
                    + "(1/2)"
                    + REGEX_OR + "(½)"
                    + REGEX_OR + "(" + FIRST_HALF_STRING_REGEX + REGEX_A_AL_POSTFIX + ")"
                + ")" + TEXT_END
            + ")";
    public static final String SECOND_HALF =
            "("
                + TEXT_START + "("
                    + "(2/2)"
                    + REGEX_OR + "(" + SECOND_HALF_STRING_REGEX + REGEX_A_AL_POSTFIX + ")"
                + ")" + TEXT_END
            + ")";
    public static final String MIDDLE_OF =
            "("
                + TEXT_START + "(jumatatea|(mij[\\w]*)|mj\\.)" + TEXT_END
            + ")";

    /** First Quarter = Beginning of... */
    public static final String FIRST_QUARTER =
            "("
                + TEXT_START + "("
                    + "(1/4)"
                    + REGEX_OR + "(¼)"
                    + REGEX_OR + "(" + "(inc[\\w]*)" + "([\\. ]*de){0,1}" + ")"
                    + REGEX_OR + "(" + "primul[ ]+sfert" + "([ ]+a[l]{0,1}){0,1}" + ")"
                + ")" + TEXT_END
            + ")";
    public static final String SECOND_QUARTER =
            "("
                + TEXT_START + "("
                    + "(2/4)"
                    + REGEX_OR + "(al doile[a]{0,1} sfert al)"
                + ")" + TEXT_END
            + ")";
    public static final String THIRD_QUARTER =
            "("
                + TEXT_START + "("
                    + "(3/4)"
                    + REGEX_OR + "(¾)"
                    + REGEX_OR + "(al treilea sfert al)"
                + ")" + TEXT_END
            + ")";
    /** Last Quarter = End of... */
    public static final String FORTH_QUARTER =
            "("
                + TEXT_START + "("
                    + "(4/4)"
                    + REGEX_OR + "(" + "(ultimul[ ]+sfert)" + "([ ]+(a[l]{0,1}|de)*){0,1}" + ")"
                    + REGEX_OR + "(" + "(sf[^(ert)][\\w]*)" + "([\\. ]de){0,1}" + ")"
                + ")"
            + ")";
}
