package eu.spitfire_project.ld4s.network;

import java.util.LinkedList;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import eu.spitfire_project.ld4s.reasoner.NetworkDeviceFilter;
import eu.spitfire_project.ld4s.resource.actuator_decision.ActuatorDecision;

public abstract class NetworkManager {

	/** List of network device descriptions to store. */ 
	/** Temporarily hard-coded RDF file paths until a broadcasted request for descriptions is implemented. */
	protected LinkedList<String> descriptionFilePaths = null;

	private String namedGraphUri = "network"; 

	/**
	 * Scan the network for collecting RDF descriptions from each surrounding neighbours
	 * 
	 * @return Model where the collected RDF descriptions are merged 
	 */
	public Model sourceDiscovery(){
		Model ret = ModelFactory.createDefaultModel();
		
		/** Temporarily hard-coded RDF file paths until a broadcasted request for descriptions is implemented. */
		descriptionFilePaths.add("/mnt/windows/myr/phd_sub/my-prototype/sam-middleware/spitfire1.xml");
		
		for (String filename : descriptionFilePaths){
			ret.add(FileManager.get().loadModel(filename));
		}
		return ret;
	}

	public abstract void getSourceDescription(NetworkAddress addr);

	public abstract void sourceFiltering(ActuatorDecision obj, NetworkDeviceFilter settings);


	public NetworkManager(String baseUri){
		descriptionFilePaths = new LinkedList<String>();
		namedGraphUri = baseUri + this.getNamedGraphUri();
	}

	public String getNamedGraphUri() {
		return namedGraphUri;
	}

	public abstract void getSourceResource(NetworkAddress naddr, int port,
			String resourceToQueryPath, int milliseconds) ;


}
