package ro.webdata.lido.translator.processing.timespan.ro.model;

import ro.webdata.lido.translator.common.CollectionUtils;
import ro.webdata.lido.translator.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.translator.processing.timespan.ro.regex.TimespanRegex;

import java.util.TreeSet;

public class YearModel extends TimePeriodModel {
    // Used to separate the minus sign from the dash separator "-2 - -14 p.chr"; "-2 p.chr - -14 p.chr"
    private static final String REGEX_AGE_SEPARATOR = "(?<=[\\w\\W&&[^ -]])[ ]*-[ ]*";

    private YearModel() {}

    public YearModel(String value) {
        setYearModel(value);
    }

    private void setYearModel(String value) {
        // used for cases similar with "anul 13=1800/1801" or with "110/109 a. chr."
        String preparedValue = value.replaceAll("/", " - ");
        String[] intervalValues = preparedValue.split(TimespanRegex.REGEX_INTERVAL_DELIMITER);

        if (intervalValues.length == 2) {
            setIsInterval(true);

            String endYear = TimeUtils.clearChristumNotation(intervalValues[1]);
            String startYear = TimeUtils.clearChristumNotation(intervalValues[0]);

            setEra(intervalValues[1], TimeUtils.END_PLACEHOLDER);
            setEra(intervalValues[0], TimeUtils.START_PLACEHOLDER);

            setDate(endYear, TimeUtils.END_PLACEHOLDER);
            setDate(startYear, TimeUtils.START_PLACEHOLDER);
        } else {
            String yearValue = TimeUtils.clearChristumNotation(preparedValue);

            setEra(preparedValue, TimeUtils.END_PLACEHOLDER);
            setEra(preparedValue, TimeUtils.START_PLACEHOLDER);

            setDate(yearValue, TimeUtils.END_PLACEHOLDER);
            setDate(yearValue, TimeUtils.START_PLACEHOLDER);
        }
    }

    @Override
    public String toString() {
        TreeSet<String> centurySet = getCenturySet();
        return CollectionUtils.treeSetToDbpediaString(centurySet);
    }

    private void setDate(String year, String position) {
        setCentury(year, position);
        setYear(year, position);
    }
}
