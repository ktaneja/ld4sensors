package eu.spitfire_project.ld4s.resource.ov;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
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

/**
 * Test all the HTTP requests supported by the REST API
 * for the exposed resource OV (ObservationValue), submitting
 * a JSON payload that follows the best-practise
 * @author Myriam Leggieri <iammyr@email.com>
 *
 */
public class TestOVRestApi extends LD4STestHelper {
	/** Resource ID necessary to store locally. */
	protected String resourceId = "x12y";

	/** LD4S currently running server host. */
	protected String local_uri = "http://localhost:8182/ld4s/ov/";

	/** Resource URI necessary in case of remote resource hosting server. */
	protected String remote_uri = "http://www.example.org/ov/remotex12y";

	/** */
	private String source = "http://localhost:8182/ld4s/device/test1";

	/** Observed values. */
	protected String[] values = new String[]{"12.4", "21.9", "88.7", "24.5"};

	/** User-defined criteria for linking. */
	protected String filters = "d=crossdomain%20OR%20geography" +
	"&s=NEAR(OR(shop1, shop2,shop3))UNDER(OR(home,d'avanzo,AND(italy, OR(palace, building), bari),south-italy))" +
	"OVER(AND(floor,garden,OR(metro,train),sky))" +
	"&th=OR(red,AND(cotton,tshirt),tissue,dress)";

	/** JSONObject contatining the above data. */
	protected JSONObject json = null;

