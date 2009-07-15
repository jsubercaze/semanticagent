package agent.core.memory;

import java.io.FileInputStream;

import org.apache.log4j.Logger;

/**
 * Configuration for Agent Memory
 * 
 * @version 0.3
 * @author Michal Laclavik
 * 
 */
public class Config {

	
	private static Logger log = Logger.getLogger(Config.class.getName());
	/**
	 * Properties file
	 */
	static java.util.Properties config = null;

	/**
	 * Operators file name.
	 */
	public Config(String propertyFile) {
		log.info("Trying to initiate config");
		try {
			log.debug("Trying to initiate config constants");
			config = new java.util.Properties();
			config.load(new FileInputStream(propertyFile));
			log.debug("property file loaded from " + propertyFile);

			DB_URL = config.getProperty("DB_URL");
			DB_USER = config.getProperty("DB_USER");
			DB_PASSWD = config.getProperty("DB_PASSWD");
			DB_TYPE = config.getProperty("DB_TYPE");
			DBDRIVER_CLASS = config.getProperty("DBDRIVER_CLASS");
			CREATE_MODEL = new Boolean(config.getProperty("CREATE_MODEL"))
					.booleanValue();
			REMOVE_MODEL = new Boolean(config.getProperty("REMOVE_MODEL"))
					.booleanValue();
			MEM_MODEL = new Boolean(config.getProperty("MEM_MODEL"))
					.booleanValue();
			INDIVIDUALS_FILE = config.getProperty("INDIVIDUALS_FILE");
			OUTPUT_FILE = config.getProperty("OUTPUT_FILE");
			BASE = config.getProperty("BASE");
			SOURCE_FILE = config.getProperty("SOURCE_FILE");
			KNOWLEDGE_BASE = config.getProperty("KNOWLEDGE_BASE");
			KNOWLEDGE = config.getProperty("KNOWLEDGE");
		} catch (Exception e) {
			log.error("Cannot load properties from " + propertyFile, e);
		}

	}

	/**
	 * url of database for example "jdbc:mysql://[host]/[database]"
	 */
	public String DB_URL = "";

	/**
	 * database user which has access to specified DB
	 */
	public String DB_USER = "";

	/**
	 * user's password
	 */
	public String DB_PASSWD = "";

	/**
	 * DB type - usualy MySQL
	 */
	public String DB_TYPE = "";

	/**
	 * location of jdbc driver class
	 */
	public String DBDRIVER_CLASS = "";

	/**
	 * If True it creates model in RDB (MySQL) when agent is started<br>
	 * We need to set this True only first time when running agent or if we want
	 * to recreate model from OWL files in case of recreating we need to set
	 * REMOVE_MODEL True as well in order to remove old model first OWl files
	 * which contains of Ontology definition and Ontology individuals
	 * (Instances) are defined in Ontology Config see:
	 * {@link agent.core.onto.Config#SOURCE_FILE SOURCE_FILE},
	 *{@link agent.core.memory.Config#INDIVIDUALS_FILE INDIVIDUALS_FILE}
	 */
	public boolean CREATE_MODEL = false;
	/**
	 * If True it removes model from RDB (MySQL) when agent is started
	 */
	public boolean REMOVE_MODEL = false;
	/**
	 * If True it reades model from FILE defined in ontology config properties
	 * and holds model in memory instead of MySQL database it is faster but not
	 * very good solution. When set to true REMOVE_MODEL and CREATE_MODEL is not
	 * valid see: {@link agent.core.onto.Config#SOURCE_FILE SOURCE_FILE},
	 * {@link agent.core.memory.Config#INDIVIDUALS_FILE INDIVIDUALS_FILE}
	 */
	public boolean MEM_MODEL = false;

	/**
	 * Individuals file defines path to OWL file containing of agent ontology
	 * Individuals (instances)<br>
	 * This can be loaded when Memory Config
	 * {@link agent.core.memory.Config#CREATE_MODEL CREATE_MODEL} is set True
	 */
	public String INDIVIDUALS_FILE = "";
	/**
	 * Output file defines path to OWL file where content of OM can we write
	 * when calling method {@link agent.core.memory.Memory#memory2owl
	 * memory2owl()}
	 */
	public String OUTPUT_FILE = "";

	public String BASE = "";

	public String SOURCE_FILE = "";
	
	public String KNOWLEDGE_BASE = "";
	
	public String KNOWLEDGE = "";

}
