package ro.webdata.lido.translator.processing.timespan.ro.model;

import ro.webdata.lido.translator.common.MathUtils;
import ro.webdata.lido.translator.common.constants.Constants;
import ro.webdata.lido.translator.common.constants.NSConstants;
import ro.webdata.lido.translator.processing.timespan.ro.TimeUtils;
import ro.webdata.lido.translator.processing.timespan.ro.regex.TimespanRegex;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: transform it into an util function
public class TimePeriodModel extends TimeModel {
    private static final String[] REGEX_LIST = {
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.CENTURY_NOTATION,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.MILLENNIUM_NOTATION,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.FIRST_HALF,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.SECOND_HALF,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.MIDDLE_OF,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.FIRST_QUARTER,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.SECOND_QUARTER,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.THIRD_QUARTER,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.FORTH_QUARTER,
            TimespanRegex.CASE_INSENSITIVE + TimespanRegex.AGES_GROUP_SUFFIX,
            "[\\.,;\\?!\\p{Space}]*"
    };

    /**
     * Get the time period value from the input value
     * @param value The initial value
     * @return The prepared value (E.g.: i, ii, iii, iv etc.)
     */
    public static String sanitizeTimePeriod(String value) {
        String preparedValue = value;

        for (String regex : REGEX_LIST) {
            Pattern suffixPattern = Pattern.compile(regex);
            Matcher suffixMatcher = suffixPattern.matcher(preparedValue);

            while (suffixMatcher.find()) {
                String group = suffixMatcher.group();
                preparedValue = preparedValue.replaceAll(group, Constants.EMPTY_VALUE_PLACEHOLDER);
            }
        }

        return preparedValue;
    }

    /**
     * Transform a time period into number.
     * @param timePeriod The original time period (E.g.: 'i', '5' etc.)
     * @return The number that represents the time period (E.g.: 5, 9 etc.)
     */
    public static Integer timePeriodToNumber(String timePeriod) {
        Integer value = null;

        try {
            value = Integer.parseInt(timePeriod);
        } catch (Exception e) {
            value = MathUtils.romanToInt(timePeriod);
        }

        return value;
    }

//    public TreeSet<String> getYearSet() {
//        TreeSet<String> yearSet = new TreeSet<>();
//
//        if (this.yearStart != null && this.yearEnd != null) {
//            pushSameBc(this.eraStart, this.eraEnd, this.yearStart, this.yearEnd, Constants.EMPTY_VALUE_PLACEHOLDER, yearSet);
//            pushSameAd(this.eraStart, this.eraEnd, this.yearStart, this.yearEnd, Constants.EMPTY_VALUE_PLACEHOLDER, yearSet);
//            pushBcAd(this.eraStart, this.eraEnd, this.yearStart, this.yearEnd, Constants.EMPTY_VALUE_PLACEHOLDER, yearSet);
//            pushAdBc(this.eraStart, this.eraEnd, this.yearStart, this.yearEnd, Constants.EMPTY_VALUE_PLACEHOLDER, yearSet);
//        }
//
//        return yearSet;
//    }

    public TreeSet<String> getCenturySet() {
        TreeSet<String> centurySet = new TreeSet<>();

        if (this.centuryStart != null && this.centuryEnd != null) {
            pushSameBc(this.eraStart, this.eraEnd, this.centuryStart, this.centuryEnd, Constants.CENTURY_PLACEHOLDER, centurySet);
            pushSameAd(this.eraStart, this.eraEnd, this.centuryStart, this.centuryEnd, Constants.CENTURY_PLACEHOLDER, centurySet);
            pushBcAd(this.eraStart, this.eraEnd, this.centuryStart, this.centuryEnd, Constants.CENTURY_PLACEHOLDER, centurySet);
            pushAdBc(this.eraStart, this.eraEnd, this.centuryStart, this.centuryEnd, Constants.CENTURY_PLACEHOLDER, centurySet);
        } else {
            pushUnknown(centurySet);
        }

        return centurySet;
    }

    public TreeSet<String> getMillenniumSet() {
        TreeSet<String> millenniumSet = new TreeSet<>();

        if (this.millenniumStart != null && this.millenniumEnd != null) {
            pushSameBc(this.eraStart, this.eraEnd, this.millenniumStart, this.millenniumEnd, Constants.MILLENNIUM_PLACEHOLDER, millenniumSet);
            pushSameAd(this.eraStart, this.eraEnd, this.millenniumStart, this.millenniumEnd, Constants.MILLENNIUM_PLACEHOLDER, millenniumSet);
            pushBcAd(this.eraStart, this.eraEnd, this.millenniumStart, this.millenniumEnd, Constants.MILLENNIUM_PLACEHOLDER, millenniumSet);
            pushAdBc(this.eraStart, this.eraEnd, this.millenniumStart, this.millenniumEnd, Constants.MILLENNIUM_PLACEHOLDER, millenniumSet);
        } else {
            pushUnknown(millenniumSet);
        }

        return millenniumSet;
    }

    private void pushUnknown(TreeSet<String> timeSet) {
        timeSet.add(NSConstants.NS_REPO_RESOURCE_TIMESPAN_UNKNOWN);
    }

