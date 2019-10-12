package ro.webdata.lido.convert.edm.processing.timespan.ro.model;

import java.util.ArrayList;

public class TimespanModel {
    private ArrayList<String> timespanList = new ArrayList<>();
    private String residualValue = "";

    private TimespanModel() {}

    public TimespanModel(String value) {
        setResidualValue(value);
    }

    public TimespanModel(ArrayList<String> list, String value) {
        setTimespanList(list);
        setResidualValue(value);
    }

    //FIXME: make it private
    public void clearTimespanList() {
        this.timespanList = new ArrayList<>();
    }

    public ArrayList<String> getTimespanList() {
        return timespanList;
    }

    public String getResidualValue() {
        return residualValue;
    }

    private void setTimespanList(ArrayList<String> timespanList) {
        this.timespanList = timespanList;
    }

    private void setResidualValue(String residualValue) {
        this.residualValue = residualValue;
    }
}
