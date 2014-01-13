package eu.spitfire_project.ld4s.resource.sparql;

import org.restlet.data.Status;
import org.restlet.representation.Representation;

import eu.spitfire_project.ld4s.resource.LD4SDataResource;

/**
 * Resource representing a SPARQL query
 * 
 * @author Myriam Leggieri <iammyr@email.com>
 *
 */
public class SparqlResource extends LD4SDataResource{


	private String resourceName = "Sparql Request";
	

	
	/**
	 * Returns a serialized RDF Model 
	 * that contains the linked data associated with the
	 * specified path
	 *
	 * @return The resource representation.
	 */
	@Override
	public Representation get() {
		Representation ret = null;
		logger.fine(resourceName + " execution: Starting");
		try {
			logger.fine(resourceName + " : Requesting answer");
			logRequest(resourceName);
			if (this.query == null){
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return null;
			}
			ret = sparqlQueryExec(this.query);
			if (ret != null){
					setStatus(Status.SUCCESS_OK);
			}
			
		}
		catch (Exception e) {
			setStatusError("Error answering the " + resourceName + " - LD4S.", e);
			ret = null;
		}

		return ret;
	}
	
}
