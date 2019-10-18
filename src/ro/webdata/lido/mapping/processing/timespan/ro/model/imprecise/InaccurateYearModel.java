package ro.webdata.lido.mapping.processing.timespan.ro.model.imprecise;

import ro.webdata.lido.mapping.common.constants.Constants;
import ro.webdata.lido.mapping.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.mapping.processing.timespan.ro.model.TimeModel;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.TimespanRegex;

public class InaccurateYearModel extends TimeModel {
    private static final String REGEX_NON_DIGIT = "[^\\d]";

    private String flag;

    private InaccurateYearModel() {}

    //TODO: check for date, century, millennium or ages
    public InaccurateYearModel(String value, String flag) {
        setDateModel(value, flag, TimespanRegex.REGEX_INTERVAL_DELIMITER);
    }

    private void setDateModel(String value, String flag, String intervalSeparator) {
        String[] intervalValues = value.split(intervalSeparator);

        if (intervalValues.length == 2) {
            setIsInterval(true);

            // Set the end time before the start time in order to use it
            // as start time too, if this is missing, but the end year exists
            String startValue = TimeUtils.clearChristumNotation(intervalValues[0]);
            String endValue = TimeUtils.clearChristumNotation(intervalValues[1]);

            setEra(intervalValues[1], TimeUtils.END_PLACEHOLDER);
            setDate(endValue, TimeUtils.END_PLACEHOLDER);

            setEra(intervalValues[0], TimeUtils.START_PLACEHOLDER);
            setDate(startValue, TimeUtils.START_PLACEHOLDER);
        } else {
            String preparedValue = TimeUtils.clearChristumNotation(value);

            setIsInterval(false);
            setEra(value, TimeUtils.START_PLACEHOLDER);
            setDate(preparedValue, TimeUtils.START_PLACEHOLDER);
        }

        setFlag(flag);
    }

    //TODO: "dupa 29 aprilie 1616"; "dupa 10 mai 1903"
    private void setDate(String value, String position) {
        String year = value
                .replaceAll(REGEX_NON_DIGIT, Constants.EMPTY_VALUE_PLACEHOLDER);
        setYear(year, position);
    }

    @Override
    public String toString() {
        String start = flag + " " + yearStart + " " + TimeUtils.getEraLabel(eraStart);

//        if (isInterval) {
//            String end = yearEnd + " " + TimeUtils.getEraName(eraEnd);
//            return start + TimespanRegex.INTERVAL_SEPARATOR + end;
//        }

        return start;
    }

    private void setFlag(String flag) {
        this.flag = flag;
    }
}
