package ro.webdata.lido.translator.processing.timespan.ro.model.date;

import ro.webdata.lido.translator.common.CollectionUtils;
import ro.webdata.lido.translator.common.DateUtils;
import ro.webdata.lido.translator.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.translator.processing.timespan.ro.model.TimePeriodModel;
import ro.webdata.lido.translator.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.translator.processing.timespan.ro.regex.date.ShortDateRegex;

import java.util.TreeSet;

/**
 * Used for date presented as month-year format<br/>
 * E.g.:<br/>
 *      * MY: "octombrie 1639", "ianuarie 632", "septembrie - octombrie 1919"
 */
public class ShortDateModel extends TimePeriodModel {
    private ShortDateModel() {}

    public ShortDateModel(String value, String order) {
        setDateModel(value, order);
    }

    // TODO: "instituit in decembrie 1915 - desfiintat in 1973"
    private void setDateModel(String value, String order) {
        String[] intervalValues = value.split(ShortDateRegex.REGEX_DATE_INTERVAL_SEPARATOR);

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

            setEra(value, TimeUtils.END_PLACEHOLDER);
            setEra(value, TimeUtils.START_PLACEHOLDER);

            setDate(preparedValue, order, TimeUtils.END_PLACEHOLDER);
            setDate(preparedValue, order, TimeUtils.START_PLACEHOLDER);
        }
    }

    @Override
    public String toString() {
        TreeSet<String> centurySet = getCenturySet();
        return CollectionUtils.treeSetToDbpediaString(centurySet);
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

            setCentury(year, position);
            setYear(year, position);
            setMonth(month, position);
        }
    }
}
