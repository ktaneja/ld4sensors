package eu.spitfire_project.ld4s.resource.actuator_decision;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.hp.hpl.jena.rdf.model.Model;
import com.realcraft.coapsensors.SensorAddress;

import eu.spitfire_project.ld4s.dataset.TDBManager;
import eu.spitfire_project.ld4s.network.NetworkAddress;
import eu.spitfire_project.ld4s.network.sensor_network.SensorNetworkManager;
import eu.spitfire_project.ld4s.reasoner.ReasonerManager;
import eu.spitfire_project.ld4s.reasoner.NetworkDeviceFilter;
import eu.spitfire_project.ld4s.resource.LD4SApiInterface;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;

public class ActuatorDecisionResource extends LD4SDataResource implements LD4SApiInterface{
	/** Service resource name. */
	protected String resourceName = "Actuator Decision";

	/** RDF Data Model of this Service resource semantic annotation. */
	protected Model rdfData = null;

	/** Resource provided by this Service resource. */
	protected ActuatorDecision ov = null;

	
	@Override
	@Get
	public Representation get() {
		if (resourceId == null || resourceId.trim().compareTo("") == 0){
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Please request only a resource stored in this LD4S TDB");
			return null;
		}

		Representation ret = null;
		logger.fine(resourceName + " as Linked Data: Starting");

		try {
			//check cache
			//get all the resource information from the Triple DB
			logger.fine(resourceName + " LD4S: Requesting data");
			logRequest(resourceName, resourceId);
			
			rdfData = retrieve(this.uristr, this.namedModel);
			
			ret = serializeAccordingToReqMediaType(rdfData);
		}
		catch (Exception e) {
			setStatusError("Error creating " + resourceName + "  LD4S.", e);
			ret = null;
		}
		
		logger.info("REQUEST "+ this.uristr +" PROCESSING END - "+LD4SDataResource.getCurrentTime()); return ret;
	}
	
	@Override
	@Put
	public Representation put(Form obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Put
	public Representation put(JSONObject obj) {
		return null;
	}

	@Override
	@Post
	public Representation post(Form obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Post
	public Representation post(JSONObject obj) {
		Representation ret = null;
		
		logger.fine(resourceName + " LD4S: Now updating.");
		try {
			this.ov = new ActuatorDecision(obj, this.ld4sServer.getHostName());
			
			//1. scan the network (for now, use an hard-coded list of addresses --> NetworkManager)
			//for network devices' rdf descriptions
			SensorNetworkManager netman = new SensorNetworkManager(this.ld4sServer.getHostName());
			Model descriptions = netman.sourceDiscovery();
			
			//2. store the descriptions locally applying the inference
			if (!TDBManager.store(descriptions, ReasonerManager.createReasoner(getRuleFilePath()), 
					netman.getNamedGraphUri(), getDatasetFolderPath())){
				logger.severe("Unable to store the RDF descriptions " +
						"collected from the network in the local TDB");
			}
				
			//3. filter out the network devices whose triples do not match certain criteria ( --> SettingMapper)
			Model filteredSensors = NetworkDeviceFilter.applyFilter(this.ov, 
					netman.getNamedGraphUri(), getDatasetFolderPath());
			
			//4. consult the rule set to decide what to do with the eventual filtered sensors latest readings
			
			
			
			//I might later decide to store an RDF representation of the generated decision-making support:
			//rdfData = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
			//rdfData = makeOVLinkedData().getModel();

			// create a new resource in the database only if the preferred resource hosting server is
			// the LD4S one
			if (resourceId != null || !this.ov.isStoredRemotely(ld4sServer.getHostName())){
				if (update(rdfData, this.namedModel)){
					setStatus(Status.SUCCESS_OK);	 
					ret = serializeAccordingToReqMediaType(rdfData);
				}else{
					setStatus(Status.SERVER_ERROR_INTERNAL, "Unable to update in the Trple DB");
				}
			}else{
				setStatus(Status.SUCCESS_OK);	 
				ret = serializeAccordingToReqMediaType(rdfData);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, "Unable to instantiate the requested resource\n"
					+e.getMessage());
			return null;
		}


		logger.info("REQUEST "+ this.uristr +" PROCESSING END - "+LD4SDataResource.getCurrentTime()); return ret;
	}

	@Override
	@Delete
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
