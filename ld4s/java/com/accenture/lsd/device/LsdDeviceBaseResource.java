package com.accenture.lsd.device;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.spitfire_project.ld4s.resource.LD4SDataResource;
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




}
