package ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.translator.edm.approach.event.lido.common.CollectionUtils;
import ro.webdata.translator.edm.approach.event.lido.common.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.AgeModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.TimespanModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.YearModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.date.DateModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.date.LongDateModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.date.ShortDateModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.imprecise.DatelessModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.imprecise.InaccurateYearModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.timePeriod.CenturyModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.timePeriod.MillenniumModel;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.*;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.date.DateRegex;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.date.LongDateRegex;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.date.ShortDateRegex;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.imprecise.DatelessRegex;
import ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.regex.imprecise.InaccurateYearRegex;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimespanUtils {
    /**
     * TODO: doc<br/>
     * In the matching process the first matched value need to be the interval type,
     * followed by the ordinal values, respecting the following order:
     * <ol>
     *     <li>Map every unknown value in order to clear the list by junk elements</li>
     *     <li>Map every date-like value</li>
     *     <li>Map every century and millennium age-like value</li>
     *     <li>Map every epoch-like value</li>
     * </ol>
     * @param original The original value taken from "lido:displayDate" record
     */
    public static TreeSet<String> getTimespanSet(String original) {
        String value = StringUtils.stripAccents(original);
        TimespanModel timespanModel = new TimespanModel(value);
        timespanModel = getMatchedValues(timespanModel, UnknownRegex.UNKNOWN);

        timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_DMY_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_YMD_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, ShortDateRegex.DATE_MY_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_DMY_OPTIONS);
        timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_YMD_OPTIONS);
        timespanModel = getMatchedValues(timespanModel, ShortDateRegex.DATE_MY_OPTIONS);
        timespanModel = getMatchedValues(timespanModel, LongDateRegex.LONG_DATE_OPTIONS);

        timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.CENTURY_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.CENTURY_OPTIONS);
        timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.MILLENNIUM_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.MILLENNIUM_OPTIONS);
        timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.OTHER_CENTURY_ROMAN_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.OTHER_CENTURY_ROMAN_OPTIONS);
        for (int i = 0; i < AgeRegex.AGE_OPTIONS.length; i++) {
            timespanModel = getMatchedValues(timespanModel, AgeRegex.AGE_OPTIONS[i]);
        }

        timespanModel = getMatchedValues(timespanModel, DatelessRegex.DATELESS);
        timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.AFTER_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.BEFORE_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.APPROX_AGES_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.AFTER);
        timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.BEFORE);
        timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.APPROX_AGES_OPTIONS);

        // Firstly, the years consisting of 3 - 4 digits need to be processed
        timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_3_4_DIGITS_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_3_4_DIGITS_SPECIAL_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_3_4_DIGITS_OPTIONS);
        // Secondly, the years consisting of 2 digits need to be processed
        timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_2_DIGITS_INTERVAL);
        timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_2_DIGITS_OPTIONS);
        // This call need to be made after all the years processing !!!
        timespanModel = getMatchedValues(timespanModel, YearRegex.UNKNOWN_YEARS);

        return timespanModel.getTimespanSet();
    }

    //TODO: "1/2 mil. 5 - sec. I al mil. 4 a.Chr."
    //TODO: "2 a.chr - 14 p.chr"
    private static TimespanModel getMatchedValues(TimespanModel timespanModel, String regex) {
        String initialValue = timespanModel.getResidualValue();
        initialValue = TimeSanitizeUtils.sanitizeValue(initialValue, regex)
                .replaceAll(TimespanRegex.AGE_BC, TimeUtils.CHRISTUM_BC_PLACEHOLDER)
                .replaceAll(TimespanRegex.AGE_AD, TimeUtils.CHRISTUM_AD_PLACEHOLDER);

        TreeSet<String> matchedSet = timespanModel.getTimespanSet();
        String residualValue = initialValue
                .replaceAll(regex, Constants.EMPTY_VALUE_PLACEHOLDER);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(initialValue);

        while (matcher.find()) {
            String group = matcher.group();
            String matchedItems = prepareValue(group, regex);

            if (!matchedItems.equals(group) && matchedItems.length() > 0) {
                String[] matchedList = matchedItems.split(CollectionUtils.STRING_LIST_SEPARATOR);
                matchedSet.addAll(Arrays.asList(matchedList));
            } else if (matchedItems.equals(group)) {
                System.err.println("The following group has not been processed: \"" + group + "\"");
            }
        }

        return new TimespanModel(matchedSet, residualValue.trim());
    }

    /**
     * Format date-like, year-like, centuries and millenniums values
     * @param value The original value
     * @param regex A regular expression
     * @return The formatted value
     */
    private static String prepareValue(String value, String regex) {
        String prepared = prepareAges(value, regex);
        prepared = prepareDateTime(prepared, regex);
        prepared = prepareTimePeriod(prepared, regex);

        return prepared;
    }

    /**
     * Format the year-like value
     * @param value The original value
     * @param regex A regular expression
     * @return The formatted value
     */
    private static String prepareAges(String value, String regex) {
        switch (regex) {
            case InaccurateYearRegex.AFTER:
            case InaccurateYearRegex.AFTER_INTERVAL:
            case InaccurateYearRegex.APPROX_AGES_INTERVAL:
            case InaccurateYearRegex.APPROX_AGES_OPTIONS:
            case InaccurateYearRegex.BEFORE:
            case InaccurateYearRegex.BEFORE_INTERVAL:
                InaccurateYearModel inaccurateYearModel = new InaccurateYearModel(value);
                return inaccurateYearModel.toString();
            case DatelessRegex.DATELESS:
            case YearRegex.UNKNOWN_YEARS:
            case UnknownRegex.UNKNOWN:
                DatelessModel datelessModel = new DatelessModel(value);
                return datelessModel.toString();
            case YearRegex.YEAR_2_DIGITS_INTERVAL:
            case YearRegex.YEAR_2_DIGITS_OPTIONS:
            case YearRegex.YEAR_3_4_DIGITS_INTERVAL:
            case YearRegex.YEAR_3_4_DIGITS_SPECIAL_INTERVAL:
            case YearRegex.YEAR_3_4_DIGITS_OPTIONS:
                YearModel yearModel = new YearModel(value);
                return yearModel.toString();
            default:
                return value;
        }
    }

    /**
     * Format the date-like value
     * @param value The original value
     * @param regex A regular expression
     * @return The formatted value
     */
    private static String prepareDateTime(String value, String regex) {
        DateModel dateModel;

        switch (regex) {
            case DateRegex.DATE_DMY_INTERVAL:
            case DateRegex.DATE_DMY_OPTIONS:
                dateModel = new DateModel(value, TimeUtils.DMY_PLACEHOLDER);
                return dateModel.toString();
            case DateRegex.DATE_YMD_INTERVAL:
            case DateRegex.DATE_YMD_OPTIONS:
                dateModel = new DateModel(value, TimeUtils.YMD_PLACEHOLDER);
                return dateModel.toString();
            case ShortDateRegex.DATE_MY_INTERVAL:
            case ShortDateRegex.DATE_MY_OPTIONS:
                ShortDateModel shortDateModel = new ShortDateModel(value, TimeUtils.MY_PLACEHOLDER);
                return shortDateModel.toString();
            case LongDateRegex.LONG_DATE_OPTIONS:
                LongDateModel longDateModel = new LongDateModel(value);
                return longDateModel.toString();
            default:
                return value;
        }
    }

    /**
     * Format centuries and millenniums
     * @param value The original value
     * @param regex A regular expression
     * @return The list of formatted values
     */
    private static String prepareTimePeriod(String value, String regex) {
        switch (regex) {
            case TimePeriodRegex.CENTURY_INTERVAL:
            case TimePeriodRegex.CENTURY_OPTIONS:
            case TimePeriodRegex.OTHER_CENTURY_ROMAN_INTERVAL:
            case TimePeriodRegex.OTHER_CENTURY_ROMAN_OPTIONS:
                CenturyModel centuryModel = new CenturyModel(value);
                return centuryModel.toString();
            case TimePeriodRegex.MILLENNIUM_INTERVAL:
            case TimePeriodRegex.MILLENNIUM_OPTIONS:
                MillenniumModel millenniumModel = new MillenniumModel(value);
                return millenniumModel.toString();
            case AgeRegex.AURIGNACIAN_CULTURE:
            case AgeRegex.BRONZE_AGE:
            case AgeRegex.CHALCOLITHIC_AGE:
            case AgeRegex.FRENCH_CONSULATE_AGE:
            case AgeRegex.HALLSTATT_CULTURE:
            case AgeRegex.INTERWAR_PERIOD:
            case AgeRegex.MESOLITHIC_AGE:
            case AgeRegex.MIDDLE_AGES:
            case AgeRegex.MODERN_AGES:
            case AgeRegex.NEOLITHIC_AGE:
            case AgeRegex.NERVA_ANTONINE_DYNASTY:
            case AgeRegex.PLEISTOCENE_AGE:
            case AgeRegex.PTOLEMAIC_DYNASTY:
            case AgeRegex.RENAISSANCE:
            case AgeRegex.ROMAN_EMPIRE_AGE:
            case AgeRegex.WW_I_PERIOD:
            case AgeRegex.WW_II_PERIOD:
                AgeModel ageModel = new AgeModel(value, regex);
                return ageModel.toString();
            default:
                return value;
        }
    }
}
