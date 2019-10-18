package ro.webdata.lido.convert.edm.processing.timespan.ro.model;

import ro.webdata.lido.convert.edm.processing.timespan.ro.TimeUtils;

public class TimeModel {
    String eraStart, eraEnd;
    int yearStart, yearEnd;
    boolean isInterval = false;

    //TODO: add this getter for all the date models
    void setEra(String value, String position) {
        if (position.equals(TimeUtils.START)) {
            boolean containsEra = value.contains(TimeUtils.CHRISTUM_BC_NAME)
                    || value.contains(TimeUtils.CHRISTUM_AD_NAME);
            this.eraStart = !containsEra && this.eraEnd != null
                    ? TimeUtils.getEraName(this.eraEnd)
                    : TimeUtils.getEraName(value);
        } else if (position.equals(TimeUtils.END)) {
            this.eraEnd = TimeUtils.getEraName(value);
        }
    }

    void setYear(String value, String position) {
        try {
            int year = Integer.parseInt(value);

            if (position.equals(TimeUtils.START))
                this.yearStart = year;
            else if (position.equals(TimeUtils.END))
                this.yearEnd = year;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    void setIsInterval(boolean isInterval) {
        this.isInterval = isInterval;
    }
}
