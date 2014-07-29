

import java.io.File;
import java.net.MalformedURLException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;


public class RDFLoader {
	
	public static void main(String[] args) throws MalformedURLException {
		// Create a model and read into it from file 
		// "data.ttl" assumed to be Turtle.
		FileManager fm = new FileManager();
		
		Model model = ModelFactory.createDefaultModel();
		
		model.read(new File("/Users/Kunal/Documents/sensor_metadata.rdf").toURL().toString(), "RDF/XML");
		System.out.println(model);

	}

}
