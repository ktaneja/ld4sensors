package eu.spitfire_project.ld4s.dataset;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import com.hp.hpl.jena.rdf.model.Model;

import eu.spitfire_project.ld4s.resource.LD4SDataResource;

/**
 * Resource representing the description of the published Hackystat dataset.
 *
 * @author Myriam Leggieri.
 *
 */
public class VocabVoIDResource extends LD4SDataResource {

	private static final String resourceName = "VoID Dataset Description";

	/** RDF Data Model of this Service resource semantic annotation. */
	protected Model rdfData = null;
	
  @Get
  public Representation get() {
			Representation ret = null;
		logger.fine(resourceName + " as Linked Data: Starting");

		try {
			//check cache
			//get all the resource information from the Triple DB
			logger.fine(resourceName + " LD4S: Requesting data");
			logRequest(resourceName, resourceId);
			ret = serializeAccordingToReqMediaType(voIDModel);
		}
		catch (Exception e) {
			setStatusError("Error creating " + resourceName + "  LD4S.", e);
			ret = null;
		}

		return ret;
  }

}
