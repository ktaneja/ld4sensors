package eu.spitfire_project.ld4s.resource.type;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import eu.spitfire_project.ld4s.resource.LD4SApiInterface;
import eu.spitfire_project.ld4s.test.LD4STestHelper;

public class TestTypeRestApi extends LD4STestHelper {
	/** Resource ID necessary to store locally. */
	protected String resourceId = "SampleType";

	/** LD4S currently running server host. */
	protected String local_uri = "http://localhost:8182/ld4s/type/";

	/** Measurement Capability URI. */
	protected String meas_capab = "http://www.example.org/meas_capab/12b";

	/** JSONObject contatining the above data. */
	protected JSONObject json = null;

	/** Form contatining the above data. */
	protected Form form = null;

//	/** Java object contatining the above data. */
//	protected OV ov = null;


	private void initJson(){
		this.json = new JSONObject();
		try {
			json.append("measurement_capability", meas_capab);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	private void initForm(){
		this.form = new Form();
		form.set("measurement_capability", meas_capab);
		
	}
	
//	private void initOV(boolean isRemote, boolean isEnriched){
//		String remote = null, filters = null;
//		try {
//			if (isRemote){
//				remote = this.remote_uri;
//			}
//			if (isEnriched){
//				filters = this.filters;
//			}
//			this.ov = new OV(remote, values, base_datetime, resource_time, start_range, end_range, filters, local_uri);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//	}
	

	
	
	/**
	 * Test PUT {host}/ov/{id}
	 * requirement: resource stored locally + no Linked Data enrichment
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testFormPut() throws Exception {
		System.out.println("Test Put - form payload");
		initForm(); 
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation response = cr.put(form); 
		System.out.println(response.getText());
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		response.release();	
	}
	
	/**
	 * Test PUT {host}/ov/{id}
	 * requirement: resource stored locally + no Linked Data enrichment
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testJSONPut() throws Exception {
		System.out.println("Test Put - JSON payload");
		initJson(); 
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation response = cr.put(json); 
		System.out.println(response.getText());
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		String rdf = response.getText();
		System.out.println("\n\n\n==============\nTesting TYPE JSON PUT " +
				"(annotation to be soterd locally)\n"
				+ "sent : "+json
				+local_uri+resourceId+"==============\n"+rdf);
		
		response.release();	
	}

	/**
	 * Test GET {host}/ov/{id}
	 * requirement: resource stored locally
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testGet() throws Exception {
		System.out.println("Test Get");
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		List<Preference<MediaType>> accepted = new LinkedList<Preference<MediaType>>();
		accepted.add(new Preference<MediaType>(MediaType.APPLICATION_RDF_TURTLE));
		cr.getClientInfo().setAcceptedMediaTypes(accepted);
		Representation resp = cr.get();
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		String rdf = resp.getText();
		System.out.println("\n\n\n==============\nTesting TYPE GET - "
				+local_uri+resourceId+"==============\n"+rdf);
	}

	

	/**
	 * Test DELETE {host}/ov/{id}
	 * requirement: resource stored locally
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testDelete() throws Exception {
		System.out.println("Test Delete");
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		LD4SApiInterface resource = cr.wrap(LD4SApiInterface.class);
		resource.remove();

		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		System.out.println("\n\n\n==============\nTesting TYPE DELETE - "
				+local_uri+resourceId+"==============");
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
	}

	/**
	 * Test POST {host}/ov/{id}
	 * requirement: resource stored locally
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testFormPost() throws Exception {
		System.out.println("Test POST - Form payload");
		initForm();
		System.out.println(form.toString());		 
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		cr.post(form);
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
	}

	

	

	/**
	 * Test POST {host}/ov
	 * requirement: resource stored remotely + Linked Data enrichment
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testJSONPost() throws Exception {
		System.out.println("Test POST - JSON object payload");
		initJson();
		ClientResource cr = new ClientResource(local_uri);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation resp = cr.post(json);
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		String rdf = resp.getText();
		System.out.println("\n\n\n==============\nTesting TYPE JSON POST - " 
				+"(annotation to be stored remotely) "
				+ "sent : "+json
				+local_uri+"==============\n"+rdf);
	}
}
