package ro.webdata.lido.convert.edm.processing.timespan.ro.model.imprecise;

import ro.webdata.lido.convert.edm.common.PrintMessages;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimespanUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;

public class InaccurateYearModel {
    public static final String AFTER = "POST";
    public static final String BEFORE = "ANTE";
    public static final String APPROXIMATE = "APPROXIMATE";

    private static final String REGEX_NON_DIGIT = "[^\\d]";

    /** From date */
    private static String START = "START";
    /** To date */
    private static String END = "END";

    private String flag;
    private String eraStart, eraEnd;
    private int yearStart, yearEnd;
    private boolean isInterval;

    private InaccurateYearModel() {}

    //TODO: check for date, century, millennium or ages
    public InaccurateYearModel(String value, String flag) {
        setDateModel(value, flag, TimespanRegex.REGEX_INTERVAL_DELIMITER);
    }

    private void setDateModel(String value, String flag, String intervalSeparator) {
        String[] intervalValues = value.split(intervalSeparator);

        if (intervalValues.length == 2) {
            String startValue = TimespanUtils.clearChristumNotation(intervalValues[0]);
            String endValue = TimespanUtils.clearChristumNotation(intervalValues[1]);

            setIsInterval(true);
            // Set the end date before the start one to store the year
            // in order to use it as a end year and start year too
            setEra(intervalValues[1], END);
            setDate(endValue, END);
            setEra(intervalValues[0], START);
            setDate(startValue, START);
        } else {
            String preparedValue = TimespanUtils.clearChristumNotation(value);

            setIsInterval(false);
            setEra(value, START);
            setDate(preparedValue, START);
        }

        setFlag(flag);
    }

    //TODO: "dupa 29 aprilie 1616"; "dupa 10 mai 1903"
    private void setDate(String value, String position) {
        String preparedValue = value
                .replaceAll(REGEX_NON_DIGIT, Constants.EMPTY_VALUE_PLACEHOLDER);
        int year = Integer.parseInt(preparedValue);
        setYear(year, position);
    }

    @Override
    public String toString() {
        String start = flag + " " + yearStart + " " + TimespanUtils.getEraName(eraStart);

//        if (isInterval) {
//            String end = yearEnd + " " + TimespanUtils.getEraName(eraEnd);
//            return start + TimespanRegex.INTERVAL_SEPARATOR + end;
//        }

        return start;
    }

    private void setEra(String value, String position) {
        if (position.equals(START)) {
            if (!value.contains(TimespanRegex.CHRISTUM_BC) && this.eraEnd != null)
                this.eraStart = TimespanUtils.getEraPlaceholder(this.eraEnd);
            else
                this.eraStart = TimespanUtils.getEraPlaceholder(value);
        } else if (position.equals(END)) {
            this.eraEnd = TimespanUtils.getEraPlaceholder(value);
        }
    }

    private void setYear(int year, String position) {
        if (position.equals(START)) {
            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimespanRegex.CHRISTUM_AD))
                PrintMessages.printTooBigYear(year);
            this.yearStart = year;
        } else if (position.equals(END)) {
            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimespanRegex.CHRISTUM_AD))
                PrintMessages.printTooBigYear(year);
            this.yearEnd = year;
        }
    }

    private void setIsInterval(boolean isInterval) {
        this.isInterval = isInterval;
    }

    private void setFlag(String flag) {
        this.flag = flag;
    }
}
