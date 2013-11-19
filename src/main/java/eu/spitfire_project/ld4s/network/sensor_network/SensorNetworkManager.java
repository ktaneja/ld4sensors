package eu.spitfire_project.ld4s.network.sensor_network;

import org.ws4d.coap.messages.CoapRequestCode;

import eu.spitfire_project.ld4s.network.NetworkAddress;
import eu.spitfire_project.ld4s.network.NetworkManager;
import eu.spitfire_project.ld4s.reasoner.NetworkDeviceFilter;
import eu.spitfire_project.ld4s.resource.actuator_decision.ActuatorDecision;

public class SensorNetworkManager extends NetworkManager {

	public SensorNetworkManager(String baseUri) {
		super(baseUri);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getSourceDescription(NetworkAddress addr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sourceFiltering(ActuatorDecision obj, NetworkDeviceFilter settings) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * resourceToQueryPath is one of SensorChoice.
	 * port for CoAP-enabled devices usually is 5683
	 * milliseconds are the milliseconds that should pass before the latest reading is pulled again
	 * 
	 *  ToDo: still unclear when to stop the latest reading pulling and what to do with the readings
	 */
	@Override
	public void getSourceResource(NetworkAddress naddr, int port, String resourceToQueryPath,
			int milliseconds) {
		QueryForwarder queryfwd = new QueryForwarder(CoapRequestCode.GET, resourceToQueryPath, naddr, port,
				milliseconds);
		Thread t = new Thread(queryfwd);
		t.start();
		//queryfwd keeps reading every x milliseconds until stopped
	}

}
