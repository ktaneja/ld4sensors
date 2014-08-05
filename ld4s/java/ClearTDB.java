import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

import eu.spitfire_project.ld4s.resource.LD4SDataResource;
import eu.spitfire_project.ld4s.vocabulary.LD4SConstants;




public class ClearTDB extends LD4SDataResource{
	private final String foldername = System.getProperty("user.home") + LD4SConstants.SYSTEM_SEPARATOR
			+".ld4s";

	protected void addToTDB() throws MalformedURLException{
		
		Model model = ModelFactory.createDefaultModel();
		
		model.read(new File("/Users/kunaltaneja/tools/ld4s/ld4sensors/ld4s/sensor_metadata.rdf").toURL().toString(), "RDF/XML");
		System.out.println(model);
		
		initTDB();
		this.dataset.begin(ReadWrite.WRITE) ;
		dataset.addNamedModel("192.168.1.65:8080/ld4s/graph/", model);
		dataset.commit() ;
		
	}
	
	protected void cleanTDB(){
		initTDB();
		TDB.sync(dataset);
		this.dataset.begin(ReadWrite.WRITE) ;
		Iterator<String> list = dataset.listNames();
		List<String> names = new ArrayList<String>();
		while(list.hasNext()){
			String name = list.next();
			names.add(name);
			System.out.println(name);

		}
		for(String name: names){
			dataset.removeNamedModel(name);
		}
		dataset.commit() ;
	}

	private void initTDB(){
		// Direct way: Make a TDB-backed dataset

		String directory = foldername +
				LD4SConstants.SYSTEM_SEPARATOR+"tdb"
				+LD4SConstants.SYSTEM_SEPARATOR+"LD4SDataset1" ;
		System.out.println(directory);
		File dirf = new File (directory);
		if (!dirf.exists()){
			dirf.mkdirs();
		}
		dataset = TDBFactory.createDataset(directory) ;
		TDB.sync(dataset ) ;
	}

	public static void main(String[] args) throws MalformedURLException {
		ClearTDB clearDB = new ClearTDB();
		clearDB.cleanTDB();
		clearDB.addToTDB();

	}

}
