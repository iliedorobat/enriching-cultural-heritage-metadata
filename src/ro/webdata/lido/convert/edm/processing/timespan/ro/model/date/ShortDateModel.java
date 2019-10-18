package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.DateUtils;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.TimeModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.ShortDateRegex;

/**
 * Used for date presented as month-year format
 */
public class ShortDateModel extends TimeModel {
    private ShortDateModel() {}

    public ShortDateModel(String value, String order) {
        setDateModel(value, order, ShortDateRegex.REGEX_DATE_INTERVAL_SEPARATOR);
    }

    // TODO: "instituit in decembrie 1915 - desfiintat in 1973"
    private void setDateModel(String value, String order, String intervalSeparator) {
        String[] intervalValues = value.split(intervalSeparator);

        if (intervalValues.length == 2) {
            setIsInterval(true);

            // Set the end time before the start time in order to use it
            // as start time too, if this is missing, but the end year exists
            String startValue = TimeUtils.clearChristumNotation(intervalValues[0]);
            String endValue = TimeUtils.clearChristumNotation(intervalValues[1]);

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
                + Constants.URL_SEPARATOR + TimeUtils.getEraLabel(eraStart);

        if (isInterval) {
            String end = yearEnd
                    + Constants.URL_SEPARATOR + monthEnd
                    + Constants.URL_SEPARATOR + TimeUtils.getEraLabel(eraEnd);

            return start + TimeUtils.INTERVAL_SEPARATOR + end;
        }

        return start;
    }

    private void setDate(String value, String order, String position) {
        String preparedValue = DateUtils.prepareDate(value);
        String[] values = preparedValue.split(TimespanRegex.REGEX_DATE_SEPARATOR);

        if (order.equals(TimeUtils.MY_PLACEHOLDER)) {
            // For cases similar with "septembrie - octombrie 1919", we need to extract
            // the startYear from the section that stores the endYear
            String year = position.equals(TimeUtils.START_PLACEHOLDER) && values.length == 1
                    ? String.valueOf(this.yearEnd)
                    : values[1];
            String month = DateUtils.getMonthName(values[0].trim());

            setYear(year, position);
            setMonth(month, position);
        }
    }
}
