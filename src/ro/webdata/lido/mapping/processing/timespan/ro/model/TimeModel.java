package ro.webdata.lido.mapping.processing.timespan.ro.model;

import ro.webdata.lido.mapping.common.PrintMessages;
import ro.webdata.lido.mapping.common.constants.Constants;
import ro.webdata.lido.mapping.processing.timespan.ro.TimeUtils;

public class TimeModel {
    protected String eraStart, eraEnd;
    protected int yearStart, yearEnd;
    protected String monthStart, monthEnd;
    protected int dayStart, dayEnd;
    protected boolean isInterval = false;

    //TODO: add this getter for all the date models
    protected void setEra(String value, String position) {
        if (position.equals(TimeUtils.START_PLACEHOLDER)) {
            boolean containsEra = value.contains(TimeUtils.CHRISTUM_BC_PLACEHOLDER)
                    || value.contains(TimeUtils.CHRISTUM_AD_PLACEHOLDER);
            this.eraStart = !containsEra && this.eraEnd != null
                    ? TimeUtils.getEraName(this.eraEnd)
                    : TimeUtils.getEraName(value);
        } else if (position.equals(TimeUtils.END_PLACEHOLDER)) {
            this.eraEnd = TimeUtils.getEraName(value);
        }
    }

    protected void setYear(String yearStr, String position) {
        try {
            int year = Integer.parseInt(yearStr.trim());

            if (position.equals(TimeUtils.START_PLACEHOLDER))
                this.yearStart = year;
            else if (position.equals(TimeUtils.END_PLACEHOLDER))
                this.yearEnd = year;

            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimeUtils.CHRISTUM_AD_PLACEHOLDER))
                PrintMessages.printTooBigYear(year);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    protected void setMonth(String month, String position) {
        if (position.equals(TimeUtils.START_PLACEHOLDER))
            this.monthStart = month;
        else if (position.equals(TimeUtils.END_PLACEHOLDER))
            this.monthEnd = month;
    }

    protected void setDay(String dayStr, String position) {
        try {
            int day = Integer.parseInt(dayStr);

            if (position.equals(TimeUtils.START_PLACEHOLDER))
                this.dayStart = day;
            else if (position.equals(TimeUtils.END_PLACEHOLDER))
                this.dayEnd = day;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    protected void setIsInterval(boolean isInterval) {
        this.isInterval = isInterval;
    }
}
