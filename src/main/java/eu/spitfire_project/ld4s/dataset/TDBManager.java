package eu.spitfire_project.ld4s.dataset;

import java.io.File;

import org.w3c.dom.Document;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

import eu.spitfire_project.ld4s.resource.sparql.SparqlResultsFormatter;

public class TDBManager {

	


	/**
	 * Initialize a connection with the triple db
	 */
	public static Dataset initTDB(String datasetFolderPath){
		Dataset dataset = null;
		if (folderValidity(datasetFolderPath)){
			dataset = TDBFactory.createDataset(datasetFolderPath) ;
			TDB.sync(dataset ) ;
		}
		return dataset;
	}

	private static boolean folderValidity(String datasetFolderPath) {
		boolean ret = true;
		File dirf = new File (datasetFolderPath);

		if (!dirf.exists()){
			ret = dirf.mkdirs();
		}
		return ret;
	}

	/**
	 * Close connection with the triple db
	 */
	public static void closeTDB(Dataset dataset){
		if (dataset != null){
			dataset.close() ;
		}
	}

	/**
	 * Store the given model in the triple db
	 * @param rdfData model to be stored
	 * @return success
	 */
	public static boolean store(Model rdfData, Reasoner reasoner, String namedGraphUri, String datasetFolderPath){
		boolean ret = true;

		Dataset dataset = initTDB(datasetFolderPath);
		dataset.begin(ReadWrite.WRITE) ;		
		try {
			Model modelOfInterest = null;
			
			if (!dataset.containsNamedModel(namedGraphUri)){				
				modelOfInterest = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
				dataset.addNamedModel(namedGraphUri, modelOfInterest);				
			}else{
				modelOfInterest = dataset.getNamedModel(namedGraphUri) ;
			} 
			if (reasoner != null){
				modelOfInterest.add(ModelFactory.createInfModel(reasoner, rdfData));
			}else{
				modelOfInterest.add(rdfData);
			}
			
			dataset.commit() ;

		}catch(Exception e){
			e.printStackTrace();
			ret = false;

		}  finally { 
			dataset.end() ;
			closeTDB(dataset);
		}
		return ret; 
	}

	public static Model open(String subjectUri, String namedGraphUri, String datasetFolderPath){
		Model ret = ModelFactory.createDefaultModel();

		Dataset dataset = initTDB(datasetFolderPath);
		dataset.begin(ReadWrite.READ) ;		
		try {
			Model modelOfInterest = dataset.getNamedModel(namedGraphUri) ;
			ret. add(modelOfInterest.listStatements(modelOfInterest.createResource(subjectUri), 
					null, (RDFNode)null));
			
		}catch(Exception e){
			e.printStackTrace();
			ret = null;

		}  finally { 
			dataset.end() ;
			closeTDB(dataset);
		}
		return ret; 
	}

	
	
	public static ResultSet search(String queryString, String datasetFolderPath){
		ResultSet ret = null;
		if (queryString == null || datasetFolderPath == null){
			return null;
		}
		Query query = QueryFactory.create(queryString);
		
		Dataset dataset = initTDB(datasetFolderPath);
		dataset.begin(ReadWrite.READ) ;		
		try {
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			ret = qexec.execSelect();

		}catch(Exception e){
			e.printStackTrace();
			ret = null;

		}finally { 
			dataset.end() ;
			closeTDB(dataset);
		}
		return ret;
	}

	public static Document formatSearchResults(ResultSet rs){
		return (Document)SparqlResultsFormatter.xmlResults(rs)[0];
	}
	
	



}
