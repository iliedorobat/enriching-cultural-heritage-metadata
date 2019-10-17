package ro.webdata.lido.convert.edm.processing.timespan.ro.model.imprecise;

public class DatelessModel {
    private static final String DATELESS = "### DATELESS ###";
    private String value;

    private DatelessModel() {}

    public DatelessModel(String value) {
        setValue(value);
    }

    @Override
    public String toString() {
        return DATELESS;
    }

    private void setValue(String value) {
        this.value = value;
    }
}
