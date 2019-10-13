package ro.webdata.lido.convert.edm.processing.timespan.ro.model;

import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;

public class YearIntervalModel {
    private static final String CHRISTUM_ANTE  = "ante Christum natum";
    private static final String CHRISTUM_POST = "post Christum natum";
    private static final String AGE_SEPARATOR = "###";
    // Used to separate the minus sign from the dash separator "-2 - -14 p.chr"; "-2 p.chr - -14 p.chr"
    private static final String REGEX_AGE_SEPARATOR = "(?<=[\\w\\W&&[^ -]])[ ]*-[ ]*";
    private static final String REGEX_CHRISTUM_ANTE = "([ai]+[\\. ]*(ch[r]*|hr)[\\. ]*)";
    private static final String REGEX_CHRISTUM_POST = "([dp]+[\\. ]*(ch[r]*|hr)[\\. ]*)";

    private String ageFromValue = "";
    private String ageFromType = "";
    private String ageToValue = "";
    private String ageToType = "";

    private YearIntervalModel() {}

    public YearIntervalModel(String value) {
        String[] values = value
                .replaceAll("[\\[\\]()]", "")
                .replaceAll(REGEX_AGE_SEPARATOR, AGE_SEPARATOR)
                .split(AGE_SEPARATOR);
        String ageFromValue = values[0].replaceAll("[^0-9]", "");
        String ageFromType = values[0].replaceAll("[0-9]", "");

        String ageToValue = values[1].replaceAll("[^0-9]", "");
        String ageToType = values[1].replaceAll("[0-9]", "");

        setAgeFromValue(ageFromValue);
        setAgeFromType(ageFromType);
        setAgeToValue(ageToValue);
        setAgeToType(ageToType);

        // If the "from" type it's empty but the "to" type has a value,
        // this means that the "from" type need to inherit the "to" type
        // E.g.: 10 - 50 CHRISTUM_POST => 10 CHRISTUM_POST - 50 CHRISTUM_POST
        // E.g.: 50 - 10 CHRISTUM_ANTE => 50 CHRISTUM_ANTE - 10 CHRISTUM_ANTE
        if (this.ageFromType.length() == 0 && this.ageToType.length() > 0) {
            setAgeFromType(ageToType);
        }

        // If none of the ages have a type, it means the time is CHRISTUM_POST
        // E.g.: 10 - 50 => 10 CHRISTUM_POST - 50 CHRISTUM_POST
        //TODO: check if ageFromValue <= ageToValue (if not, make it CHRISTUM_ANTE)
        if (this.ageFromType.length() == 0 && this.ageToType.length() == 0) {
            setAgeFromType(CHRISTUM_POST);
            setAgeToType(CHRISTUM_POST);
        }

        checkAge();
    }

    @Override
    public String toString() {
        return ageFromValue + " (" + ageFromType + ")"
                + TimespanRegex.INTERVAL_SEPARATOR
                + ageToValue + " (" + ageToType + ")";
    }

    private void setAgeFromValue(String ageFromValue) {
        this.ageFromValue = ageFromValue;
    }

    private void setAgeFromType(String ageFromType) {
        this.ageFromType = getAgeType(ageFromType);
    }

    private void setAgeToValue(String ageToValue) {
        this.ageToValue = ageToValue;
    }

    private void setAgeToType(String ageToType) {
        this.ageToType = getAgeType(ageToType);
    }

    private String getAgeType(String ageType) {
        return ageType.trim()
                .replaceAll(REGEX_CHRISTUM_ANTE, CHRISTUM_ANTE)
                .replaceAll(REGEX_CHRISTUM_POST, CHRISTUM_POST);
    }

    private void checkAge() {
        try {
            int ageFrom = Integer.parseInt(ageFromValue);
            int ageTo = Integer.parseInt(ageToValue);

            if (ageFromType.equals(CHRISTUM_POST) && ageToType.equals(CHRISTUM_ANTE)) {
                System.err.println(
                        "The \"from age\" can not be " + CHRISTUM_POST
                                + " if the \"to age\" is " + CHRISTUM_ANTE + "!"
                );
            } else if (ageFromType.equals(CHRISTUM_ANTE) && ageToType.equals(CHRISTUM_ANTE)
                    && ageFrom < ageTo) {
                System.err.println("The \"from age\" (" + ageFrom + " " + CHRISTUM_ANTE + ") "
                        + "can not be lower than the \"to age\" (" + ageTo + " " + CHRISTUM_ANTE + ")!");
            } else if (ageFromType.equals(CHRISTUM_POST) && ageToType.equals(CHRISTUM_POST)
                    && ageFrom > ageTo) {
                System.err.println("The \"from age\" (" + ageFrom + " " + CHRISTUM_POST + ") "
                        + "can not be higher than the \"to age\" (" + ageTo + " " + CHRISTUM_POST + ")!");
            }
        } catch (NumberFormatException e) {

        }
    }
}
