package eu.spitfire_project.ld4s.vocabulary;


import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.TransitiveProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class SptCtVocab {
	/**
	   * <p>
	   * The ontology model that holds the vocabulary terms
	   * </p>
	   */
	  public static OntModel m_model = ModelFactory.createOntologyModel(
			  OntModelSpec.OWL_MEM, null);

	  /**
	   * <p>
	   * The namespace of the vocabulary as a string
	   * </p>
	   */
	  public static final String NS = "http://spitfire-project.eu/ontology/ns/ct/";

	  /**
	   * <p>
	   * The namespace of the vocabulary as a string
	   * </p>
	   *
	   * @see #NS
	   */
	  public static String getURI() {
	    return NS;
	  }

	  /**
	   * <p>
	   * The namespace of the vocabulary as a resource
	   * </p>
	   */
	  public static final Resource NAMESPACE = m_model.createResource(NS);

	  /** PREFIX. */
	  public static final String PREFIX = "spt-ct";

	  // Vocabulary properties
	  // /////////////////////////

	  public static final TransitiveProperty TARGET = m_model
			    .createTransitiveProperty(SptSnVocab.NS+"target");
	  public static final TransitiveProperty PROPERTY_OF = m_model
			    .createTransitiveProperty(SptSnVocab.NS+"propertyOf");
	  public static final TransitiveProperty HAS_PROPERTY = m_model
			    .createTransitiveProperty(SptSnVocab.NS+"property");
	  public static final TransitiveProperty HAS_PURPOSE = m_model
			    .createTransitiveProperty(SptSnVocab.NS+"purpose");
	
	  
	  // Vocabulary classes
	  // /////////////////////////


	  public static final OntClass APPLICATION_DOMAIN = m_model
			    .createClass(SptSnVocab.NS+"ApplicationDomain");
	  public static final OntClass ACTUATOR_DECISION = m_model
			    .createClass(SptSnVocab.NS+"ActuatorDecision");
	  public static final OntClass ACTUATOR_DECISION_SUPPORT = m_model
			    .createClass(SptSnVocab.NS+"ActuatorDecisionSupport");
	  public static final OntClass ENTERTAINMENT = m_model
			    .createClass(SptSnVocab.NS+"Entertainment");
	  public static final OntClass MUSIC = m_model
			    .createClass(SptSnVocab.NS+"Music");
	  public static final OntClass PURPOSE = m_model
			    .createClass(SptSnVocab.NS+"Purpose");
	  public static final OntClass RELAX = m_model
			    .createClass(SptSnVocab.NS+"Relax");
	  
	  
	  // Vocabulary individuals
	  // /////////////////////////

	  public  SptCtVocab(){
		  ENTERTAINMENT.addProperty(RDFS.subClassOf, APPLICATION_DOMAIN);
		  MUSIC.addProperty(RDFS.subClassOf, ENTERTAINMENT);
		  RELAX.addProperty(RDFS.subClassOf, PURPOSE);
		  
		  ENTERTAINMENT.addProperty(TARGET, FOAF.Person);		  
		  
		  PROPERTY_OF.addInverseOf(HAS_PROPERTY);
		  
		  
		  
	  }

}

