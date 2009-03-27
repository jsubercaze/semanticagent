package agent.core.onto;

import org.apache.log4j.Logger;

import agent.core.memory.SWRLMemory;


import com.hp.hpl.jena.ontology.OntModel;

import jade.core.Agent;

public class ReasonerAgentInterface extends Agent{

	public SWRLMemory mem;
	protected OntModel model;
	protected String ont;
	public Logger log ;

}
