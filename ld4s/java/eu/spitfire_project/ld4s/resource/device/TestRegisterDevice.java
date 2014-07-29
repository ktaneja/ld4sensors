package eu.spitfire_project.ld4s.resource.device;

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

import eu.spitfire_project.ld4s.test.LD4STestHelper;

public class TestRegisterDevice extends LD4STestHelper{
	
	JSONObject json;
	
	/** Resource ID necessary to store locally. */
	protected String resourceId ;

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
	
	
	private void initJson(boolean isRemote, boolean isEnriched){
		this.json = new JSONObject();
		try {
			json.append("uri", null);
			json.append("context", null);
			//json.append("base_datetime", base_datetime);
			json.append("base_name", base_name);
			json.append("base_ov_name", base_ov_name);
			json.append("observed_property", observed_property);
			json.append("foi", foi);
			json.append("uom", uom);
			json.append("type", type);
			json.append("owner", owner);
			JSONObject obj = new JSONObject();
			if (author.getFirstname() != null){
				obj.append("firstname", author.getFirstname());
			}
			if (author.getSurname() != null){
				obj.append("surname", author.getSurname());
			}
			if (author.getEmail() != null){
				obj.append("email", author.getEmail());
			}
			if (author.getHomepage() != null){
				obj.append("homepage", author.getHomepage());
			}
			if (author.getNickname() != null){
				obj.append("nickname", author.getNickname());
			}
			if (author.getWeblog() != null){
				obj.append("weblog", author.getWeblog());
			}
			JSONArray vals = new JSONArray();
			for (int i=0; hasSensors != null && i<hasSensors.length ;i++){
				vals.put(hasSensors[i]);
			}
			json.append("has_sensors", vals);
			
			json.append("location-coords", location_coords);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	
	@Test
	public void testRegisterDevice() throws IOException{
		
		ClientResource cr;
		Representation resp;
		Status status;
		
		resourceId = "cpu_sensor"; 
		observed_property = "http://10.1.175.81/ld4s/resource/property/cpu_usage";
		uom = "http://localhost:8182/ld4s/resource/uom/percentage";
		capability = "http://10.1.175.81/ld4s/resource/meas_capab/standard_capability";
		type = "CPU Sensor";
		location_coords = "37.3333_121.9000";
		//location = "San Jose, CA";
		
		initJson(false, false);
		System.out.println(json.toString());		 
		cr = new ClientResource(local_uri+resourceId);
		resp = cr.post(json);
		status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		resourceId = "memory_sensor"; 
		observed_property = "http://10.1.175.81/ld4s/resource/property/memory_usage";
		uom = "http://localhost:8182/ld4s/resource/uom/percentage";
		capability = "http://10.1.175.81/ld4s/resource/meas_capab/standard_capability";
		type = "Memory Sensor";
		
		initJson(false, false);
		System.out.println(json.toString());	
		cr = new ClientResource(local_uri+resourceId);
		resp = cr.post(json);
		status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		observed_property = null;
		uom = null;
		capability = null;
		
		local_uri = "http://localhost:8080/ld4s/device/";
		resourceId = "VM_Qian-Demo_gui";
		hasSensors = new String[2];
		hasSensors[0] = "http://10.1.175.81/ld4s/resource/device/cpu_sensor";
		hasSensors[1] = "http://10.1.175.81/ld4s/resource/device/memory_sensor";
		observed_property = "http://10.1.175.81/ld4s/resource/property/memory_usage";
		uom = "http://localhost:8080/ld4s/resource/uom/percentage";
		type = "Virtual Machine";
		owner = "http://10.1.175.81/ld4s/resource/people/QianZhu";
		
		initJson(false, false);
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
	
	
	@Test
	public void testSelect() throws Exception {
		String filters = "SELECT * " +
				"FROM NAMED <http://10.1.175.81:8080/ld4s/graph/ov> " +
				"{" +
						"?x ?y ?z" 
				+ "}" 
				+"LIMIT 10";
		
		filters = "SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?p ?o}}";
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
