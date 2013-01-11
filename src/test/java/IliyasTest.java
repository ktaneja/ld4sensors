import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import eu.spitfire_project.ld4s.lod_cloud.Person;
import eu.spitfire_project.ld4s.resource.LD4SApiInterface;
import eu.spitfire_project.ld4s.server.Server;
import eu.spitfire_project.ld4s.server.ServerProperties;
import eu.spitfire_project.ld4s.test.LD4STestHelper;


public class IliyasTest {
	/** Resource ID necessary to store locally. */
	protected String resourceId = "a12bZCreole2";
//	protected String resourceId = "rainbowSensorAB";
	
	/** Platform type. */
	protected String type = "Testbed";

	/** LD4S currently running server host. */
	protected String local_uri = "http://localhost:8182/ld4s/platform/";

	/** Resource URI necessary in case of remote resource hosting server. */
	protected String remote_uri = "http://www.example.org/platform/remotea12b";

	/** Base datetime from which the shifts are calculated. */
	protected String base_datetime = "12-08-28T19:03Z";
	
	/** Datetime at which the resource was created. */
	protected String resource_datetime = "12-08-28T19:03Z";

	/** Base host name. */
	protected String base_name = "http://www.example2.org/platform/";

	/** Temporal Platform Property IDs. */
	protected String[] tpprops = new String[]{"id123", "id456", "id789", "id101"};
	
	/** Feed URIs. */
	protected String[] feeds = new String[]{"http://uberdust.cti.gr/rest/testbed/1/georss", 
			"http://maps.google.com/maps?q=http://uberdust.cti.gr/rest/testbed/1/georss", 
			"http://uberdust.cti.gr/rest/testbed/1/kml", "http://maps.google.com/maps?q=http://uberdust.cti.gr/rest/testbed/1/kml"};
	
	/** Status page URI. */
	protected String status = "http://www.example.org/testbed/status/"+resourceId;
	
	/** Historical Archive. */
	protected String archive = "http://www.example.org/testbed/"+resourceId+"/archive";

	/** User-defined criteria for linking. */
	protected String filters = "d=crossdomain%20OR%20geography" +
	"&s=NEAR(OR(shop1, shop2,shop3))UNDER(OR(home,d'avanzo,AND(italy, OR(palace, building), bari),south-italy))" +
	"OVER(AND(floor,garden,OR(metro,train),sky))" +
	"&th=OR(red,AND(cotton,tshirt),tissue,dress)";

	/** JSONObject contatining the above data. */
	protected JSONObject json = null;

	/** Form contatining the above data. */
	protected Form form = null;
	
	/** Locations in the form <spacerel # <name | lat_long>>. */
	protected String[] locations = new String[]{" # madrid", "near # 12.009_24.500"
			, "near # 19.489_23.52", "in # spain"};
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		setupServer();
		IliyasTest it = new IliyasTest();
		System.out.println("SDSDSD " +System.getProperty(ServerProperties.RDF_STORE_HOSTNAME));
//		it.testLDPostRemoteResource();
//		it.testGet();
		it.testPut();
//		it.testLDPostLocalResource();
//		it.testLDPostLocalResource();
//		it.testDelete();
	}
	/** The LD4S server used in these tests. */
	private static Server ld4sServer;
	@BeforeClass public static void setupServer() throws Exception {
		// Create a testing version of the Ld4S.
		ld4sServer = Server.newInstance();
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
		System.out.println(resp.getText());
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
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
		form.set("base_datetime", base_datetime);
		form.set("base_name", base_name);
		form.set("type", type);
		for (int i=0; i<locations.length ;i++){
			form.set("locations", locations[i]);
		}
		for (int i=0; i<tpprops.length ;i++){
			form.set("tsproperties", tpprops[i]);
		}
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
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
		
		System.out.println("\n\n\n==============\nTesting DEVICE DELETE - "
				+local_uri+resourceId+"==============");
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
	}
	
	@Test
	public void testLDPostRemoteResource() throws Exception {
		System.out.println("Test POST remote and with external links - Java object payload");
		initJson(true, true);
		ClientResource cr = new ClientResource(local_uri);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation resp = cr.post(json);
		System.out.println(resp.getText());
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
	}
	
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
		ClientResource cr = new ClientResource(local_uri+resourceId);
		//ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, 
				//user_password);
		//cr.setChallengeResponse(authentication);
		Representation response = cr.put(json);
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());
		assertTrue(status.isSuccess());
		
		String rdf = response.getText();
		System.out.println("\n\n\n==============\nTesting DEVICE JSON PUT- " 
				+"(annotation to be stored locally) "
				+ "sent : "+json
				+local_uri+resourceId+"==============\n"+rdf);
		
		response.release();	
	}
	
	protected Person author = new Person(
//			"Ioannis", "Chatzigiannakis", null, null, null, null, null);
			"Manfred", "Hauswirth", null, null, null, null, null);
	
	private void initJson(boolean isRemote, boolean isEnriched){
		this.json = new JSONObject();
		try {
			if (isRemote){
				json.append("uri", remote_uri);
			}else{
				json.append("uri", null);
			}
			if (isEnriched){
				json.append("context", filters);	
			}else{
				json.append("context", null);
			}
			json.append("resource_datetime", resource_datetime);
			json.append("type", type);
			json.append("base_name", base_name);
			json.append("status_page", status);
			json.append("author", getAuthor(author));
			json.append("archive", archive);
			JSONArray vals = new JSONArray();
			for (int i=0; i<tpprops.length ;i++){
				vals.put(tpprops[i]);
			}
			json.append("tpproperties", vals);
			vals = new JSONArray();
			for (int i=0; i<feeds.length ;i++){
				vals.put(feeds[i]);
			}
			json.append("feeds", vals);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	protected JSONObject getAuthor(Person author) throws JSONException{
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
		return obj;
	}
	
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
		System.out.println("RESPONSE to GET REQUEST***\n"+resp.getText());
		Status status = cr.getStatus();
		System.out.println(status.getCode()+ " - "+cr.getStatus().getDescription());            
		assertTrue(status.isSuccess());
	}
}
