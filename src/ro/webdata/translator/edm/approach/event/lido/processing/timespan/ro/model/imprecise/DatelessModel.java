package ro.webdata.translator.edm.approach.event.lido.processing.timespan.ro.model.imprecise;

import ro.webdata.translator.edm.approach.event.lido.common.CollectionUtils;

import java.util.TreeSet;

public class DatelessModel {
    private String value;

    private DatelessModel() {}

    public DatelessModel(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        // The ThreeSet is empty because the entry doesn't have any date
        TreeSet<String> centurySet = new TreeSet<>();
        return CollectionUtils.treeSetToDbpediaString(centurySet);
    }
}