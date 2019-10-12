package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.DateUtils;
import ro.webdata.lido.convert.edm.common.PrintMessages;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.ShortDateRegex;

/**
 * Used for date presented as month-year
 */
public class ShortDateModel {
    /** Year, Month order */
    public static String YM = "YM";
    /** Month, Year order */
    public static String MY = "MY";

    /** From Year, Month */
    private static String START = "START";
    /** To Year, Month */
    private static String END = "END";

    private int yearStart, yearEnd;
    private String monthStart, monthEnd;
    private boolean isInterval;

    private ShortDateModel() {}

    public ShortDateModel(String value, String order) {
        setDateModel(value, order);
    }

    // TODO: "instituit in decembrie 1915 - desfiintat in 1973"
    private void setDateModel(String value, String order) {
        String[] intervalValues = value.trim()
                .split(ShortDateRegex.REGEX_DATE_INTERVAL_SEPARATOR);

        if (intervalValues.length == 2) {
            setIsInterval(true);
            // Set the end date before the start one in order to store the year if we need
            setDate(intervalValues[1], order, END);
            setDate(intervalValues[0], order, START);
        } else {
            setIsInterval(false);
            setDate(value, order, START);
        }
    }

    @Override
    public String toString() {
        String start = yearStart + Constants.URL_SEPARATOR + monthStart;

        if (isInterval) {
            String end = yearEnd + Constants.URL_SEPARATOR + monthEnd;
            return start + Constants.INTERVAL_SEPARATOR + end;
        }

        return start;
    }

    private void setDate(String value, String order, String position) {
        String preparedValue = DateUtils.prepareDate(value);
        String[] values = preparedValue.split(TimespanRegex.REGEX_DATE_SEPARATOR);

        if (order.equals(MY)) {
            // For cases similar with "septembrie - octombrie 1919" we need to extract
            // the startYear from the section that stores the endYear
            int year = position.equals(START) && values.length == 1
                    ? yearEnd
                    : Integer.parseInt(values[1]);
            String month = DateUtils.getMonthName(values[0].trim());

            setYear(year, position);
            setMonth(month, position);
        }
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

    private void setIsInterval(boolean isInterval) {
        this.isInterval = isInterval;
    }
}
