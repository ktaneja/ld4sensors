package eu.spitfire_project.ld4s.resource.type;

import java.io.Serializable;

import org.json.JSONObject;
import org.restlet.data.Form;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.vocabulary.RDFS;

import eu.spitfire_project.ld4s.lod_cloud.Context;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.resource.LD4SObject;
import eu.spitfire_project.ld4s.vocabulary.LD4SConstants;

/**
 * Type resource.
 * This resource enables the generation of RDF descriptions for an RDF class
 * rather than for an RDF instance, unlike the other LD4S resources.
 * 

 * @author Myriam Leggieri <iammyr@email.com>
 *
 */
public class Type extends LD4SObject  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845385924519981423L;
	
	/** Measurement Capability URI. */
	private String meas_capab_uri = null;


	public Type(String meas_capab,
			String base_datetime, String start_range, String end_range, 
			String[] locations) 
	throws Exception{
		super(base_datetime, start_range, end_range,locations);
		this.setMeasurementCapability(meas_capab);
	}

	public Type(JSONObject json, String localhost) throws Exception {
		super(json);
		if (json.has("measurement"+LD4SConstants.JSON_SEPARATOR+"capability")){
			this.setMeasurementCapability(LD4SDataResource.removeBrackets(
					json.getString("observed"+LD4SConstants.JSON_SEPARATOR+"property")));
		}
	}

	

	private void setMeasurementCapability(String meas_capab) {
		this.meas_capab_uri = meas_capab;
		
	}

	public Type (Form form, String localhost) throws Exception {
		super(form);
		this.setMeasurementCapability(form.getFirstValue("measurement"+LD4SConstants.JSON_SEPARATOR+"capability"));
	}

	public String getMeasurementCapability() {
		return meas_capab_uri;
	}
	
	@Override
	public String getRemote_uri() {
		return remote_uri;
	}


	@Override
	public void setRemote_uri(String host) {
		this.remote_uri = host;
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
	protected void initDefaultType() {
		this.defaultType = (OntClass) RDFS.Class ;
	}

	
		
}
