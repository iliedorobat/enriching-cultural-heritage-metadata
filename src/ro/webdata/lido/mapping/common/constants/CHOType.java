package ro.webdata.lido.mapping.common.constants;

import java.util.ArrayList;
import java.util.HashMap;

public class CHOType {
    /**
     * The list with the types of CHOs from a controlled vocabulary, which
     * is going to be used as values for <b>dc:subject</b> property<br/>
     * E.g. archaeology, documents etc.
     */
    //TODO: automatize
    public static ArrayList<HashMap<String, String>> SUBJECTS = new ArrayList();

    private static HashMap getSubjects(String language, String value) {
        HashMap hashMap = new HashMap();

        hashMap.put("lang", language);
        hashMap.put("value", value);

        return hashMap;
    }

    static {
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "archaeology"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "decorative arts"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "documents"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "englopion"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "ethnography"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "fine arts"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "history"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "loch"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "medals"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "natural sciences"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "numismatics"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "science and technology"));
        SUBJECTS.add(getSubjects(Constants.LANG_EN, "sextant"));

        SUBJECTS.add(getSubjects(Constants.LANG_RO, "album"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "alidadă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "ambarcațiuni"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "ancoră"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "ancore"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "arheologie"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "artă decorativă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "artă plastică"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "bancnotă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "barometru"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "binoclu"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "buton"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "ceas"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "ceasuri"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "cilndru"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "clișeu foto"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "clopot"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "colecția egipteană"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "compas"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "cordon"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "cronometre"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "cronometru"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "document imprimat"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "documente"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "engolpion"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "etnografie"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "felinar"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "galion"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "heliograf"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "hublou"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "istorie"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "înclinometre"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "înclinometru"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "lampă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "manometre"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "mareice sigilară"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "matrice sigilaqră"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "matriță monetară pt. avers și revers"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "matriță pentru medalie"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "matriță pentru medalie (avers)"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "matriță pentru medalii"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "măsură"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "medalistică"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "memoriale"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "mină"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "numismatică"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "obiect de cult"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "obiect de podoabă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "obiect de podoabă din aur"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "obiecte din lut"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "obiecte din piatră"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "obiecte masonice"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "pafta"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "pictură germană din Transilvania"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "piese din piatră"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "plachete"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "plastică"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "plică"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "scriere și tipar"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "sextant"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "sigiliu"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "sonde"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "stație radio-emisie"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "stațiograf"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "statuetă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "ștampilă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "știință și tehnică"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "științele naturii"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "timonă"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "tipar monetar"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "toc"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "torpile"));
        SUBJECTS.add(getSubjects(Constants.LANG_RO, "țeavă de tun"));
    }
}
