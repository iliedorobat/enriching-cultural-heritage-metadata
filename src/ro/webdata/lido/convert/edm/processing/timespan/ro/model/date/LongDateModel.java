package ro.webdata.lido.convert.edm.processing.timespan.ro.model.date;

import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.LongDateRegex;

/**
 * Used for those time intervals that are stored as a "long" date
 * format (having a century, a year, a month and a day)<br/>
 * E.g.: "s:17;a:1622;l:12;z:30"
 */
public class LongDateModel {
    private static final String DATE_SEPARATOR = " ## ";
    private static final String SUFFIX_CENTURY = "s:";
    private static final String SUFFIX_YEAR = "a:";
    private static final String SUFFIX_MONTH = "l:";
    private static final String SUFFIX_DAY = "z:";

    private String era;
    private String century;
    private int year;
    private String month;
    private int day;

    private LongDateModel() {}

    public LongDateModel(String value) {
        String preparedValue = TimeUtils.clearChristumNotation(value);
        String[] values = preparedValue.split(LongDateRegex.DATE_SEPARATOR);

        setEra(value);
        for (String str : values) {
            if (str.contains(SUFFIX_CENTURY))
                setCentury(str);
            else if (str.contains(SUFFIX_YEAR))
                setYear(str);
            else if (str.contains(SUFFIX_MONTH))
                setMonth(str);
            else if (str.contains(SUFFIX_DAY))
                setDay(str);
        }
    }

    public String getDate() {
        return year
                + Constants.URL_SEPARATOR + month
                + Constants.URL_SEPARATOR + day
                + Constants.URL_SEPARATOR + TimeUtils.getEraLabel(era);
    }

    public String getEra() {
        return "century " + century + " " + TimeUtils.getEraLabel(era);
    }

    @Override
    public String toString() {
        return getEra() + DATE_SEPARATOR + getDate();
    }

    private void setEra(String value) {
        this.era = TimeUtils.getEraName(value);
    }

    private void setCentury(String value) {
        this.century = value
                .replaceAll(SUFFIX_CENTURY, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
    }

    private void setYear(String value) {
        String yearStr = value
                .replaceAll(SUFFIX_YEAR, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
        this.year = Integer.parseInt(yearStr);
    }

    private void setMonth(String value) {
        this.month = value
                .replaceAll(SUFFIX_MONTH, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
    }

    private void setDay(String value) {
        String dayStr = value
                .replaceAll(SUFFIX_DAY, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
        this.day = Integer.parseInt(dayStr);
    }
}
