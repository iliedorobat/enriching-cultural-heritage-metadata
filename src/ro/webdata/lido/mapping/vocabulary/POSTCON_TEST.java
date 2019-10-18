package ro.webdata.lido.mapping.vocabulary;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;

public class POSTCON_TEST extends Object {

    // URI for vocabulary elements
    protected static final String uri = "http://burningbird.net/postcon/elements/1.0/";

    // Return URI for vocabulary elements
    public static String getURI(  )
    {
        return uri;
    }

    // Define the property labels and objects
    static final String   nbio = "bio";
    public static       Property bio = null;
    static final String   nrelevancy = "relevancy";
    public static       Property relevancy = null;
    static final String   npresentation = "presentation";
    public static       Resource presentation = null;
    static final String   nhistory = "history";
    public static       Property history = null;
    static final String   nmovementtype = "movementType";
    public static       Property movementtype = null;
    static final String   nreason = "reason";
    public static       Property reason = null;
    static final String   nstatus = "currentStatus";
    public static       Property status = null;
    static final String   nrelated = "related";
    public static       Property related = null;
    static final String   ntype = "type";
    public static       Property type = null;
    static final String   nrequires = "requires";
    public static       Property requires = null;


    // Instantiate the properties and the resource
    static {
            // Instantiate the properties
            bio          = new PropertyImpl(uri, nbio);
            relevancy    = new PropertyImpl(uri, nrelevancy);
            presentation = new PropertyImpl(uri, npresentation);
            history      = new PropertyImpl(uri, nhistory);
            related      = new PropertyImpl(uri, nrelated);
            type         = new PropertyImpl(uri, ntype);
            requires     = new PropertyImpl(uri, nrequires);
            movementtype = new PropertyImpl(uri, nmovementtype);
            reason       = new PropertyImpl(uri, nreason);
            status       = new PropertyImpl(uri, nstatus);
    }

}
