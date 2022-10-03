package ro.webdata.echo.translator.edm.approach.event.lido.commons.constants;

import ro.webdata.echo.commons.Const;

import java.util.ArrayList;
import java.util.HashMap;

public final class CHOType {
    /**
     * The list with the types of CHOs from a controlled vocabulary, which
     * is going to be used as values for <b>dc:subject</b> property<br/>
     * E.g. archaeology, documents etc.
     */
    // TODO: automatize
    public static ArrayList<HashMap<String, String>> SUBJECTS = new ArrayList<>();

    private static HashMap getSubjects(String language, String value) {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("lang", language);
        hashMap.put("value", value);

        return hashMap;
    }

    static {
        SUBJECTS.add(getSubjects(Const.LANG_EN, "archaeology"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "decorative arts"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "documents"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "englopion"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "ethnography"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "fine arts"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "history"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "loch"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "medals"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "natural sciences"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "numismatics"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "science and technology"));
        SUBJECTS.add(getSubjects(Const.LANG_EN, "sextant"));

        SUBJECTS.add(getSubjects(Const.LANG_RO, "album"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "alidadă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "ambarcațiuni"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "ancoră"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "ancore"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "arheologie"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "artă decorativă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "artă plastică"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "bancnotă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "barometru"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "binoclu"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "buton"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "ceas"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "ceasuri"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "cilndru"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "clișeu foto"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "clopot"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "colecția egipteană"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "compas"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "cordon"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "cronometre"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "cronometru"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "document imprimat"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "documente"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "engolpion"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "etnografie"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "felinar"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "galion"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "heliograf"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "hublou"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "istorie"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "înclinometre"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "înclinometru"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "lampă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "manometre"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "mareice sigilară"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "matrice sigilaqră"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "matriță monetară pt. avers și revers"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "matriță pentru medalie"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "matriță pentru medalie (avers)"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "matriță pentru medalii"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "măsură"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "medalistică"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "memoriale"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "mină"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "numismatică"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "obiect de cult"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "obiect de podoabă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "obiect de podoabă din aur"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "obiecte din lut"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "obiecte din piatră"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "obiecte masonice"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "pafta"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "pictură germană din Transilvania"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "piese din piatră"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "plachete"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "plastică"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "plică"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "scriere și tipar"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "sextant"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "sigiliu"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "sonde"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "stație radio-emisie"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "stațiograf"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "statuetă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "ștampilă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "știință și tehnică"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "științele naturii"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "timonă"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "tipar monetar"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "toc"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "torpile"));
        SUBJECTS.add(getSubjects(Const.LANG_RO, "țeavă de tun"));
    }
}
