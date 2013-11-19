package eu.spitfire_project.ld4s.network;

import java.util.LinkedList;

public class NetworkAddress {

	private LinkedList<String> gatewayAddresses = new LinkedList<String>();

	private String localAddress = null;


	public NetworkAddress(LinkedList<String> gatewayAddresses, String localAddress){
		this.setGatewayAddresses(gatewayAddresses);
		this.setLocalAddress(localAddress);
	}

	public NetworkAddress(){
		new NetworkAddress(null, null);	
	}

	public LinkedList<String> getGatewayAddresses() {
		return gatewayAddresses;
	}

	public void setGatewayAddresses(LinkedList<String> gatewayAddresses) {
		this.gatewayAddresses = gatewayAddresses;
	}
	
	public void setGatewayAddresses(String gatewayAddress) {
		this.gatewayAddresses.add(gatewayAddress);
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

}
