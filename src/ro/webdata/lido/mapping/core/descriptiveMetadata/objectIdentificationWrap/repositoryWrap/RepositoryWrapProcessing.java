package ro.webdata.lido.mapping.core.descriptiveMetadata.objectIdentificationWrap.repositoryWrap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.lido.mapping.mapping.leaf.LegalBodyRefComplexTypeProcessing;
import ro.webdata.lido.mapping.mapping.leaf.WorkIDProcessing;
import ro.webdata.lido.mapping.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.repositoryName.RepositoryName;
import ro.webdata.lido.parser.core.leaf.workID.WorkID;
import ro.webdata.lido.parser.core.set.repositorySet.RepositorySet;
import ro.webdata.lido.parser.core.wrap.repositoryWrap.RepositoryWrap;

import java.util.ArrayList;

public class RepositoryWrapProcessing {
    private static LegalBodyRefComplexTypeProcessing legalBodyRefComplexTypeProcessing = new LegalBodyRefComplexTypeProcessing();
    private static WorkIDProcessing workIDProcessing = new WorkIDProcessing();

    /**
     * Add the repository description (the current location of the CHO)
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param repositoryWrap The wrapper for repository properties
     */
    public void addRepositoryWrap(Model model, Resource providedCHO, RepositoryWrap repositoryWrap) {
        if (repositoryWrap != null) {
            ArrayList<RepositorySet> repositorySetList = repositoryWrap.getRepositorySet();
            addRepositorySet(model, providedCHO, repositorySetList);
        }
    }

    /**
     * Add the repository description for all the <b>RepositorySet</b> objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param repositorySetList The list with <b>RepositorySet</b> objects
     */
    private void addRepositorySet(Model model, Resource providedCHO, ArrayList<RepositorySet> repositorySetList) {
        for (int i = 0; i < repositorySetList.size(); i++) {
            RepositorySet repositorySet = repositorySetList.get(i);
            addRepositoryName(model, providedCHO, repositorySet.getRepositoryName());
            addRepository(model, providedCHO, repositorySet.getWorkID());
        }
    }

    /**
     * Add properties regarding the repository name
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param repositoryName The <b>RepositoryName</b> object
     */
    private void addRepositoryName(Model model, Resource providedCHO, RepositoryName repositoryName) {
        Resource organization = legalBodyRefComplexTypeProcessing.createLegalBodyRef(model, repositoryName);

        // Add the CHO current location
        if (organization != null)
            providedCHO.addProperty(EDM.currentLocation, organization);
    }

    /**
     * Add properties regarding the WorkID objects
     * @param model The RDF graph
     * @param providedCHO The CHO
     * @param workIDList The list with <b>WorkID</b> objects
     */
    private void addRepository(Model model, Resource providedCHO, ArrayList<WorkID> workIDList) {
        workIDProcessing.addWorkIDList(model, providedCHO, workIDList);
    }
}
