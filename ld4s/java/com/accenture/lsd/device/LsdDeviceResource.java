package com.accenture.lsd.device;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.accenture.techlabs.sensordata.dao.SensorDataDAO;
import com.accenture.techlabs.sensordata.dao.SensorDAOFactory;
import com.accenture.techlabs.sensordata.model.DeviceDataType;
import com.accenture.techlabs.sensordata.model.DeviceDataType.Type;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.spitfire_project.ld4s.lod_cloud.Context.Domain;
import eu.spitfire_project.ld4s.resource.LD4SApiInterface;
import eu.spitfire_project.ld4s.server.Server;
import eu.spitfire_project.ld4s.vocabulary.EQIQVocab;

/**
 * Resource representing an observation value.
 *
 * @author Kunal Taneja.
 *
 */
public class LsdDeviceResource extends LsdDeviceBaseResource implements LD4SApiInterface{


	// GET req:resource stored locally
	/**
	 * Returns a serialized RDF Model 
	 * that contains the linked data associated with the
	 * specified path
	 *
	 * @return The resource representation.
	 */
	@Override
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
			//get the resource uri by cutting off the eventual appended query string
			int query = uristr.indexOf("?");
			if (query != -1){
				uristr = this.uristr.substring(0,query-1);
			}
			rdfData = retrieve(EQIQVocab.NS + resourceId, this.namedModel);
			//how it is: for now, if links are requested, then search for new ones 
			//and filter out all the stored ones.
			if (!this.context.isEmpty()){
				//how it should be: add the already existing links iff their context 
				//matches with the requested one search for new links
				rdfData = addLinkedData(rdfData.getResource(uristr), Domain.ALL, this.context).getModel();
			}
			ret = serializeAccordingToReqMediaType(rdfData);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage(), e);
			setStatusError("Error creating " + resourceName + "  LD4S. " + e.getStackTrace(), e);
			ret = null;
		}

		return ret;
	}




	

	/**
	 * Update a stored Observation Value resource
	 * with the information sent 
	 *
	 *@param obj information to store
	 */
	@Override
	public Representation post(JSONObject obj){
		Representation ret = null;
		String id = getUUID(EQIQVocab.NS + resourceId);
		
		int urlID = getDataAccessURL(EQIQVocab.NS + resourceId);
		int urID = getDataAccessURL(EQIQVocab.NS + "xyz");
		
		if(id != null){
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			logger.log(Level.INFO, "Resource " + resourceId + " exists with ID " + id);
			return new JsonRepresentation("Resource " + resourceId + "exists with ID " + id);
		}
		
		rdfData = ModelFactory.createDefaultModel();
		super.initModel(rdfData,"spitfire.rdf");
		logger.fine(resourceName + " LSD: Now updating.");
		try {
			String uuid = getUniqueIdentifier();
			//obj.append("uuid", uuid);
			obj.putOnce("uuid", uuid);
			
			this.device = new LsdDevice(obj, Server.getHostName());
			if(resourceId == null)
				resourceId = device.getName();
			registerDevice();
			
			
			rdfData = createDeviceResource().getModel();

			
			if (resourceId != null || !this.device.isStoredRemotely(Server.getHostName())){
				if (update(rdfData, this.namedModel)){
					setStatus(Status.SUCCESS_CREATED);
					JSONObject response = new JSONObject();
					response.putOnce("uuid", uuid);
					ret = new JsonRepresentation(response); 
					setLocationRef("/ld4s/lsddevice/" + resourceId);
					
				}else{
					setStatus(Status.SERVER_ERROR_INTERNAL, "Unable to update in the Trple DB");
				}
			}else{	 
				setStatus(Status.SUCCESS_CREATED);
				

			}
		} catch (JSONException e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, "Unable to instantiate the requested resource\n"
					+e.getMessage());
			return null;
		}
		return ret;
	}
	

	

	private void registerDevice() throws UnsupportedEncodingException {
		DeviceDataType dataType = getDataTypesOfDevice(EQIQVocab.NS + resourceId);
		SensorDataDAO sensorDAO = SensorDAOFactory.getSensorDAO(SensorDAOFactory.SENSOR_TIMESERIES);
		sensorDAO.createTable(resourceId, dataType);
	}


	private String getUniqueIdentifier() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	


	


	// DELETE req: resource stored locally
	/**
	 * Delete an already store Observation Value resource 
	 *
	 */
	@Override
	public void remove(){
		if (resourceId == null || resourceId.trim().compareTo("") == 0){
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return;
		}
		logger.fine(resourceName + " LD4S: Now deleting "+this.uristr);		
		// create a new resource in the database
		if (delete(this.uristr, this.namedModel)){
			setStatus(Status.SUCCESS_OK);	 
		}else{
			setStatus(Status.SERVER_ERROR_INTERNAL, "Unable to delete from the Trple DB");
		}
	}






	@Override
	public Representation put(Form obj) {
		return null;
	}






	@Override
	public Representation put(JSONObject obj) {
		Representation ret = null;
		String id = getUUID(EQIQVocab.NS + resourceId);
		if(id == null){
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			logger.log(Level.INFO, "Resource " + resourceId + " does not exist ");
			return new JsonRepresentation("Resource " + resourceId + "does not exist");
		}
		
		rdfData = ModelFactory.createDefaultModel();
		super.initModel(rdfData,"spitfire.rdf");
		logger.fine(resourceName + " LSD: Now updating.");
		try {
			
			this.device = new LsdDevice(obj, Server.getHostName());
			if(resourceId == null)
				resourceId = device.getName();
			rdfData = createDeviceResource().getModel();
			
			if (resourceId != null || !this.device.isStoredRemotely(Server.getHostName())){
				if (update(rdfData, this.namedModel)){
					setStatus(Status.SUCCESS_CREATED);
					JSONObject response = new JSONObject();
					ret = new JsonRepresentation(response); 
					setLocationRef("/ld4s/lsddevice/" + resourceId);
					
				}else{
					setStatus(Status.SERVER_ERROR_INTERNAL, "Unable to update in the Trple DB");
				}
			}else{	 
				setStatus(Status.SUCCESS_CREATED);
				

			}
		} catch (JSONException e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL, "Unable to instantiate the requested resource\n"
					+e.getMessage());
			return null;
		}
		return ret;
	}






	@Override
	public Representation post(Form obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
