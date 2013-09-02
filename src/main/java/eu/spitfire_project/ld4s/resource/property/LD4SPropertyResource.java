package eu.spitfire_project.ld4s.resource.property;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.spitfire_project.ld4s.lod_cloud.Context.Domain;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;

/**
 * Construct an observed property resource.
 *
 * @author Myriam Leggieri.
 *
 */
public class LD4SPropertyResource extends LD4SDataResource {
	/** Service resource name. */
	protected String resourceName = "Observed Property";

	/** RDF Data Model of this Service resource semantic annotation. */
	protected OntModel rdfData = null;

	/** Resource provided by this Service resource. */
	protected Property ov = null;


	/**
	 * Creates main resources and additional related information
	 * including linked data
	 *
	 * @param m_returned model which the resources to be created should be attached to
	 * @param obj object containing the information to be semantically annotate
	 * @param id resource identification
	 * @return model 
	 * @throws Exception
	 */
	protected Object[] makeOVLinkedData() throws Exception {
		Object[] resp = makeOVData();
		//set the linking criteria
		this.context = ov.getLink_criteria();
		resp = addLinkedData((Resource)resp[0], Domain.ALL, this.context, (OntModel)resp[1]);
		return resp;
	}

	
	/**
	 * Creates the main resource
	 * @param model
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	@Override
	protected  Object[] createOVResource() throws Exception {
		Resource resource = null;
		String subjuri = null;
		if (resourceId != null){
			subjuri = this.uristr;	
		}else{
			subjuri = ov.getRemote_uri();
		}
		resource = rdfData.createResource(subjuri);
		
		resource = crossResourcesAnnotation(ov, resource,rdfData);
		return new Object[]{resource, rdfData};
	}




}
