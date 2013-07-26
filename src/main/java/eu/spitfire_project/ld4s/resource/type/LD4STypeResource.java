package eu.spitfire_project.ld4s.resource.type;

import org.restlet.data.Status;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.vocabulary.SptVocab;
import eu.spitfire_project.ld4s.vocabulary.SsnVocab;

/**
 * Construct a client-defined Resource Type as an RDF Class.
 *
 * @author Myriam Leggieri.
 *
 */
public class LD4STypeResource extends LD4SDataResource {
	/** Service resource name. */
	protected String resourceName = "Type";

	/** RDF Data Model of this Service resource semantic annotation. */
	protected Model rdfData = null;

	/** Resource provided by this Service resource. */
	protected Type ov = null;


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
	protected Resource makeOVLinkedData() throws Exception {
		Resource resource = makeOVData();
		return resource;
	}

	
	/**
	 * Creates the main resource
	 * @param model
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	@Override
	protected  Resource createOVResource() throws Exception {
		Resource resource = null;
		String subjuri = null;
		if (resourceId != null){
			subjuri = SptVocab.getURI()+resourceId;	
		}else{
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The resource type ID has to specified (appended at the end of the requested URI");
			return null;
		}
		resource = rdfData.createResource(subjuri);
		resource.addProperty(RDF.type, RDFS.Class);

		String item = ov.getMeasurementCapability();
		if (item != null && item.trim().compareTo("")!=0){
			if (item.startsWith("http://")){
				resource.addProperty(SsnVocab.IN_CONDITION, 
						rdfData.createResource(item));	
			}
		}
		
		return resource;
	}




}
