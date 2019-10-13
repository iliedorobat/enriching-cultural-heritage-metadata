package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.DateUtils;
import ro.webdata.lido.convert.edm.common.PrintMessages;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.FullDateRegex;

public class FullDateModel {
    /** Year, Month, Day order */
    public static String YMD = "YMD";
    /** Day, Month, Year order */
    public static String DMY = "DMY";

    /** From Year, Month, Day */
    private static String START = "START";
    /** To Year, Month, Day */
    private static String END = "END";

    private int yearStart, yearEnd;
    private String monthStart, monthEnd;
    private int dayStart, dayEnd;
    private boolean isInterval;

    private FullDateModel() {}

    public FullDateModel(String value, String order) {
        setDateModel(value, order);
    }

    //TODO: "s:17;a:1622;l:12;z:30"
    //TODO: "17/29 octombrie 1893"
    private void setDateModel(String value, String order) {
        String[] intervalValues = value.trim()
                .split(FullDateRegex.REGEX_DATE_INTERVAL_SEPARATOR);

        if (intervalValues.length == 2) {
            setIsInterval(true);
            setDate(intervalValues[0], order, START);
            setDate(intervalValues[1], order, END);
        } else {
            setIsInterval(false);
            setDate(value, order, START);
        }
    }

    @Override
    public String toString() {
        String start = yearStart + Constants.URL_SEPARATOR + monthStart + Constants.URL_SEPARATOR + dayStart;

        if (isInterval) {
            String end = yearEnd + Constants.URL_SEPARATOR + monthEnd + Constants.URL_SEPARATOR + dayEnd;
            return start + Constants.INTERVAL_SEPARATOR + end;
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
