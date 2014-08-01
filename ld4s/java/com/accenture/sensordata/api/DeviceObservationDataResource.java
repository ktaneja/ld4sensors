package com.accenture.sensordata.api;

import javax.media.j3d.Sensor;

import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

import com.accenture.techlabs.sensordata.dao.SensorDAOFactory;
import com.accenture.techlabs.sensordata.dao.SensorDataDAO;
import com.google.gson.Gson;

import eu.spitfire_project.ld4s.resource.LD4SApiInterface;

public class DeviceObservationDataResource implements LD4SApiInterface {

	@Override
	public Representation get() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public Representation post(JSONObject obj) {
		Gson gson = new Gson();
		DeviceObservationData obsdata  = gson.fromJson(obj.toString(), DeviceObservationData.class);
		SensorDAOFactory daofactory = new SensorDAOFactory();
		SensorDataDAO dao = SensorDAOFactory.getSensorDAO(SensorDAOFactory.SENSOR_TIMESERIES);
		//dao.addData(obj.get("sensorUUID"), SensorDataDAO);
		return new JsonRepresentation("Success");
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
