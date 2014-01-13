package eu.spitfire_project.ld4s.resource.measurement_capab;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.spitfire_project.ld4s.lod_cloud.Context.Domain;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.vocabulary.SsnVocab;

/**
 * Construct an oobservation value resource.
 *
 * @author Myriam Leggieri.
 *
 */
public class LD4SMCResource extends LD4SDataResource {
	/** Service resource name. */
	protected String resourceName = "Measurement Capability";
	
	/** RDF Data Model of this Service resource semantic annotation. */
	protected OntModel rdfData = null;
	
	/** Resource provided by this Service resource. */
	protected MC ov = null;


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
		
		String item = ov.getObserved_property();
		if (item != null && item.trim().compareTo("")!=0){
			if (item.startsWith("http://")){
				resource.addProperty(SsnVocab.FOR_PROPERTY, 
						rdfData.createResource(item));	
			}else{
				resource = addObsProp(resource, item, SsnVocab.FOR_PROPERTY, ov.getFoi(), rdfData);
			}
		}	
		String[] props = ov.getMeasurement_prop_uris();
		if (props != null){
			for (int ind=0; ind<props.length ;ind++){
				if (props[ind] != null){
					resource.addProperty(SsnVocab.HAS_MEASUREMENT_PROPERTY, 
							rdfData.createResource(props[ind]));
				}
			}
		}
		resource = crossResourcesAnnotation(ov, resource, rdfData);
		return new Object[]{resource, rdfData};
	}
	
}
