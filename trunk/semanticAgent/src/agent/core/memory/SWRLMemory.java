package agent.core.memory;


import java.io.*;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;


import agent.core.onto.Ontology;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

/**
 * Agent Memory
 * Adaptation of Memory to SWRL support
 * @author Julien Subercaze
 * @version 0.1
 */

public class SWRLMemory {
	
  public Config config = null;
 

  /**
   * Constructor
   * @param _propertyFile - property file name where settings for agent memory are stored. Settings are defined according to {@link agent.core.memory.Config memory.Config}
   * @param _agentName name of the agent
   */
  
  public SWRLMemory(String _propertyFile, String _agentName) {
	  
  	config = new Config(_propertyFile);
  	log = Logger.getLogger(_agentName+ " " + SWRLMemory.class.getName() );
  	this.initModel();
  }
  
  /**
   * Default ontology model name
   */
  private static final String DEFAULT_MODEL_NAME = "urn:x-hp-jena:agent";
  

  /**
   * Log4j object
   */
  private Logger log = null;

  /**
   * Instanceof database connection object
   */
  private IDBConnection conn = null;

  /**
   * Instance of ontological model used in agent
   */
  private OntModel model = null;


  /**
   * State variable identifies whether model is loaded
   */
  private boolean loaded = false;

  /**
   * Reurns the agent onoloy name space
   * @return name space of agent
   */
  public String getBase() {
    return config.BASE + "#";
  }


  /**
   * This method returns db connection
   * @return instance of db connection object
   */

  public IDBConnection getConnection() {

    if (conn == null) {
      if (!loaded) {	
	      // Load the Driver
	      try {
	        Class.forName(config.DBDRIVER_CLASS); // load driver
	      }
	      catch (Exception e) {
	      //  System.out.println("failed loading driver:"+ e.toString());
	      }
	      loaded = true;
      }  
      // Create database connection
    //  System.out.println("DB:"+config.DB_URL+" user:"+ config.DB_USER+ " pass:"+config.DB_PASSWD);
      conn = new DBConnection(config.DB_URL, config.DB_USER, config.DB_PASSWD, config.DB_TYPE);
    }
    return conn;
  }

  /**
   * Creates OWL model out of defined SOURCE_FILE in OWL database and store this model
   * see: {@link agent.core.onto.Config#SOURCE_FILE SOURCE_FILE},  
   *   {@link agent.core.memory.Config#INDIVIDUALS_FILE INDIVIDUALS_FILE}  
   */

  public void createModel() {
    log.debug("creating model ...");
    ModelMaker modelMaker = ModelFactory.createModelRDBMaker (getConnection());

//    if (modelMaker.hasModel(DEFAULT_MODEL_NAME)) modelMaker.removeModel(DEFAULT_MODEL_NAME);


    model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC, null );
 

    log.debug("reading model from "+ config.SOURCE_FILE);
    model.read(config.SOURCE_FILE);
    log.debug("reading individuals from "+ config.INDIVIDUALS_FILE);
    model.read(config.INDIVIDUALS_FILE);
    log.debug("storing model into RDB ...");
    model.close();
    model = null;
  }
  /**
   * Remove model from DB
   */

  public void removeModel(){
    log.debug("removing old model if any ...");
    ModelMaker modelMaker = ModelFactory.createModelRDBMaker (getConnection());
    if (modelMaker.hasModel(DEFAULT_MODEL_NAME)) {
      modelMaker.removeModel(DEFAULT_MODEL_NAME);
      modelMaker.close();
      model = null;
    }

  }
  /**
   * Returns OWL ontological model for manipulation
   * @return ontological model
   */


  public OntModel initModel() {
    if (model == null) {
      if (config.MEM_MODEL) {
      	log.debug("Starting with MEM model ...");
      	model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        try {
            log.info("reading model from "+ config.SOURCE_FILE +" "+config.BASE);
            model.read(new FileInputStream(config.SOURCE_FILE), config.BASE);
            log.debug("reading individuals from "+ config.INDIVIDUALS_FILE);
            model.read(new FileInputStream(config.INDIVIDUALS_FILE), config.BASE);
        } catch (Exception e) {
        	log.error("Loading Model failed:"+ e.getMessage());
        	e.printStackTrace();
        }
      } else {
      	log.debug("Starting with MySQL model ...");
	      if (config.REMOVE_MODEL)
	        removeModel();
	      log.debug("Create model");
	      if (config.CREATE_MODEL)
	        createModel();
	
	      ModelMaker modelMaker = ModelFactory.createModelRDBMaker (getConnection());
	
	//    if (modelMaker.hasModel(DEFAULT_MODEL_NAME)) modelMaker.removeModel(DEFAULT_MODEL_NAME);
	
	      OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
	      ontModelSpec.setBaseModelMaker(modelMaker);
	      ontModelSpec.setDocumentManager(new OntDocumentManager());
	
	      Model base = modelMaker.openModel(DEFAULT_MODEL_NAME);
	      model = ModelFactory.createOntologyModel(ontModelSpec, base);
      }


    }

    if (model != null)
      return model;
    else
      return null;
  }
