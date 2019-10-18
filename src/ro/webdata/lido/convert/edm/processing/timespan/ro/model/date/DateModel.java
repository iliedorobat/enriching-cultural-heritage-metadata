package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.DateUtils;
import ro.webdata.lido.convert.edm.common.PrintMessages;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.DateRegex;

/**
 * Used for date presented as day-month-year or year-month-day
 */
//TODO: extending from a common date class
public class DateModel {
    /** Year, Month, Day order */
    public static String YMD = "YMD";
    /** Day, Month, Year order */
    public static String DMY = "DMY";

    private String eraStart, eraEnd;
    private int yearStart, yearEnd;
    private String monthStart, monthEnd;
    private int dayStart, dayEnd;
    private boolean isInterval;

    private DateModel() {}

    public DateModel(String value, String order) {
        setDateModel(value, order, DateRegex.REGEX_DATE_INTERVAL_SEPARATOR);
    }

    //TODO: "17/29 octombrie 1893"
    private void setDateModel(String value, String order, String intervalSeparator) {
        String[] intervalValues = value.split(intervalSeparator);

        if (intervalValues.length == 2) {
            String startValue = TimeUtils.clearChristumNotation(intervalValues[0]);
            String endValue = TimeUtils.clearChristumNotation(intervalValues[1]);

            setIsInterval(true);
            // Set the end date before the start one to store the year
            // in order to use it as a end year and start year too
            setEra(intervalValues[1], TimeUtils.END);
            setDate(endValue, order, TimeUtils.END);
            setEra(intervalValues[0], TimeUtils.START);
            setDate(startValue, order, TimeUtils.START);
        } else {
            String preparedValue = TimeUtils.clearChristumNotation(value);

            setIsInterval(false);
            setEra(value, TimeUtils.START);
            setDate(preparedValue, order, TimeUtils.START);
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

    private void setDate(String value, String order, String position) {
        String preparedValue = DateUtils.prepareDate(value);
        // values.length == 4 if the month name is abbreviated (E.g.: "aug.")
        String[] values = preparedValue.split(TimespanRegex.REGEX_DATE_SEPARATOR);

        // Default values for "YMD" order
        int year = Integer.parseInt(values[0].trim());
        String month = DateUtils.getMonthName(values[1].trim());
        int day = values.length == 4
                ? Integer.parseInt(values[3].trim())
                : Integer.parseInt(values[2].trim());

        if (order.equals(DMY)) {
            year = values.length == 4
                    ? Integer.parseInt(values[3].trim())
                    : Integer.parseInt(values[2].trim());
            month = DateUtils.getMonthName(values[1].trim());
            day = Integer.parseInt(values[0].trim());
        }

        setYear(year, position);
        setMonth(month, position);
        setDay(day, position);
    }

    private void setEra(String value, String position) {
        if (position.equals(TimeUtils.START)) {
            if (!value.contains(TimeUtils.CHRISTUM_BC_NAME) && this.eraEnd != null)
                this.eraStart = TimeUtils.getEraName(this.eraEnd);
            else
                this.eraStart = TimeUtils.getEraName(value);
        } else if (position.equals(TimeUtils.END)) {
            this.eraEnd = TimeUtils.getEraName(value);
        }
    }

    private void setYear(int year, String position) {
        if (position.equals(TimeUtils.START)) {
            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimeUtils.CHRISTUM_AD_NAME))
                PrintMessages.printTooBigYear(year);
            this.yearStart = year;
        } else if (position.equals(TimeUtils.END)) {
            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimeUtils.CHRISTUM_AD_NAME))
                PrintMessages.printTooBigYear(year);
            this.yearEnd = year;
        }
    }

    private void setMonth(String month, String position) {
        if (position.equals(TimeUtils.START))
            this.monthStart = month;
        else if (position.equals(TimeUtils.END))
            this.monthEnd = month;
    }

    private void setDay(int day, String position) {
        if (position.equals(TimeUtils.START))
            this.dayStart = day;
        else if (position.equals(TimeUtils.END))
            this.dayEnd = day;
    }

    private void setIsInterval(boolean isInterval) {
        this.isInterval = isInterval;
    }
}
