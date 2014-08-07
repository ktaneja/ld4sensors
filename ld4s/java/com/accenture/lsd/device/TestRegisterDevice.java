package com.accenture.lsd.device;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;

import eu.spitfire_project.ld4s.test.LD4STestHelper;
import eu.spitfire_project.ld4s.vocabulary.EQIQVocab;

public class TestRegisterDevice extends LD4STestHelper{
	
	JSONObject json;
	
	/** Resource ID necessary to store locally. */
	protected String resourceId ;

	protected String local_ip = "localhost";
	/** LD4S currently running server host. */
	protected String local_uri = "http://localhost:8080/ld4s/device/";
	
	protected String base_uri = "http://localhost:8080/ld4s/";

	/** Resource URI necessary in case of remote resource hosting server. */
	protected String remote_uri = "http://www.example.org/device/remotea12b";

	/** Milliseconds shift from the base time. */
	protected String base_datetime = "12-08-28T19:03Z";

	/** Base OV host name. */
	protected String base_ov_name = "http://www.example1.org/ov/";

	/** Base host name. */
	protected String base_name = "http://www.example2.org/device/";

	/** Observed Property. */
	protected String observed_property ;

	/** Temporarily: to enhance the link search for the observed property. */
	protected String foi = "room";
	
	/** Preferred type. */
	protected String type = "temperature sensor";
	
	/** Unit of Measurement. */
	protected String uom = "centigrade";
	
	protected String owner;
	
	protected String capability;
	
	protected String hasSensors[];
	
	
	private void initJson(LsdDevice device) throws JSONException{
		Gson gson = new Gson();
		String jsonString = gson.toJson(device);
		json = new JSONObject(jsonString);
	}
	
	
	@Test
	public void testRegisterDevice() throws IOException, JSONException{
		//System.out.println(Runtime.getRuntime().exec("hostname"));
		ClientResource cr;
		Representation resp;
		Status status;

		local_uri = "http://localhost:8080/ld4s/lsddevice/";
		resourceId = "VM_Qian-Demo_gui";
		hasSensors = new String[1];
		hasSensors[0] = EQIQVocab.VCenter_1.getURI();
		type = "Virtual Machine";
		location_coords = "37.3333_121.9000_0.0";
		LsdDevice device = new LsdDevice(resourceId, hasSensors, type, location_coords);
		
		initJson(device);
		System.out.println(json.toString());		 
		cr = new ClientResource(local_uri+resourceId);
		
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
	
	
	//@Test
	public void testSelect() throws Exception {
		String filters = "SELECT * " +
				"FROM NAMED <http://10.1.175.81:8080/ld4s/graph/ov> " +
				"{" +
						"?x ?y ?z" 
				+ "}" 
				+"LIMIT 10";
		
		

		
		//filters = "SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?p ?o}}";
		filters = URLEncoder.encode(filters, "utf-8");
		ClientResource cr = new ClientResource(
				"http://localhost:8080/ld4s/device/sparql?q="+filters);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		List<Preference<MediaType>> accepted = new LinkedList<Preference<MediaType>>();
		accepted.add(new Preference<MediaType>(MediaType.APPLICATION_RDF_XML));
		cr.getClientInfo().setAcceptedMediaTypes(accepted);
		Representation resp = cr.get();
		System.out.println("RESPONSE to the SPARQL QUERY***\n"+resp.getText());
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
	}

}
