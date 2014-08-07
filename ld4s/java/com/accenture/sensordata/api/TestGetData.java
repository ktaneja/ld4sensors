package com.accenture.sensordata.api;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class TestGetData {
	@Test
	public void testRegisterDevice() throws IOException, JSONException{
		//System.out.println(Runtime.getRuntime().exec("hostname"));
		ClientResource cr;
		Representation resp;
		Status status;
		Date from = new Date();
		Date to = new Date();
		from.setTime(from.getTime() - 86400000);
		
		String local_uri = "http://localhost:8080/ld4s/postdata?to=" + to.getTime() +"&from=" + from.getTime() + "&device=" + "VM_Qian-Demo_gui";//localhost:8080/ld4s/postdata/123
		
				 
		 cr = new ClientResource(local_uri);
		
		resp = cr.get();
		status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());   
		assertTrue(status.isSuccess());
		
		String csv = resp.getText();
		System.out.println(csv);
		
		
	}
}
