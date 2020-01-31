package ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.date;

import ro.webdata.translator.edm.approach.event.lido.common.CollectionUtils;
import ro.webdata.translator.edm.approach.event.lido.common.DateUtils;
import ro.webdata.common.utils.MathUtils;
import ro.webdata.translator.edm.approach.event.lido.common.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.TimeUtils;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.date.LongDateRegex;

import java.util.TreeSet;

/**
 * Used for those time intervals that are stored as a "long" date
 * format (having a century, a year, a month and a day)<br/>
 * E.g.: "s:17;a:1622;l:12;z:30"
 */
public class LongDateModel {
    private static final String SUFFIX_CENTURY = "s:";
    private static final String SUFFIX_YEAR = "a:";
    private static final String SUFFIX_MONTH = "l:";
    private static final String SUFFIX_DAY = "z:";

    private String era;
    private Integer century;
    private Integer year;
    private String month;
    private int day;

    private LongDateModel() {}

    public LongDateModel(String value) {
        String preparedValue = TimeUtils.clearChristumNotation(value);
        String[] values = preparedValue.split(LongDateRegex.DATE_SEPARATOR);

        setEra(value);
        for (String str : values) {
            str = str.toLowerCase();
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

    @Override
    public String toString() {
        TreeSet<String> centurySet = getCenturySet();
        return CollectionUtils.treeSetToDbpediaString(centurySet);
    }

    private TreeSet<String> getCenturySet() {
        TreeSet<String> centurySet = new TreeSet<>();

        String centuryDbpedia = MathUtils.getOrdinal(this.century)
                + Constants.DBPEDIA_CENTURY_PLACEHOLDER;
        centurySet.add(centuryDbpedia);

        return centurySet;
    }

    private void setEra(String value) {
        this.era = TimeUtils.getEraName(value);
    }

    private void setCentury(String value) {
        String centuryStr = value
                .replaceAll(SUFFIX_CENTURY, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
        try {
            this.century = Integer.parseInt(centuryStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setYear(String value) {
        String yearStr = value
                .replaceAll(SUFFIX_YEAR, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
        try {
            this.year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setMonth(String value) {
        String month = value
                .replaceAll(SUFFIX_MONTH, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
        this.month = DateUtils.getMonthName(month);
    }

    private void setDay(String value) {
        String dayStr = value
                .replaceAll(SUFFIX_DAY, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
        try {
            this.day = Integer.parseInt(dayStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
