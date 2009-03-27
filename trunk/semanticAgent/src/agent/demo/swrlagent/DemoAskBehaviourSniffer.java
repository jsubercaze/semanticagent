package agent.demo.swrlagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

/**
 * In this demo, the agent AskAgent will ask AnswerAgent to send his behaviour. <br>
 * AskAgent then saves it behaviour in an external file <br>
 * <center><b>This demo shows the behaviour execution and extraction
 * process</b></center>
 * 
 * @author Julien Subercaze
 * 
 */
public class DemoAskBehaviourSniffer {
    public static void main(String args[]) {

	Runtime rt = Runtime.instance();

	System.out.println("Launching the agent container ...");
	Profile pMain = new ProfileImpl(null, 8888, null);
	AgentContainer ac = rt.createMainContainer(pMain);
	System.out.println("Starting simulation");
	AgentController rma;
	try {
	    rma = ac.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
	    rma.start();
	    AgentController sniffer = ac.createNewAgent("sniffer",
		    "jade.tools.sniffer.Sniffer", new Object[0]);
	    sniffer.start();
	    // Start the answer agent
	    Object[] argv = new Object[2];
	    argv[0] = "config/ReasonerAgent.properties";
	    argv[1] = "AskAgent";
	    AgentController reasoner = ac.createNewAgent((String) argv[1],
		    "agent.core.reasoner.ReasonerAgent", argv);
	    // Thread.sleep(10000);
	    Object[] argve = new Object[2];
	    argve[0] = "config/ReasonerAgent2.properties";
	    argve[1] = "AnswerAgent";
	    AgentController reasonere = ac.createNewAgent((String) argve[1],
		    "agent.core.reasoner.ReasonerAgent", argve);
	    getStringFromUser("Press any key to start the first agent");
	    reasonere.start();
	    Thread.sleep(1000);
	    getStringFromUser("Press any key to start the second agent");
	    reasoner.start();

	} catch (Exception e) {

	    e.printStackTrace();
	}
    }

    public static void getStringFromUser(String msg) {
	String str = "";
	try {
	    System.out.print(msg);
	    str = new BufferedReader(new InputStreamReader(System.in))
		    .readLine();
	} catch (IOException e) {
	    // TODO do exception handling
	}
    }
}
