package ro.webdata.lido.mapping.processing.timespan.ro.model;

import ro.webdata.lido.mapping.common.PrintMessages;
import ro.webdata.lido.mapping.common.constants.Constants;
import ro.webdata.lido.mapping.processing.timespan.ro.TimeUtils;

public class TimeModel {
    protected String eraStart, eraEnd;
    protected Integer millenniumStart, millenniumEnd;
    protected Integer centuryStart, centuryEnd;
    protected Integer yearStart, yearEnd;
    protected String monthStart, monthEnd;
    protected int dayStart, dayEnd;
    protected boolean isInterval = false;

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

    protected void setMillennium(Integer millennium, String position) {
        if (position.equals(TimeUtils.START_PLACEHOLDER))
            this.millenniumStart = millennium;
        else if (position.equals(TimeUtils.END_PLACEHOLDER))
            this.millenniumEnd = millennium;
    }

    protected void setCentury(String yearStr, String position) {
        try {
            int year = Integer.parseInt(yearStr.trim());
            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimeUtils.CHRISTUM_AD_PLACEHOLDER)) {
                PrintMessages.printTooBigYear("setting century", position, year);
            } else {
                int century = (int) (Math.floor((year / 100)) + 1);
                setCentury(century, position);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    protected void setCentury(Integer century, String position) {
        if (position.equals(TimeUtils.START_PLACEHOLDER))
            this.centuryStart = century;
        else if (position.equals(TimeUtils.END_PLACEHOLDER))
            this.centuryEnd = century;
    }

    protected void setYear(String yearStr, String position) {
        try {
            int year = Integer.parseInt(yearStr.trim());

            if (year > Constants.LAST_UPDATE_YEAR && eraStart.equals(TimeUtils.CHRISTUM_AD_PLACEHOLDER)) {
                PrintMessages.printTooBigYear("setting year", position, year);
            } else {
                if (position.equals(TimeUtils.START_PLACEHOLDER))
                    this.yearStart = year;
                else if (position.equals(TimeUtils.END_PLACEHOLDER))
                    this.yearEnd = year;
            }
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
