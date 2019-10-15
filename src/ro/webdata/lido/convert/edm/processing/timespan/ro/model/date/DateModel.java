package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.DateUtils;
import ro.webdata.lido.convert.edm.common.PrintMessages;
import ro.webdata.lido.convert.edm.common.constants.Constants;
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

    /** From Year, Month, Day */
    private static String START = "START";
    /** To Year, Month, Day */
    private static String END = "END";

    private String eraStart, eraEnd;
    private int yearStart, yearEnd;
    private String monthStart, monthEnd;
    private int dayStart, dayEnd;
    private boolean isInterval;

    private DateModel() {}

    public DateModel(String value, String order) {
        setDateModel(value, order);
    }

    //TODO: "s:17;a:1622;l:12;z:30"
    //TODO: "17/29 octombrie 1893"
    private void setDateModel(String value, String order) {
        String preparedValue = value
                .replaceAll(TimespanRegex.CHRISTUM_AD, Constants.EMPTY_VALUE_PLACEHOLDER)
                .replaceAll(TimespanRegex.CHRISTUM_BC, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
        String[] intervalValues = preparedValue
                .split(DateRegex.REGEX_DATE_INTERVAL_SEPARATOR);

        if (intervalValues.length == 2) {
            setIsInterval(true);
            setEra(value, START);
            setDate(intervalValues[0], order, START);
            setEra(value, END);
            setDate(intervalValues[1], order, END);
        } else {
            setIsInterval(false);
            setEra(value, START);
            setDate(preparedValue, order, START);
        }
    }

    @Override
    public String toString() {
        String start = yearStart
                + Constants.URL_SEPARATOR + monthStart
                + Constants.URL_SEPARATOR + dayStart
                + Constants.URL_SEPARATOR + getLinkEra(eraStart);

        if (isInterval) {
            String end = yearEnd
                    + Constants.URL_SEPARATOR + monthEnd
                    + Constants.URL_SEPARATOR + dayEnd
                    + Constants.URL_SEPARATOR + getLinkEra(eraEnd);

            return start + TimespanRegex.INTERVAL_SEPARATOR + end;
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

    private String getLinkEra(String value) {
        return value.contains(TimespanRegex.CHRISTUM_BC)
                ? "BC"
                : "AD";
    }

    private String getEra(String value) {
        return value.contains(TimespanRegex.CHRISTUM_BC)
                ? TimespanRegex.CHRISTUM_BC
                : TimespanRegex.CHRISTUM_AD;
    }

    private void setEra(String value, String position) {
        if (position.equals(START))
            this.eraStart = getEra(value);
        else if (position.equals(END))
            this.eraEnd = getEra(value);
    }

    private void setYear(int year, String position) {
        if (year > Constants.LAST_UPDATE_YEAR)
            PrintMessages.printTooBigYear(year);

        if (position.equals(START))
            this.yearStart = year;
        else if (position.equals(END))
            this.yearEnd = year;
    }

    private void setMonth(String month, String position) {
        if (position.equals(START))
            this.monthStart = month;
        else if (position.equals(END))
            this.monthEnd = month;
    }

    private void setDay(int day, String position) {
        if (position.equals(START))
            this.dayStart = day;
        else if (position.equals(END))
            this.dayEnd = day;
    }

    private void setIsInterval(boolean isInterval) {
        this.isInterval = isInterval;
    }
}
