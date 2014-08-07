package com.accenture.lsd.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.jena.ontology.OntClass;

import eu.spitfire_project.ld4s.lod_cloud.Context;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.resource.LD4SObject;
import eu.spitfire_project.ld4s.vocabulary.SptSnVocab;
import eu.spitfire_project.ld4s.vocabulary.SsnVocab;

/**
 * Sensing Device resource.
 * This resource is usually stored on the Sensor and transmitted rarely.
 * 
<10e2073a01080063> a spt-sn:WS ;
clf:bn <http://ex.org> ;
clf:bt "12-06-22T17:00Z" ;
spt:uom qudt:unit/Abampere ;
spt:obs <electricCurrentInstance112> ;
spt:out <obval11204id> .

 * @author Kunal Taneja
 *
 */
public class LsdDevice extends LD4SObject  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845385924519981423L;
	

	/** OV IDs (ov base name). */
	private String name = null;
	/** List of sensor this device has */
	private String[] hasSensor = null;
	private String UUID = null;
	private String location;
	private String type;



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public LsdDevice(String host, String[] values, String uom,
			String op, String bn, String bovn, String criteria, String localhost,
			String base_datetime, String start_range, String end_range, 
			String[] locations) 
	throws Exception{
		super(base_datetime, start_range, end_range,locations);
		this.setRemote_uri(host);
	}

	public LsdDevice(JSONObject json, String localhost) throws Exception {
		super(json);
		if (json.has("uri")){
			this.setRemote_uri(LD4SDataResource.removeBrackets(
					json.getString("uri")));
		}
		if (json.has("hasSensor")){
			this.setSensors(json.getJSONArray("hasSensor"));
		}
		if(json.has("type")){
			this.setType(json.getString("type"));
		}
		if(json.has("name")){
			this.setName(json.getString("name"));
		}
		if(json.has("uuid")){
			this.setUUID(json.getString("uuid"));
		}
	}

	

	public LsdDevice(String name, String[] sensors, String type,
			String location) {
		setName(name);
		setLocation(location);
		List<String> sensorsList = new ArrayList<String>();
		for (String sensor : sensors) {
			sensorsList.add(sensor);
		}
		setSensors(sensorsList);
		setType(type);
		setLocation(location);
	}

	@Override
	public String getRemote_uri() {
		return remote_uri;
	}


	@Override
	public void setRemote_uri(String host) {
		this.remote_uri = host;
	}

	

	
	
	public void setSensors(JSONArray jvalues) throws JSONException {
		
		setSensors(jstonArrayToStringArray(jvalues));
		
	}
	
	private List<String> jstonArrayToStringArray(JSONArray jvalues) throws JSONException{
		List<String> vals = new ArrayList<String>();
		for (int i=0; i< jvalues.length(); i++){
			if(jvalues.get(i) instanceof JSONArray){
				vals.addAll(jstonArrayToStringArray((JSONArray) jvalues.get(i)));
			}
			else
				vals.add(jvalues.get(i).toString());
		}
		return vals;
	}

	private void setSensors(List<String> sensorList ) {
		this.hasSensor = new String[sensorList.size()];
		int i=0;
		for (String sensor : sensorList) {
			hasSensor[i++] = sensor;
		}
	}



	@Override
	public void setStoredRemotely(boolean storedRemotely) {
		this.stored_remotely = storedRemotely;		
	}

	@Override
	public boolean isStoredRemotely() {
		return this.stored_remotely;
	}

	@Override
	public boolean isStoredRemotely(String localUri) {
		if (getRemote_uri() == null
				||
				(localUri.contains(getRemote_uri())
						|| getRemote_uri().contains(localUri))){
			return false;
		}
		return true;
	}

	@Override
	public void setLink_criteria(Context link_criteria) {
		this.link_criteria = link_criteria;
	}

	@Override
	public void setLink_criteria(String link_criteria, String localhost) throws Exception {
		this.link_criteria = new Context(link_criteria, localhost);
	}

	@Override
	public Context getLink_criteria() {
		return this.link_criteria;
	}

	
	
	@Override
	protected void initAcceptedTypes() {
		this.setAcceptedTypes(new OntClass[]{
				SptSnVocab.ACTUATOR, SptSnVocab.TRANSDUCER,
				SptSnVocab.ACCELEROMETER,
				SptSnVocab.GPS,
				SptSnVocab.HUMIDITY_SENSOR,
				SptSnVocab.LIGHT_SENSOR,
				SptSnVocab.MOTION_SENSOR,
				SsnVocab.SENSING_DEVICE
		});
	}

	@Override
	protected void initDefaultType() {
		this.defaultType = SsnVocab.DEVICE;
	}

	

	public String[] getSensors() {
		return hasSensor;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
}
