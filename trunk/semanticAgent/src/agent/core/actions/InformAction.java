package agent.core.actions;

import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Resource;

import agent.core.communication.Message;
import agent.core.onto.ReasonerAgentInterface;
import agent.core.reasoner.ReasonerAgent;



import jade.core.Agent;
/**
 * 
 * @author Julien Subercaze
 *
 */
public class InformAction implements Action {

	public synchronized void run(ReasonerAgent a, final HashMap<String, String> parameters) {
		Resource r = null;
		a.log.info("Send message " + this.getClass().getName());
		a.send(Message.createInformMessage(a, "AnswerAgent", r));
		a.log.info("Starting " + this.getClass().getName());
	}

	
	

	

}
