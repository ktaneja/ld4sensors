package com.accenture.sensordata.api;

import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.accenture.techlabs.sensordata.dao.SensorDAOFactory;
import com.accenture.techlabs.sensordata.dao.SensorDataDAO;
import com.accenture.techlabs.sensordata.model.DeviceObservationData;
import com.google.gson.Gson;

import eu.spitfire_project.ld4s.resource.LD4SApiInterface;
import eu.spitfire_project.ld4s.resource.LD4SDataResource;

public class DeviceObservationDataResource extends LD4SDataResource implements LD4SApiInterface {

	@Override
	public Representation get() {
		String from = getQuery().getValues("from");
		String to = getQuery().getValues("to");
		String device = getQuery().getValues("device");
		if(from == null || to == null){
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new JsonRepresentation("Query Parameter 'from' and/or 'to' missing");
		}
		SensorDataDAO dao = SensorDAOFactory.getSensorDAO(SensorDAOFactory.SENSOR_TIMESERIES);
		String data = dao.getData(device, from, to);
		
		return new StringRepresentation(data,MediaType.TEXT_CSV);
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
		DeviceObservationData obsdata  = gson.fromJson(obj.toString(), DeviceObservationData.class);
		SensorDataDAO dao = SensorDAOFactory.getSensorDAO(SensorDAOFactory.SENSOR_TIMESERIES);
		dao.addData(obsdata);
		
		setStatus(Status.SUCCESS_CREATED);
		setLocationRef("/ld4s/postdata");
		return new JsonRepresentation("Success");
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
