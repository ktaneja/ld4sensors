package com.accenture.sensordata.api;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.accenture.lsd.device.LsdDevice;
import com.accenture.lsd.device.LsdDeviceResource;
import com.accenture.techlabs.sensordata.model.DeviceDataType;
import com.accenture.techlabs.sensordata.model.DeviceObservationData;
import com.accenture.techlabs.sensordata.model.DeviceDataType.Type;
import com.accenture.techlabs.sensordata.model.DeviceObservationData.SensorObservationData;
import com.google.gson.Gson;

import eu.spitfire_project.ld4s.vocabulary.EQIQVocab;

public class TestPostData {
	@Test
	public void testPostVMData() throws IOException, JSONException{
		//System.out.println(Runtime.getRuntime().exec("hostname"));
		ClientResource cr;
		Representation resp;
		Status status;
		//JSONObject json = createJson();
		String uuid= "9c086605-c9de-4d8e-8701-0b9a55bdb34c";
		JSONObject json = VSphereDataAgent.getDataFromLastHour(uuid);
		
		String local_uri = "http://localhost:8080/ld4s/postdata/VM_Qian-Demo_gui";//localhost:8080/ld4s/postdata/123
		
				 
		 cr = new ClientResource(local_uri);
		
		resp = cr.post(json);
		status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		String rdf = resp.getText();
		System.out.println("\n\n\n==============\nTesting DEVICE JSON POST " +
				"(annotation to be soterd remotely)\n"
				+ "sent : "+json
				+local_uri+"==============\n"+rdf);
		
		
	}

	private JSONObject createJson() throws JSONException {
		
		Gson gson = new Gson();
		DateTime time = DateTime.now();
		
		DeviceObservationData data = new DeviceObservationData("vm_qiandemo_gui", "aa12f414-c303-4e7f-990e-060a9edfa8f1");
		SensorObservationData sData = data.new SensorObservationData("vcenter_1");
		
		/*DeviceDataType type = new DeviceDataType();
		type .addMember("vcenter_1", "cpureading", Type.DOUBLE);
		type.addMember("vcenter_1", "diskreading", Type.INT);
		type.addMember("vcenter_1", "memoryreading", Type.DOUBLE);
		data.setDataType(type);*/
		List<Object> obs = new ArrayList<Object>();
		obs.add(50);
		obs.add(0);
		obs.add(9);
		sData.addSensorEntry(time, obs);
		
		time = DateTime.now();
		sData.addSensorEntry(time, obs);
		
		data.addObservationValue("vcenter_1", sData);
		String json = gson.toJson(data);
		System.out.println(json);
		return new JSONObject(json);
	}
}
