package eu.spitfire_project.ld4s.resource.ov;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.spitfire_project.ld4s.lod_cloud.Context.Domain;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.vocabulary.SptVocab;

/**
 * Construct an oobservation value resource.
 *
 * @author Myriam Leggieri.
 *
 */
public class LD4SOVResource extends LD4SDataResource {
	/** Service resource name. */
	protected String resourceName = "Observation Value";
	
	/** RDF Data Model of this Service resource semantic annotation. */
	protected OntModel rdfData = null;
	
	/** Resource provided by this Service resource. */
	protected OV ov = null;


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
		
		
		String[] vals = ov.getValues();
		if (vals != null){
			for (int i=0; i<vals.length ;i++){
				if (vals[i] != null){
					resource.addProperty(SptVocab.VALUE, 
							rdfData.createTypedLiteral(vals[i], XSDDatatype.XSDdouble));
				}
			}			
		}
		resource = crossResourcesAnnotation(ov, resource, rdfData);
		return new Object[]{resource, rdfData};
	}

	



		  

	
}
