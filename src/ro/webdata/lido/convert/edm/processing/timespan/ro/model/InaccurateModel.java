package ro.webdata.lido.convert.edm.processing.timespan.ro.model;

public class InaccurateModel {
    public static final String AFTER = "post";
    public static final String BEFORE = "ante";
    public static final String APPROXIMATE = "approximate";
    public static final String UNDATED = "undated";

    private String flag = null;
    private String value = "";

    private InaccurateModel() {}

    //TODO: check for date, century, millennium or ages
    public InaccurateModel(String value, String flag) {
        setValue(value);
        setFlag(flag);
    }

    @Override
    public String toString() {
        return value;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
