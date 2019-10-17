package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.DateUtils;
import ro.webdata.lido.convert.edm.common.PrintMessages;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimespanUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimespanRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.DateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.ShortDateRegex;

/**
 * Used for date presented as month-year
 */
//TODO: extending from a common date class
public class ShortDateModel {
    /** Year, Month order */
    public static String YM = "YM";
    /** Month, Year order */
    public static String MY = "MY";

    /** From Year, Month */
    private static String START = "START";
    /** To Year, Month */
    private static String END = "END";

    private String eraStart, eraEnd;
    private int yearStart, yearEnd;
    private String monthStart, monthEnd;
    private boolean isInterval;

    private ShortDateModel() {}

    public ShortDateModel(String value, String order) {
        setDateModel(value, order, ShortDateRegex.REGEX_DATE_INTERVAL_SEPARATOR);
    }

    // TODO: "instituit in decembrie 1915 - desfiintat in 1973"
    private void setDateModel(String value, String order, String intervalSeparator) {
        String[] intervalValues = value.split(intervalSeparator);

        if (intervalValues.length == 2) {
            String startValue = TimespanUtils.clearChristumNotation(intervalValues[0]);
            String endValue = TimespanUtils.clearChristumNotation(intervalValues[1]);

            setIsInterval(true);
            // Set the end date before the start one to store the year
            // in order to use it as a end year and start year too
            setEra(intervalValues[1], END);
            setDate(endValue, order, END);
            setEra(intervalValues[0], START);
            setDate(startValue, order, START);
        } else {
            String preparedValue = TimespanUtils.clearChristumNotation(value);

            setIsInterval(false);
            setEra(value, START);
            setDate(preparedValue, order, START);
        }
    }

    @Override
    public String toString() {
        String start = yearStart
                + Constants.URL_SEPARATOR + monthStart
                + Constants.URL_SEPARATOR + TimespanUtils.getEraName(eraStart);

        if (isInterval) {
            String end = yearEnd
                    + Constants.URL_SEPARATOR + monthEnd
                    + Constants.URL_SEPARATOR + TimespanUtils.getEraName(eraEnd);

            return start + TimespanRegex.INTERVAL_SEPARATOR + end;
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

    private void setEra(String value, String position) {
        if (position.equals(START)) {
            if (!value.contains(TimespanRegex.CHRISTUM_BC) && this.eraEnd != null)
                this.eraStart = TimespanUtils.getEraPlaceholder(this.eraEnd);
            else
                this.eraStart = TimespanUtils.getEraPlaceholder(value);
        } else if (position.equals(END)) {
            this.eraEnd = TimespanUtils.getEraPlaceholder(value);
        }
    }

    private void setYear(int year, String position) {
        if (position.equals(START)) {
            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimespanRegex.CHRISTUM_AD))
                PrintMessages.printTooBigYear(year);
            this.yearStart = year;
        } else if (position.equals(END)) {
            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimespanRegex.CHRISTUM_AD))
                PrintMessages.printTooBigYear(year);
            this.yearEnd = year;
        }
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
