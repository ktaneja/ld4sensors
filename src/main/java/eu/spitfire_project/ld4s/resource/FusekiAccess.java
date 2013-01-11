package eu.spitfire_project.ld4s.resource;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjena.riot.Lang;
import org.openjena.riot.WebContent;

import com.hp.hpl.jena.graph.query.regexptrees.EndOfLine;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.setup.SystemParams;

import eu.spitfire_project.ld4s.server.ServerProperties;

public final class FusekiAccess {

	/**
	 * @param args
	 */
	
	private DefaultHttpClient httpclient;
	private static FusekiAccess instance;
	
	private FusekiAccess(){
		httpclient = new DefaultHttpClient();
	}
	
	public static FusekiAccess getInstance(){
		if(instance==null){
			instance = new FusekiAccess();
		}
		return instance;
	}
	
	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
		Model model = ModelFactory.createDefaultModel();
		Resource subj = model.createResource("http://localhost:8182/ld4s/platform/strengthSensor2Myr");
		Property pred = model.createProperty("http://localhost:8182/ld4s/platform/property#qualityAndFeelandLook");
		RDFNode obj = model.createResource("http://localhost:8182/ld4s/platform/MasaQkoPi4Opa");
		model.add(model.createStatement(subj, pred, obj));
//		f.create("http://localhost:3030/opa/upload", model);
		
//		f.insert("http://localhost:3030/opa/upload", model);
		FusekiAccess f = FusekiAccess.getInstance();
		f.insert("http://localhost:3030/opa/uploadBla", model);
	}
	public static void gangNamStyle() throws ClientProtocolException, IOException{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://www.vogella.com");
		HttpResponse response = client.execute(request);

		// Get the response
		BufferedReader rd = new BufferedReader
		  (new InputStreamReader(response.getEntity().getContent()));
		    
		String line = "";
		while ((line = rd.readLine()) != null) {
		  System.out.println(line);
		} 
	}
	
	private HttpEntityEnclosingRequest addBody(
			HttpEntityEnclosingRequest req, String graphIRI, Model model) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		model.write(baos, "TURTLE", graphIRI);
		ContentType contentType = ContentType.create(Lang.TURTLE
				.getContentType(), WebContent
				.getCharsetForContentType(Lang.TURTLE.getContentType()));
		HttpEntity entity = new ByteArrayEntity(baos.toByteArray(), contentType);
		req.setEntity(entity);
		return req;
	}
//	MODIFY <http://localhost:3030/opa/upload> DELETE { <http://localhost:8182/ld4s/platform/strengthSensor> <http://localhost:8182/ld4s/platform/property#quality> <http://localhost:8182/ld4s/platform/VeryGood> } INSERT {<http://localhost:8182/ld4s/platform/strengthSensor> <http://localhost:8182/ld4s/platform/property#qualityAndFeel> <http://localhost:8182/ld4s/platform/ExtremelyGood> } where {<http://localhost:8182/ld4s/platform/strengthSensor> <http://localhost:8182/ld4s/platform/property#quality> <http://localhost:8182/ld4s/platform/VeryGood>}
	
	private HttpEntityEnclosingRequest addBodyForModify(
			HttpEntityEnclosingRequest req, String graphIRI, Model model) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		model.write(baos, "TURTLE", graphIRI);
		OutputStream os = System.out;
		String queryString = "MODIFY <"+graphIRI+"> DELETE { ";
		os.write(queryString.getBytes());
		baos.write(queryString.getBytes());
		model.write(os,"N-TRIPLE",graphIRI);
		model.write(baos,"N-TRIPLE",graphIRI);
		
		queryString = "} \nINSERT {";
		os.write(queryString.getBytes());
		baos.write(queryString.getBytes());
		queryString="<http://localhost:8182/ld4s/platform/strengthSensor> <http://localhost:8182/ld4s/platform/property#qualityAndFeel> <http://localhost:8182/ld4s/platform/MasaQko4eIO6te> } " +
				"where {<http://localhost:8182/ld4s/platform/strengthSensor> <http://localhost:8182/ld4s/platform/property#qualityAndFeel> <http://localhost:8182/ld4s/platform/MasaQko>}";
		os.write(queryString.getBytes());
		baos.write(queryString.getBytes());
		String st = baos.toString();
		System.out.println("\nsss"+st);
