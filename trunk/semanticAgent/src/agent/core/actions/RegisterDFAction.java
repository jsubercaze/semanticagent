package agent.core.actions;

import java.util.HashMap;

import agent.core.communication.Message;
import agent.core.onto.ReasonerAgentInterface;
import agent.core.reasoner.ReasonerAgent;
import jade.core.Agent;
/** Specific JADE action - Register to Yellow Pages (Directory facilitator)
 * 
 * @author Julien Subercaze
 *
 */
public class RegisterDFAction implements Action {

	@Override
	/** Register the agent to the directory finder
	 * 
	 */
	public synchronized void run(ReasonerAgent A, HashMap<String,String> parameters) {
		System.out.println("Starting"+this.getClass().getName());
		//Register to the DF
		System.out.println("Send message to :"+parameters.get("SendTo"));
		Message.register(A, A.getName());
	}
	

	

}
