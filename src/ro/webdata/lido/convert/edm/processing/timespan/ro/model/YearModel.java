package ro.webdata.lido.convert.edm.processing.timespan.ro.model;

import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;

//TODO: check if ageFromValue <= ageToValue (if not, make it CHRISTUM_ANTE)
public class YearModel extends TimeModel {
    // Used to separate the minus sign from the dash separator "-2 - -14 p.chr"; "-2 p.chr - -14 p.chr"
    private static final String REGEX_AGE_SEPARATOR = "(?<=[\\w\\W&&[^ -]])[ ]*-[ ]*";

    private YearModel() {}

    public YearModel(String value) {
        setYearModel(value);
    }

    //TODO: "anul 13=1800/1801"
    //TODO: "110/109 a. chr."
    private void setYearModel(String value) {
        String[] intervalValues = value.split(TimespanRegex.REGEX_INTERVAL_DELIMITER);

        if (intervalValues.length == 2) {
            setIsInterval(true);

            String endYear = TimeUtils.clearChristumNotation(intervalValues[1]);
            String startYear = TimeUtils.clearChristumNotation(intervalValues[0]);

            setEra(intervalValues[1], TimeUtils.END_PLACEHOLDER);
            setEra(intervalValues[0], TimeUtils.START_PLACEHOLDER);

            setYear(endYear, TimeUtils.END_PLACEHOLDER);
            setYear(startYear, TimeUtils.START_PLACEHOLDER);
        } else {
            String year = TimeUtils.clearChristumNotation(intervalValues[0]);

            setEra(intervalValues[0], TimeUtils.START_PLACEHOLDER);
            setYear(year, TimeUtils.START_PLACEHOLDER);
        }
    }

    @Override
    public String toString() {
        String start = this.yearStart
                + Constants.URL_SEPARATOR + TimeUtils.getEraLabel(this.eraStart);

        if (isInterval) {
            String end = this.yearEnd
                    + Constants.URL_SEPARATOR + TimeUtils.getEraLabel(this.eraEnd);
            return start + TimeUtils.INTERVAL_SEPARATOR + end;
        }

        return start;
    }
}
