package eu.spitfire_project.ld4s.resource.foi;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;

import eu.spitfire_project.ld4s.lod_cloud.Context;
import eu.spitfire_project.ld4s.resource.LD4SObject;

public class FeatureOfInterest extends LD4SObject{


	public FeatureOfInterest(Form form) {
		super(form);
		// TODO Auto-generated constructor stub
	}
	
	public FeatureOfInterest(JSONObject json) throws JSONException {
		super(json);
		// TODO Auto-generated constructor stub
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

	@Override
	protected void initAcceptedTypes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultType() {
		// TODO Auto-generated method stub
		
	}

}
