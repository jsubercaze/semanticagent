package test;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
public class Tester {
	
	public static void main (String args[]){
		
		Runtime rt = Runtime.instance();
		// Exit the JVM when there are no more containers around
		//rt.setCloseVM(true);
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
			argv[0] = "config/ReasonerAgent.properties";
			argv[1] = "ReasonerAgent";
			AgentController reasoner = ac.createNewAgent((String)argv[1], "agent.core.reasoner.ReasonerAgent",argv);
			//Thread.sleep(10000);
			Object [] argve = new Object[2];
			argve[0] = "config/ReasonerAgent2.properties";
			argve[1] = "AnswerAgent";
			AgentController reasonere = ac.createNewAgent((String)argve[1], "agent.core.reasoner.ReasonerAgent",argve);	
			Thread.sleep(5000);
			reasonere.start();
			Thread.sleep(10000);
			reasoner.start();
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