//		baos.reset();
//		baos.write("select distinct ?a ?s ?d where {graph <http://localhost:3030/opa/upload> {?a ?s ?d}}".getBytes());
		ContentType contentType = ContentType.create(Lang.NTRIPLES
				.getContentType(), WebContent
				.getCharsetForContentType(Lang.NTRIPLES.getContentType()));
		HttpEntity entity = new ByteArrayEntity(baos.toByteArray(), contentType);
		req.setEntity(entity);
		return req;
	}
	
	public boolean create(String graphIRI, Model model) throws URISyntaxException,
			ClientProtocolException, IOException {
		URI uri = getURI(graphIRI).build();
//		URI uri = new URI("http://localhost:3030/opa/upload");
		HttpPut httpPut = new HttpPut(uri);
		StatusLine status = execute((HttpUriRequest) addBody((httpPut), graphIRI, model));
		return (status.getStatusCode()>=200 && status.getStatusCode() < 300);
	}
	
	public boolean insert(String graphIRI, Model model)
			throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = getURI(graphIRI).build();
		// URI uri = new URI("http://localhost:3030/opa/upload");
		HttpPost httpPost = new HttpPost(uri);
		StatusLine status = execute((HttpUriRequest) addBody((httpPost),
				graphIRI, model));
		return (status.getStatusCode() >= 200 && status.getStatusCode() < 300);
	}
	public boolean delete(String graphIRI, Model model)
			throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = getURI(graphIRI).build();
		// URI uri = new URI("http://localhost:3030/opa/upload");
		HttpPost httpPost = new HttpPost(uri);
		StatusLine status = execute((HttpUriRequest) addBody((httpPost),
				graphIRI, model));
		return (status.getStatusCode() >= 200 && status.getStatusCode() < 300);
	}
	
	public boolean update(String graphIRI, Model model)
			throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = getURIforUpdate(graphIRI).build();
		// URI uri = new URI("http://localhost:3030/opa/upload");
		HttpPost httpPost = new HttpPost(uri);
//		HttpPut httpPost = new HttpPut(uri);
		StatusLine status = execute((HttpUriRequest) addBodyForModify((httpPost),
				graphIRI, model));
		return (status.getStatusCode() >= 200 && status.getStatusCode() < 300);
	}
	
	private StatusLine execute(HttpUriRequest req)
			throws ClientProtocolException, IOException {
//		for (String hdr : headers.keySet()) {
//			req.addHeader(hdr, StringUtils.join(headers.get(hdr), ","));
//		}
		HttpResponse response = null;
		try {
			response = httpclient.execute(req);
			return response.getStatusLine();
		} finally {
//			if (response != null) {
//				EntityUtils.consume(response.getEntity());
//			}
		}
	}
	private URIBuilder getURI(String graphIRI) throws URISyntaxException {
		URIBuilder ub = getServerURI();
//		ub.setScheme("http");
//		ub.setHost("localhost:3030/opa/upload");
//		http://localhost:3030/opa/update
		ub.addParameter("graph", graphIRI);
		System.out.println(ub.toString());
		return ub;
	}
	private URIBuilder getURIforUpdate(String graphIRI) throws URISyntaxException {
		URIBuilder ub = getServerURI();
//		ub.setScheme("http");
//		ub.setHost("localhost:3030/opa/upload");
//		http://localhost:3030/opa/update
		ub.addParameter("graph", graphIRI);
		System.out.println(ub.toString());
		return ub;
	}
	
	
	private URIBuilder getServerURI() throws URISyntaxException {
			URIBuilder builder = new URIBuilder(
					"http://localhost:3030");
			return builder.setPath("/opa/data");

	}
	
	public static String getSPARQLQueryURL(){
//		http://localhost:3030/opa/query
		String endpointHost = System.getProperty(ServerProperties.RDF_STORE_HOSTNAME);
		String endpointPort = System.getProperty(ServerProperties.RDF_STORE_PORT);
		String url = "http://"+endpointHost+":"+endpointPort+"/opa/query";
		return url;
	}
	
	public static String getSPARQLUpdateURL(){
//		http://localhost:3030/opa/update
		String endpointHost = System.getProperty(ServerProperties.RDF_STORE_HOSTNAME);
		String endpointPort = System.getProperty(ServerProperties.RDF_STORE_PORT);
		String url = "http://"+endpointHost+":"+endpointPort+"/opa/update";
		return url;
	}
}
