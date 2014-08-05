

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import eu.spitfire_project.ld4s.resource.LD4SDataResource;


public class RDFLoader extends LD4SDataResource{
	
	public static void main(String[] args) throws MalformedURLException {
		// Create a model and read into it from file 
		// "data.ttl" assumed to be Turtle.
		RDFLoader loader = new RDFLoader();
		
		
		
		Model model = ModelFactory.createDefaultModel();
		
		model.read(new File("/Users/kunaltaneja/tools/ld4s/ld4sensors/ld4s/sensor_metadata.rdf").toURL().toString(), "RDF/XML");
		System.out.println(model);
		
		
		// Create a new query
		String queryString = 
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
			"SELECT ?name ?type " +//?name ?type " +
			"WHERE {" +
			"<http://10.1.175.98:8080/ld4s/resource/device/VM/Qian-Demo_gui> <http://10.1.175.98:8080/ld4s/resource/property/hasSensor> ?sensor.\n"
			+ " ?sensor <http://spitfire-project.eu/ontology/ns/uom>  ?uom. \n"
			+ " ?uom <http://spitfire-project.eu/ontology/ns/hasMember>  ?member. "
			+ " ?member <http://spitfire-project.eu/ontology/ns/type> ?type. "
			+ " OPTIONAL { ?member <http://spitfire-project.eu/ontology/ns/name> ?name. }"
			+ "" + 
			"      }";
		
		
		queryString = 
				"SELECT ?sensor ?name ?type" +
				" WHERE {" + 
				"<" + "http://192.168.1.65:8080/ld4s/device/VM_Qian-Demo_gui" + ">" + " <http://spitfire-project.eu/ontology/ns/has_sensor> ?sensor.\n"
				+ " ?sensor <http://spitfire-project.eu/ontology/ns/uom>  ?uom. \n"
				+ " ?uom <http://spitfire-project.eu/ontology/ns/hasMember>  ?member. \n"
				+ " ?member <http://spitfire-project.eu/ontology/ns/type> ?type. \n"
				+ " OPTIONAL { ?member <http://spitfire-project.eu/ontology/ns/name> ?name. }"
				+ "" + 
				"      } LIMIT 10";
		
		Query query = QueryFactory.create(queryString);
		//loader.load
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		List<String> res = results.getResultVars();
				// Output query results	
		ResultSetFormatter.out(System.out, results, query);

		// Important - free up resources used running the query
		qe.close();


	}

}

