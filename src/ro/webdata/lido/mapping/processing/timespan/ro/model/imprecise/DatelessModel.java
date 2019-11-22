package ro.webdata.lido.mapping.processing.timespan.ro.model.imprecise;

import ro.webdata.lido.mapping.common.constants.NSConstants;

public class DatelessModel {
    private String value;

    private DatelessModel() {}

    public DatelessModel(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return NSConstants.NS_REPO_RESOURCE_TIMESPAN_UNKNOWN;
    }
}
