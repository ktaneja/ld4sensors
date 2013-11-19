package eu.spitfire_project.ld4s.resource.actuator_decision;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import eu.spitfire_project.ld4s.network.lod_cloud.Context;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.resource.LD4SObject;
import eu.spitfire_project.ld4s.vocabulary.LD4SConstants;

public class ActuatorDecision extends LD4SObject  implements Serializable{

	/** Property that is the focus of an Actuator. **/
	private String actuatorProperty = null;

	/** Application Domain of an Actuator. **/
	private String applicationDomain = null;

	/** Optional actions that the actuator can apply on the property of focus. **/
	private String decisionOption = null;


	/**
	 * 
	 */
	private static final long serialVersionUID = 5222281425211809256L;

	public ActuatorDecision(JSONObject json, String localhost) throws JSONException {
		super(json);
		if (json.has("actuator"+LD4SConstants.JSON_SEPARATOR+"property")){
			this.setActuatorProperty(LD4SDataResource.removeBrackets(
					json.getString("actuator"+LD4SConstants.JSON_SEPARATOR+"property")));
		}
		if (json.has("application"+LD4SConstants.JSON_SEPARATOR+"domain")){
			this.setApplicationDomain(LD4SDataResource.removeBrackets(
					json.getString("application"+LD4SConstants.JSON_SEPARATOR+"domain")));
		}
		if (json.has("decision"+LD4SConstants.JSON_SEPARATOR+"options")){
			this.setActuatorProperty(LD4SDataResource.removeBrackets(
					json.getString("decision"+LD4SConstants.JSON_SEPARATOR+"options")));
		}
	}

	@Override
	protected void initDefaultType() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initAcceptedTypes() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getRemote_uri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRemote_uri(String resourceHost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStoredRemotely(boolean storedRemotely) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStoredRemotely() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStoredRemotely(String localUri) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLink_criteria(Context link_criteria) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getLink_criteria() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLink_criteria(String link_criteria, String localhost)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public String getActuatorProperty() {
		return this.actuatorProperty;
	}

	public String getApplicationDomain() {
		return applicationDomain;
	}

	public void setApplicationDomain(String applicationDomain) {
		this.applicationDomain = applicationDomain;
	}

	public String getDecisionOption() {
		return decisionOption;
	}

	public void setDecisionOption(String decisionOption) {
		this.decisionOption = decisionOption;
	}

	public void setActuatorProperty(String actuatorProperty) {
		this.actuatorProperty = actuatorProperty;
	}



}
