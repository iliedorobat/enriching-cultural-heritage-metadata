package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.DateUtils;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.TimeModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.DateRegex;

/**
 * Used for date presented as day-month-year or year-month-day format
 */
public class DateModel extends TimeModel {
    private DateModel() {}

    public DateModel(String value, String order) {
        setDateModel(value, order, DateRegex.REGEX_DATE_INTERVAL_SEPARATOR);
    }

    //TODO: "17/29 octombrie 1893"
    private void setDateModel(String value, String order, String intervalSeparator) {
        String[] intervalValues = value.split(intervalSeparator);

        if (intervalValues.length == 2) {
            setIsInterval(true);

            // Set the end time before the start time in order to use it
            // as start time too, if this is missing, but the end year exists
            String endValue = TimeUtils.clearChristumNotation(intervalValues[1]);
            String startValue = TimeUtils.clearChristumNotation(intervalValues[0]);

            setEra(intervalValues[1], TimeUtils.END_PLACEHOLDER);
            setEra(intervalValues[0], TimeUtils.START_PLACEHOLDER);

            setDate(endValue, order, TimeUtils.END_PLACEHOLDER);
            setDate(startValue, order, TimeUtils.START_PLACEHOLDER);
        } else {
            String preparedValue = TimeUtils.clearChristumNotation(value);

            setEra(value, TimeUtils.START_PLACEHOLDER);
            setDate(preparedValue, order, TimeUtils.START_PLACEHOLDER);
        }
    }

    @Override
    public String toString() {
        String start = yearStart
                + Constants.URL_SEPARATOR + monthStart
                + Constants.URL_SEPARATOR + dayStart
                + Constants.URL_SEPARATOR + TimeUtils.getEraLabel(eraStart);

        if (isInterval) {
            String end = yearEnd
                    + Constants.URL_SEPARATOR + monthEnd
                    + Constants.URL_SEPARATOR + dayEnd
                    + Constants.URL_SEPARATOR + TimeUtils.getEraLabel(eraEnd);

            return start + TimeUtils.INTERVAL_SEPARATOR + end;
        }

        return start;
    }

    // values.length == 4 if the month name is abbreviated (E.g.: "aug.")
    private void setDate(String value, String order, String position) {
        String year = null, month = null, day = null;
        String preparedValue = DateUtils.prepareDate(value);
        String[] values = preparedValue.split(TimespanRegex.REGEX_DATE_SEPARATOR);

        if (order.equals(TimeUtils.DMY_PLACEHOLDER)) {
            year = values.length == 4 ? values[3] : values[2];
            month = DateUtils.getMonthName(values[1].trim());
            day = values[0];
        } else if (order.equals(TimeUtils.YMD_PLACEHOLDER)) {
            year = values[0];
            month = DateUtils.getMonthName(values[1].trim());
            day = values.length == 4 ? values[3] : values[2];
        }

        setYear(year, position);
        setMonth(month, position);
        setDay(day, position);
    }
}
