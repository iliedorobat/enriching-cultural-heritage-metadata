package ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.date;

import ro.webdata.translator.edm.approach.event.lido.common.CollectionUtils;
import ro.webdata.translator.edm.approach.event.lido.common.DateUtils;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.TimeUtils;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.TimePeriodModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.date.DateRegex;

import java.util.TreeSet;

/**
 * Used for date presented as day-month-year or year-month-day format.<br/>
 * E.g.:<br/>
 *      * DMY: "14 ianuarie 1497", "21/01/1916", "01.11.1668", "1.09.1607"<br/>
 *      * YMD: "1974-05-05", "1891 decembrie 07", "1738, MAI, 4"
 */
public class DateModel extends TimePeriodModel {
    private DateModel() {}

    public DateModel(String value, String order) {
        setDateModel(value, order);
    }

    //TODO: "17/29 octombrie 1893"
    private void setDateModel(String value, String order) {
        String[] intervalValues = value.split(DateRegex.REGEX_DATE_INTERVAL_SEPARATOR);

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

        setCentury(year, position);
        setYear(year, position);
        setMonth(month, position);
        setDay(day, position);
    }
}
