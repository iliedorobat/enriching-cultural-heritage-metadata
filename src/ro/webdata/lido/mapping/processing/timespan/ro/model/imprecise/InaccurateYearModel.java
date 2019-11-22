package ro.webdata.lido.mapping.processing.timespan.ro.model.imprecise;

import ro.webdata.lido.mapping.common.CollectionUtils;
import ro.webdata.lido.mapping.common.constants.Constants;
import ro.webdata.lido.mapping.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.mapping.processing.timespan.ro.model.TimePeriodModel;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.TimespanRegex;

import java.util.TreeSet;

//TODO: find a way to store the detail for inaccurate time periods (after, before, approx.)
public class InaccurateYearModel extends TimePeriodModel {
    private static final String REGEX_NON_DIGIT = "[^\\d]";

    private InaccurateYearModel() {}

    public InaccurateYearModel(String value) {
        setDateModel(value);
    }

    private void setDateModel(String value) {
        String[] intervalValues = value.split(TimespanRegex.REGEX_INTERVAL_DELIMITER);

        if (intervalValues.length == 2) {
            setIsInterval(true);

            // Set the end time before the start time in order to use it
            // as start time too, if this is missing, but the end year exists
            String startValue = TimeUtils.clearChristumNotation(intervalValues[0]);
            String endValue = TimeUtils.clearChristumNotation(intervalValues[1]);

            setEra(intervalValues[1], TimeUtils.END_PLACEHOLDER);
            setEra(intervalValues[0], TimeUtils.START_PLACEHOLDER);

            setDate(endValue, TimeUtils.END_PLACEHOLDER);
            setDate(startValue, TimeUtils.START_PLACEHOLDER);
        } else {
            String preparedValue = TimeUtils.clearChristumNotation(value);

            setEra(value, TimeUtils.END_PLACEHOLDER);
            setEra(value, TimeUtils.START_PLACEHOLDER);

            setDate(preparedValue, TimeUtils.END_PLACEHOLDER);
            setDate(preparedValue, TimeUtils.START_PLACEHOLDER);
        }
    }

    @Override
    public String toString() {
        TreeSet<String> centurySet = getCenturySet();
        return CollectionUtils.treeSetToDbpediaString(centurySet);
    }

    //TODO: "dupa 29 aprilie 1616"; "dupa 10 mai 1903"
    private void setDate(String value, String position) {
        String year = value
                .replaceAll(REGEX_NON_DIGIT, Constants.EMPTY_VALUE_PLACEHOLDER);

        setCentury(year, position);
        setYear(year, position);
    }
}
