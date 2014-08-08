package com.accenture.sensordata.api;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.accenture.lsd.device.LsdDeviceBaseResource;
import com.accenture.techlabs.sensordata.dao.SensorDAOFactory;
import com.accenture.techlabs.sensordata.dao.SensorDataDAO;
import com.accenture.techlabs.sensordata.model.DeviceObservationData;
import com.google.gson.Gson;

import eu.spitfire_project.ld4s.resource.LD4SApiInterface;
import eu.spitfire_project.ld4s.vocabulary.EQIQVocab;

public class DeviceObservationDataResource extends LsdDeviceBaseResource implements LD4SApiInterface {

	@Override
	public Representation get() {
		String from = getQuery().getValues("from");
		String to = getQuery().getValues("to");
		String device = resourceId;

		if(resourceId == null){
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new JsonRepresentation("Provide a Resource");
		}
		try {

			if(from == null || to == null){
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return new JsonRepresentation("Query Parameter 'from' and/or 'to' missing");
			}

			int datastoreID = getDataAccessURL(EQIQVocab.NS + resourceId);
			SensorDataDAO dao = SensorDAOFactory.getSensorDAO(datastoreID);
			String data = dao.getData(device, from, to);

			return new StringRepresentation(data,MediaType.TEXT_CSV);
		}catch(Exception e){
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL);
			return null;
		}
	}

	@Override
	public Representation put(Form obj) {
		setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
		return new JsonRepresentation("Media Type Not Supported");
	}

	@Override
	public Representation put(JSONObject obj) {
		setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
		return new JsonRepresentation("Media Type Not Supported");
	}

	@Override
	public Representation post(Form obj) {
		setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
		return new JsonRepresentation("Media Type Not Supported");
	}

	@Override
	public Representation post(JSONObject obj) {
		Gson gson = new Gson();
		if(resourceId == null){
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new JsonRepresentation("Provide a Resource");
		}
		try {
			if(obj.has("deviceUUID")){
				String uuid = obj.getString("deviceUUID");
				String id = getUUID(EQIQVocab.NS + resourceId);
				if(id == null){
					setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					return new JsonRepresentation("Device Not Registered.");
				}
				if(!id.equals(uuid)){
					setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
					return new JsonRepresentation("Invalid UUID.");
				}
			}
			else{
				setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return new JsonRepresentation("No UUID Provided.");
			}

			DeviceObservationData obsdata  = gson.fromJson(obj.toString(), DeviceObservationData.class);
			obsdata.setDataType(getDataTypesOfDevice(EQIQVocab.NS + resourceId));
			
			int datastoreID = getDataAccessURL(EQIQVocab.NS + resourceId);
			SensorDataDAO dao = SensorDAOFactory.getSensorDAO(datastoreID);
			dao.addData(obsdata);

			setStatus(Status.SUCCESS_CREATED);
			setLocationRef("/ld4s/postdata");
			return new JsonRepresentation("Success");
		} catch (JSONException | UnsupportedEncodingException e) {
			e.printStackTrace();
			setStatus(Status.SERVER_ERROR_INTERNAL);
			return null;
		}


	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