    /**
     * Push a time period where the starting era and the ending era are both
     * TimeUtils.CHRISTUM_AD_PLACEHOLDER<br/>
     * E.g.:<br/>
     *      * sec. VI p.Chr - sec. II-lea p.Chr<br/>
     *      * sec. II p.Chr - sec. VI-lea p.Chr
     * @param eraStart The starting era
     * @param eraEnd The ending era
     * @param timeStart The starting time period
     * @param timeEnd The ending time period
     * @param timePlaceholder Placeholder for a time period:<br/>
     *                        * Constants.CENTURY_PLACEHOLDER<br/>
     *                        * Constants.MILLENNIUM_PLACEHOLDER<br/>
     *                        * Constants.EMPTY_VALUE_PLACEHOLDER
     * @param timeSet The set where DBPedia time periods will be stored
     */
    private void pushSameAd(
            String eraStart,
            String eraEnd,
            Integer timeStart,
            Integer timeEnd,
            String timePlaceholder,
            TreeSet<String> timeSet
    ) {
        if (eraStart.equals(TimeUtils.CHRISTUM_AD_PLACEHOLDER)
                && eraEnd.equals(TimeUtils.CHRISTUM_AD_PLACEHOLDER)) {
            int start = Math.min(timeStart, timeEnd);
            int end = Math.max(timeStart, timeEnd);

            for (int timePeriod = start; timePeriod <= end; timePeriod++) {
                String timeDbpedia = MathUtils.getOrdinal(timePeriod)
                        + timePlaceholder;
                timeSet.add(timeDbpedia);
            }
        }
    }

    /**
     * Push a time period where the starting era and the ending era are both
     * TimeUtils.CHRISTUM_BC_PLACEHOLDER<br/>
     * E.g.:<br/>
     *      * sec. VI a.Chr - sec. II-lea a.Chr<br/>
     *      * sec. II a.Chr - sec. VI-lea a.Chr
     * @param eraStart The starting era
     * @param eraEnd The ending era
     * @param timeStart The starting time period
     * @param timeEnd The ending time period
     * @param timePlaceholder Placeholder for a time period:<br/>
     *                        * Constants.CENTURY_PLACEHOLDER<br/>
     *                        * Constants.MILLENNIUM_PLACEHOLDER<br/>
     *                        * Constants.EMPTY_VALUE_PLACEHOLDER
     * @param timeSet The set where DBPedia time periods will be stored
     */
    private void pushSameBc(
            String eraStart,
            String eraEnd,
            Integer timeStart,
            Integer timeEnd,
            String timePlaceholder,
            TreeSet<String> timeSet
    ) {
        if (eraStart.equals(TimeUtils.CHRISTUM_BC_PLACEHOLDER)
                && eraEnd.equals(TimeUtils.CHRISTUM_BC_PLACEHOLDER)) {
            int start = Math.max(timeStart, timeEnd);
            int end = Math.min(timeStart, timeEnd);

            for (int timePeriod = start; timePeriod >= end; timePeriod--) {
                String timeDbpedia = MathUtils.getOrdinal(timePeriod)
                        + timePlaceholder
                        + "_" + TimeUtils.CHRISTUM_BC_LABEL;
                timeSet.add(timeDbpedia);
            }
        }
    }

    private void pushBcAd(
            String eraStart,
            String eraEnd,
            Integer timeStart,
            Integer timeEnd,
            String timePlaceholder,
            TreeSet<String> timeSet
    ) {
        // sec. VI a.Chr - sec. II-lea p.Chr
        if (eraStart.equals(TimeUtils.CHRISTUM_BC_PLACEHOLDER)
                && eraEnd.equals(TimeUtils.CHRISTUM_AD_PLACEHOLDER)) {
            int start = timeStart;
            int end = 1;
            pushSameBc(
                    TimeUtils.CHRISTUM_BC_PLACEHOLDER,
                    TimeUtils.CHRISTUM_BC_PLACEHOLDER,
                    start,
                    end,
                    timePlaceholder,
                    timeSet
            );

            start = 1;
            end = timeEnd;
            pushSameAd(
                    TimeUtils.CHRISTUM_AD_PLACEHOLDER,
                    TimeUtils.CHRISTUM_AD_PLACEHOLDER,
                    start,
                    end,
                    timePlaceholder,
                    timeSet
            );
        }
    }

    private void pushAdBc(
            String eraStart,
            String eraEnd,
            Integer timeStart,
            Integer timeEnd,
            String timePlaceholder,
            TreeSet<String> timeSet
    ) {
        // sec. II p.Chr - sec. VI-lea a.Chr
        if (eraStart.equals(TimeUtils.CHRISTUM_AD_PLACEHOLDER)
                && eraEnd.equals(TimeUtils.CHRISTUM_BC_PLACEHOLDER)) {
            int start = timeEnd;
            int end = 1;
            pushSameBc(
                    TimeUtils.CHRISTUM_BC_PLACEHOLDER,
                    TimeUtils.CHRISTUM_BC_PLACEHOLDER,
                    start,
                    end,
                    timePlaceholder,
                    timeSet
            );

            start = 1;
            end = timeStart;
            pushSameAd(
                    TimeUtils.CHRISTUM_AD_PLACEHOLDER,
                    TimeUtils.CHRISTUM_AD_PLACEHOLDER,
                    start,
                    end,
                    timePlaceholder,
                    timeSet
            );
        }
    }
}