/**
 * 
 * @return the model for manipulation
 */
  public OntModel getModel() {
	return model;
}


/**
   * Infers some new statements about the agent ontological model accoding to given rules of inference
   * <br> and adds inferenced model as submodel of agent
   * @param ruleFile path to file with line delimited list of rules
   * @param derivationLogging true/false value indicates whether to perform logging of inferenced statements
   * <br> false value accelerate inferece processing <br>
   * @see <a HREF="http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/rdf/model/InfModel.html#setDerivationLogging(boolean)">setDerivationLogging</a>
   */
  public void performInference(String ruleFile, boolean derivationLogging) {
    log.debug("Perform inference");
    // prepare rules
    String rule = null, rules = "";
    File file = new File(ruleFile);
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      while ((rule = reader.readLine()) != null)
        rules += rule;
      log.debug(rules);
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
    Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
    reasoner.setDerivationLogging(derivationLogging);
    InfModel infModel = ModelFactory.createInfModel(reasoner, this.getModel());
    infModel.prepare();
    this.getModel().add(infModel.getDeductionsModel(), true);
  }



  /**
   * Sometimes if operation is idle MySQL connection fails and we need to reset connection and model 
   */
  public void reset() {
  	model = null;
  	conn = null;
    ModelMaker modelMaker = ModelFactory.createModelRDBMaker (getConnection());

    OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
    ontModelSpec.setBaseModelMaker(modelMaker);
    ontModelSpec.setDocumentManager(new OntDocumentManager());
	
    Model base = modelMaker.createModel(DEFAULT_MODEL_NAME);
    model = ModelFactory.createOntologyModel(ontModelSpec, base);
  }


  /**
   * Prints objects from OWL model for testing purposes
   */
  public void test() {
    Iterator i = this.getModel().listObjects();
    while (i.hasNext()) {
      Object o = i.next();
      //if (o instanceof OntClass) {
      System.out.println(o.toString());
      //}
    }
  }

  /**
   * Saves current content of Memory into file defined in Ontology Property file
   * see: {@link agent.core.memory.Config#OUTPUT_FILE OUTPUT_FILE}  
   */
  public void memory2owl() {
  	try {
	  	Model m = this.getModel();
	  	log.debug("Creating file:"+config.OUTPUT_FILE);
	  	OutputStream file = new FileOutputStream(config.OUTPUT_FILE);
	  	m.write(file,"RDF/XML", config.BASE);
	  	file.close();
	    
    } catch (Exception e) {
        System.out.print(e.toString());
    }
  }

  
  /**
   * Close OWL Model and store Model in RDB if using database backend
   * This have to be redesigned and store everything on the crash
   */
  public void closeModel() {
    getModel().close();
    model = null;
  }

  /**
   * Creates ontology property out ofstring represenation
   * @param property represents local name of ontology property
   */
  public Property createProperty(String property) {
    return getModel().createProperty(getBase() + property);

  }

  /**
   * Tests the status of connection. If it doesn't exist then make new connection to database and
   *  initialize model
   */
  public void testConnection() {
    log.debug("Test Connection ...");
    if (conn != null)
      try {
        if (!conn.containsModel(DEFAULT_MODEL_NAME)) {
          conn.close();
          conn = null;
          model = getModel();
        }
      }
      catch (Exception e) {
        log.error(e);
      }
  }


public void setModel(OntModel model) {
	this.model = model;
}

  

}