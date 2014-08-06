package com.accenture.lsd.device;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;











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

/**
 * Resource representing an observation value.
 *
 * @author Myriam Leggieri.
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
			rdfData = retrieve(this.uristr, this.namedModel);
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
			setStatusError("Error creating " + resourceName + "  LD4S.", e);
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
		
		rdfData = ModelFactory.createDefaultModel();
		super.initModel(rdfData,"spitfire.rdf");
		logger.fine(resourceName + " LD4S: Now updating.");
		try {
			
			//String uuid = registerDevice();
			obj.append("uuid","123");
			this.device = new LsdDevice(obj, Server.getHostName());
			if(resourceId == null)
				resourceId = device.getName();
			rdfData = createDeviceResource().getModel();

			// create a new resource in the database only if the preferred resource hosting server is
			// the LD4S one
			getDataTypesOfDevice();
			if (resourceId != null || !this.device.isStoredRemotely(Server.getHostName())){
				
				
				if (update(rdfData, this.namedModel)){
					setStatus(Status.SUCCESS_OK);
					JSONObject response = new JSONObject();
					response.append("uuid", "123");
					ret = new JsonRepresentation(response); 
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
	

	

	private String registerDevice() {
		DeviceDataType dataType = getDataTypesOfDevice();
		SensorDataDAO sensorDAO = SensorDAOFactory.getSensorDAO(SensorDAOFactory.SENSOR_TIMESERIES);
		
		String uuid = getUniqueIdentifier();
		sensorDAO.createTable(resourceId, dataType);
		
		return uuid;
	}


	private String getUniqueIdentifier() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	private DeviceDataType getDataTypesOfDevice() {
		String resourceURI = "http://" + Server.getHostName() + "device/" + resourceId;
		
		String queryString = 
				"SELECT ?sensor ?name ?type " +
				" WHERE {" + 
				"<" + resourceURI + ">" + " <http://spitfire-project.eu/ontology/ns/has_sensor> ?sensor.\n"
				+ " ?sensor <http://spitfire-project.eu/ontology/ns/uom>  ?uom. \n"
				+ " ?uom <http://spitfire-project.eu/ontology/ns/hasMember>  ?member. \n"
				+ " ?member <http://spitfire-project.eu/ontology/ns/type> ?type. \n"
				+ " OPTIONAL { ?member <http://spitfire-project.eu/ontology/ns/name> ?name. }"
				+ "" + 
				"      } LIMIT 10";
		queryString = 
				"SELECT  ?name ?type " +
				" WHERE {" + 
				"<http://eqiq.techlabs.accenture.com/sensor#VM_Qian-Demo_gui> ?name ?type"
				+ "" + 
				"      } LIMIT 10";
		try {
			queryString = URLDecoder.decode(queryString, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Object answer = sparqlExec(queryString, SparqlType.SELECT);
		Model m = ((ResultSet)answer).getResourceModel();
		QueryExecution qe = QueryExecutionFactory.create(queryString, m);
		ResultSet results = qe.execSelect();
		DeviceDataType dataType = new DeviceDataType();
		while(results.hasNext()){
			QuerySolution qs = results.next();
			String sensor = qs.get("sensor").asResource().getLocalName();
			String name = null;
			if(qs.get("name") != null)
				name = qs.get("name").asResource().getLocalName(); 
			Type type = getType(qs.get("type").asResource().getLocalName());
			dataType.addMember(sensor, name, type);
		}
		return dataType;
	}


	private Type getType(String xsdType) {
		
		return Type.DOUBLE;
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
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public Representation put(JSONObject obj) {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public Representation post(Form obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