	/** Form contatining the above data. */
	protected Form form = null;

//	/** Java object contatining the above data. */
//	protected OV ov = null;

/**
 * best-practise compliant json payload for OV (intrinsic):
 * values
 * start_range
 * end_range
 */
	private void initJson(boolean isRemote, boolean isEnriched){
		this.json = new JSONObject();
		try {
			if (isRemote){
				json.append("uri", remote_uri);
			}else{
				json.append("uri", null);
			}
//			if (isEnriched){
//				json.append("context", filters);	
//			}else{
//				json.append("context", null);
//			}
			json.append("start_range", start_range);
			json.append("end_range", end_range);
			JSONArray vals = new JSONArray();
			for (int i=0; i<values.length ;i++){
				vals.put(values[i]);
			}
			json.append("values", vals);
			json.append("source", source);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void initForm(boolean isRemote, boolean isEnriched){
		this.form = new Form();
		if (isRemote){
			form.set("uri", remote_uri);
		}else{
			form.set("uri", null);
		}
		if (isEnriched){
			form.set("context", filters);	
		}else{
			form.set("context", null);
		}
		form.set("start_range", start_range);
		form.set("end_range", end_range);
		for (int i=0; i<values.length ;i++){
			form.set("values", values[i]);
		}
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
	public void testPut() throws Exception {
		System.out.println("Test Put - java object payload");
		initJson(false, false); 
		resourceId = resourceId+"?sep="+SPARQL_ENDPOINT_URI_UPDATE;
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		List<Preference<MediaType>> accepted = new LinkedList<Preference<MediaType>>();
		accepted.add(new Preference<MediaType>(MediaType.APPLICATION_RDF_XML));
		cr.getClientInfo().setAcceptedMediaTypes(accepted);
		Representation response = cr.put(json); 
		
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		String rdf = response.getText();
		System.out.println("\n\n\n==============\nTesting OV JSON PUT- " 
				+"(annotation to be stored locally) "
				+ "sent : "+json
				+local_uri+resourceId+"==============\n"+rdf);
		
		response.release();	
	}
	
	/**
	 * Test PUT {host}/ov/{id}
	 * requirement: resource stored locally + no Linked Data enrichment
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testFormPut() throws Exception {
		System.out.println("Test Put - form payload");
		initForm(false, false); 
		resourceId = resourceId+"?sep="+SPARQL_ENDPOINT_URI_UPDATE;
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation response = cr.put(form); 
		
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
		initJson(false, false); 
		resourceId = resourceId+"?sep="+SPARQL_ENDPOINT_URI_UPDATE;
		ClientResource cr = new ClientResource(local_uri+resourceId);
//		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
//				//user_password);
//		//cr.setChallengeResponse(authentication);
		Representation response = cr.put(json); 
		
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		
		String rdf = response.getText();
		System.out.println("\n\n\n==============\nTesting OV JSON PUT " +
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
		
		resourceId = resourceId+"?sep="+SPARQL_ENDPOINT_URI_QUERY;
		ClientResource cr = new ClientResource(local_uri+resourceId);
//		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
//				//user_password);
//		//cr.setChallengeResponse(authentication);
		List<Preference<MediaType>> accepted = new LinkedList<Preference<MediaType>>();
		accepted.add(new Preference<MediaType>(MediaType.APPLICATION_RDF_TURTLE));
		cr.getClientInfo().setAcceptedMediaTypes(accepted);
		Representation resp = cr.get();
		
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		String rdf = resp.getText();
		System.out.println("\n\n\n==============\nTesting OV GET - "
				+local_uri+resourceId+"==============\n"+rdf);
	}

	/**
	 * Test GET {host}/ov/{id}?d=&s=&th=&trange=
	 * requirement: resource stored locally
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testLDGet() throws Exception {
		System.out.println("Test Get with query string appended");
		ClientResource cr = new ClientResource(local_uri+resourceId+"?"+filters + "&sep="+SPARQL_ENDPOINT_URI_QUERY );
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
		System.out.println("\n\n\n==============\nTesting OV LD GET - " 
				+ "sent : "+json
				+local_uri+resourceId+"?"+filters+"==============\n"+rdf);
	}

	/**
	 * Test DELETE {host}/ov/{id}
	 * requirement: resource stored locally
	 *
	 * @throws Exception If problems occur.
	 */
//	@Test
	public void testDelete() throws Exception {
		System.out.println("Test Delete");
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		LD4SApiInterface resource = cr.wrap(LD4SApiInterface.class);
		resource.remove();

		Status status = cr.getStatus();
		
		System.out.println("\n\n\n==============\nTesting OV DELETE - "
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
	public void testFormPostLocalResource() throws Exception {
		System.out.println("Test POST local and with no external links - Form payload");
		initForm(false, false);
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
	 * Test POST {host}/ov/{id}
	 * requirement: resource stored locally + Linked Data enrichment
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testLDPostLocalResource() throws Exception {
		System.out.println("Test POST local and with external links - Java object payload");
		initJson(false, true);
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation resp = cr.post(json);
		
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		String rdf = resp.getText();
		System.out.println("\n\n\n==============\nTesting OV LD JSON POST - " 
				+"(annotation to be stored locally) "
				+ "sent : "+json
				+local_uri+resourceId+"==============\n"+rdf);
		
	}

	/**
	 * Test POST {host}/ov
	 * requirement: resource stored remotely
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testJSONPostRemoteResource() throws Exception {
		System.out.println("Test POST remote and with no external links - JSON payload");
		initJson(true, false);
		System.out.println(json.toString());		 
		ClientResource cr = new ClientResource(local_uri);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation resp = cr.post(json);
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		String rdf = resp.getText();
		System.out.println("\n\n\n==============\nTesting OV JSON POST " +
				"(annotation to be soterd remotely)\n"
				+ "sent : "+json
				+local_uri+"==============\n"+rdf);
	}

	/**
	 * Test POST {host}/ov
	 * requirement: resource stored remotely + Linked Data enrichment
	 *
	 * @throws Exception If problems occur.
	 */
	@Test
	public void testLDPostRemoteResource() throws Exception {
		System.out.println("Test POST remote and with external links - Java object payload");
		initJson(true, true);
		ClientResource cr = new ClientResource(local_uri);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation resp = cr.post(json);
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		String rdf = resp.getText();
		System.out.println("\n\n\n==============\nTesting OV LD JSON POST - " 
				+"(annotation to be stored remotely) "
				+ "sent : "+json
				+local_uri+"==============\n"+rdf);
	}
}
