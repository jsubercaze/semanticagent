package agent.core.actions;

import java.util.HashMap;

import agent.core.onto.ReasonerAgentInterface;
import agent.core.reasoner.ReasonerAgent;

import jade.core.Agent;
/** 
 * 
 * @author Julien Subercaze
 *
 */
public class ReceiveAction implements Action {

	

	@Override
	public void run(ReasonerAgent a, HashMap<String,String> parameters) {
		System.out.println("Starting "+this.getClass().getName());
		
	}

}
