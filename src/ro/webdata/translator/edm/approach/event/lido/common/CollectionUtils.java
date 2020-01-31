package ro.webdata.translator.edm.approach.event.lido.common;

import ro.webdata.translator.edm.approach.event.lido.common.constants.FileConstants;
import ro.webdata.translator.edm.approach.event.lido.common.constants.NSConstants;

import java.util.TreeSet;

public class CollectionUtils {
    public static final String STRING_LIST_SEPARATOR = " ### ";

    public static String treeSetToDbpediaString(TreeSet<String> treeSet) {
        StringBuilder sb = new StringBuilder();
        Object[] list = treeSet.toArray();

        for (int i = 0; i < list.length; i++) {
            String item = list[i].toString();

            if (!item.contains(NSConstants.NS_REPO_RESOURCE)) {
                sb.append(NSConstants.NS_DBPEDIA_PAGE);
                sb.append(FileConstants.FILE_SEPARATOR);
                sb.append(item);

                //TODO: check for possible bugs
                /*
                // OLD:
                sb.append("http://dbpedia.org/page/");
                sb.append(item);

                // NEW:
                sb.append("http://dbpedia.org/page");
                sb.append("/");
                sb.append(item);
                 */
            } else {
                sb.append(item);
            }

            if (i < list.length - 1)
                sb.append(STRING_LIST_SEPARATOR);
        }

        return sb.toString();
    }
}
