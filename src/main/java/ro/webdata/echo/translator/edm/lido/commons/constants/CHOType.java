package ro.webdata.echo.translator.edm.lido.commons.constants;

import java.util.HashSet;
import java.util.Set;

public final class CHOType {
    private static final Set<String> DOMAINS_EN = new HashSet<>() {{
        add("archaeology");
        add("fine arts");
        add("decorative arts");
        add("documents");
        add("ethnography");
        add("science and technology");
        add("history");
        add("medals");
        add("numismatics");
        add("natural sciences");
    }};
    private static final Set<String> DOMAINS_RO = new HashSet<>() {{
        add("arheologie");
        add("artă plastică");
        add("artă decorativă");
        add("documente");
        add("etnografie");
        add("știință și tehnică");
        add("istorie");
        add("medalistică");
        add("numismatică");
        add("științele naturii");
    }};

    /**
     * The list with the types of CHOs from a controlled vocabulary, which
     * is going to be used as values for <b>dc:subject</b> property<br/>
     * E.g. archaeology, documents etc.
     */
    public static final Set<String> DOMAINS = new HashSet<>() {{
        addAll(DOMAINS_EN);
        addAll(DOMAINS_RO);
    }};

    /**
     * Check if CHOType.SUBJECTS contains the input value
     * @param value Input string
     * @return true/false
     */
    public static boolean isDomainClassification(String value) {
        return DOMAINS.contains(value);
    }
}
