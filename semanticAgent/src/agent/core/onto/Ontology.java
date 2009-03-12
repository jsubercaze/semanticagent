package agent.core.onto;




/**
 * Title: Agent Ontology<br/>
 * Description: Provides constants and utilities for manipulating OWL ontology needed for Agents
 * @author Michal Laclavik
 * @version 0.5
 */

public class Ontology {
	/**
	 * RDF holds RDF uri "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	 */
	  public static String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	/**
	 * RDFS holds RDFS uri "http://www.w3.org/2000/01/rdf-schema#"
	 */

	  public static String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	/**
	 * OWL holds OWL uri "http://www.w3.org/2002/07/owl#"
	 */

	  public static String OWL = "http://www.w3.org/2002/07/owl#";


	/**
	 * SPARQL_PREFIX is used in SPARQL queries where we can write &lt;ont:something&gt; or &lt;rdf:something&gt; instead of full agent ontology uri or RDF uri
	 */
	  //public static String SPARQL_PREFIX = 		  "PREFIX ont: <"+Config.BASE + "#> \nPREFIX rdf: <"+RDF+">\nPREFIX rdfs: <"+RDFS+"> \n";

	/**
	 * SPARQL is used in inter agent communication to specify using of SPARQL language  
	 */
	  public static String SPARQL = "SPARQL";
	  
	  /**
     * BASE returns agent ontology uri 
	 */	  
	  //public static String BASE = Config.BASE;

	/**
	 * Used for registering of agents into Directory Facilitator  
	 */	  
	
	  public static String AGENT_OWNERSHIP = "IISAS";
	
	/**
	 * OWL_HEAD is used to add RDF, OWL and agent ontology headers to Event messages comming through XML-RPC  
	 */	  
	
	
	  /*public static String OWL_HEAD =
	    "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"+
	    "<rdf:RDF\n"+
	    "    xmlns=\""+ Config.BASE + "#\"\n"+
	    "    xmlns:rdf=\"" + RDF + "\"\n"+
	    "    xmlns:rdfs=\"" + RDFS + "\"\n"+
	    "    xmlns:owl=\"" + OWL + "\"\n"+
	    "  xml:base=\""+ Config.BASE + "\">\n";*/

	/**
	 * OWL_FOOT is used to add RDF footer to Event messages comming through XML-RPC  
	 */	  
	
	  public static String OWL_FOOT = "\n</rdf:RDF>";
	



}