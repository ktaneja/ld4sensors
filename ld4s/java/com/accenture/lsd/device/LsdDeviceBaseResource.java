package com.accenture.lsd.device;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.accenture.techlabs.sensordata.dao.CassandraSensorDataDAO;
import com.accenture.techlabs.sensordata.dao.SensorDAOFactory;
import com.accenture.techlabs.sensordata.model.DeviceDataType;
import com.accenture.techlabs.sensordata.model.DeviceDataType.Type;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.resource.LD4SDataResource.SparqlType;
import eu.spitfire_project.ld4s.vocabulary.EQIQVocab;

/**
 * Construct a device resource.
 *
 * @author Kunal Taneja.
 *
 */
public class LsdDeviceBaseResource extends LD4SDataResource {
	/** Service resource name. */
	protected String resourceName = "Device";

	/** RDF Data Model of this Service resource semantic annotation. */
	protected Model rdfData = null;

	/** Resource provided by this Service resource. */
	protected LsdDevice device = null;


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
	protected Resource createDeviceResource() throws Exception {
		Resource resource = createDeviceResourceData();
		//resource = addLinkedData(resource, Domain.ALL, this.context);
		return resource;
	}



	/**
	 * Creates the main resource
	 * @param model
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	protected  Resource createDeviceResourceData() throws Exception {
		Resource resource = null;
		String subjuri = null;
		if (resourceId != null){
			//subjuri = this.uristr;	
			subjuri = EQIQVocab.getURI() + resourceId;
		}else{
			subjuri = EQIQVocab.getURI() + resourceId;
		}
		resource = rdfData.createResource(subjuri);

		
		
		
		String item = device.getUUID();
		if (item != null && item.trim().compareTo("")!=0){
			
			if (item.startsWith("http://")){
				resource.addProperty(EQIQVocab.deviceID, 
						rdfData.createResource(item));	
			}else{
				resource.addProperty(EQIQVocab.deviceID, item);
			}	
			
		}
		
		item = device.getName();
		if (item != null && item.trim().compareTo("")!=0){
			//Put name
			if (item.startsWith("http://")){
				resource.addProperty(EQIQVocab.deviceName, 
						rdfData.createResource(item));	
			}else{
				resource.addProperty(EQIQVocab.deviceName, item);
			}	
			
		}
		
		item = device.getType();
		if (item != null && item.trim().compareTo("")!=0){
			//Put name
			if (item.startsWith("http://")){
				resource.addProperty(EQIQVocab.deviceType, 
						rdfData.createResource(item));	
			}else{
				resource.addProperty(EQIQVocab.deviceType, item);
			}	
			
		}
		
		String[] sensors = device.getSensors();
		if (sensors != null){
			for (int i=0; i<sensors.length ;i++){
				if (sensors[i] != null){
					if (sensors[i].startsWith("http://")){
						resource.addProperty(EQIQVocab.hasSensors, 
								rdfData.createResource(sensors[i]));	
					}else{
						resource.addProperty(EQIQVocab.hasSensors, sensors[i]);
					}	
				}
			}			
		}
		
		resource.addProperty(EQIQVocab.data_access_url, Integer.toString(SensorDAOFactory.SENSOR_TIMESERIES));
		
		
		String loc = device.getLocation();
		if (loc != null && loc.trim().compareTo("")!=0){
			//Create Location Resource and set
			String[] coords = loc.split("_");
			OntClass locationClass = EQIQVocab.Location;
			Individual ind = locationClass.createIndividual();
			Resource locationInstance = ind.asResource();
			if(coords.length >= 2 ){
				locationInstance.addProperty(EQIQVocab.latitude, coords[0]);
				locationInstance.addProperty(EQIQVocab.longitude, coords[1]);
			}
			if(coords.length == 3){
				locationInstance.addProperty(EQIQVocab.altitude, coords[2]);
			}
			resource.addProperty(EQIQVocab.hasLocation, locationInstance);
		}
		
		
		resource = crossResourcesAnnotation(device, resource);
		return resource;
	}


	public DeviceDataType getDataTypesOfDevice(String resourceURI) throws UnsupportedEncodingException {	
		String queryString = 
				"SELECT ?sensor ?stype ?uom ?datatype" +
				" WHERE {" + 
				"<"+ resourceURI +">" + " <" + EQIQVocab.hasSensors + "> " + " ?sensor. \n" + 
				"?sensor"+ " <" + EQIQVocab.observesReading + "> " + "?reading. \n" + 
				"?reading <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  ?stype. \n" +
				"?reading" + " <" + EQIQVocab.hasUOM + "> " +  "?uom. \n" +
				"?p   <http://www.w3.org/2000/01/rdf-schema#subPropertyOf> <" + EQIQVocab.unitType + ">. \n" +
				"?p   <http://www.w3.org/2000/01/rdf-schema#domain> ?uom. \n"  +
				"?p   <http://www.w3.org/2000/01/rdf-schema#range> ?datatype. \n" 
				+ "" + 
				"      }";
		queryString = URLDecoder.decode(queryString, "utf-8");


		Object answer = sparqlExec(queryString, SparqlType.SELECT);
		Model m = ((ResultSet)answer).getResourceModel();
		QueryExecution qe = QueryExecutionFactory.create(queryString, m);
		ResultSet results = qe.execSelect();
		
		DeviceDataType dataType = new DeviceDataType();
		while(results.hasNext()){
			QuerySolution qs = results.next();
			String sensor = qs.get("sensor").asResource().getLocalName();
			String name = null;
			if(qs.get("stype") != null)
				name = qs.get("stype").asResource().getLocalName(); 
			Type type = getType(qs.get("datatype").asResource().getLocalName());
			dataType.addMember(sensor, name, type);
		}
		return dataType;
	}
	
	private Type getType(String xsdType) {
		if(xsdType.toLowerCase().endsWith("double"))
			return Type.DOUBLE;
		else if(xsdType.toLowerCase().endsWith("integer"))
			return Type.INT;
		else if(xsdType.toLowerCase().endsWith("string"))
			return Type.TEXT;
		else
			throw new NotImplementedException();
	}


}
