package agent.demo.swrlagent;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

/** In this demo, the agent AskAgent will ask AnswerAgent to send his behaviour. 
 * 	<br>AskAgent then saves it behaviour in an external file
 * 	<br><center><b>This demo shows the behaviour execution and extraction process</b></center>
 * 
 * @author Julien Subercaze
 *
 */
public class DemoAskBehaviour {
	public static void main (String args[]){
		
		Runtime rt = Runtime.instance();
	
		System.out.println("Launching the agent container ...");
		Profile pMain = new ProfileImpl(null, 8888, null);
		AgentContainer ac = rt.createMainContainer(pMain);
		System.out.println("Starting simulation");
		AgentController rma;
		try {
			rma = ac.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();
			AgentController sniffer = ac.createNewAgent("sniffer","jade.tools.sniffer.Sniffer",new Object[0]);
			sniffer.start();
			//Start the answer agent			
			Object [] argv = new Object[2];
			argv[0] = "config/AskAgent.properties";
			argv[1] = "AskAgent";
			AgentController reasoner = ac.createNewAgent((String)argv[1], "agent.core.swrlagent.SWRLAgent",argv);
			//Thread.sleep(10000);
			Object [] argve = new Object[2];
			argve[0] = "config/AnswerAgent.properties";
			argve[1] = "AnswerAgent";
			AgentController reasonere = ac.createNewAgent((String)argve[1], "agent.core.swrlagent.SWRLAgent",argve);	
			//Thread.sleep(10000);
			reasonere.start();
			Thread.sleep(1000);
			reasoner.start();
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
