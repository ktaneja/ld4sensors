package eu.spitfire_project.ld4s.reasoner;

import com.hp.hpl.jena.rdf.model.Model;

import eu.spitfire_project.ld4s.resource.actuator_decision.ActuatorDecision;

/**
 * Class that selects a decision according to the custom rules and the readings collected from
 * the (filtered list of) network devices.   
 * @author iammyr
 *
 */
public class DecisionSupport extends ReasonerManager {


	/**
	 * Given the client input and custom rules, select the best decision with some confidence
	 * 
	 * @param obj 
	 * @param namedGraphUri
	 * @param datasetFolderPath
	 * @return
	 */
	public static Model applyFilter(ActuatorDecision obj, String namedGraphUri,
			String datasetFolderPath) {
		//1. write the custom rules in the text file
		
		//2. create a new ActuatorDecisionSupport resource for each decision option 
		
		//3. set the new resources as :OfInterest and remove the :OfInterest triple from any other
		//previously stored resource
		
		//4. apply the custom rule-driven inference
		
		//5. return the ActuatorDecisionSupport resourceS which will now include 
		//the confidence level for each decision option
		
		return null;
	}

	

	
	
}
