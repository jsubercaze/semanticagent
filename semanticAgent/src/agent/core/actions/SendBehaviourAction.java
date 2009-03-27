package agent.core.actions;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import agent.core.communication.Message;
import agent.core.onto.ReasonerAgentInterface;
import agent.core.swrlagent.SWRLAgent;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;

/** This low level action extracts the rules of the behaviour and sends it to the agent passed as parameter
 *  <br>Receiver parameter name : <b>SendTo</b>
 * 
 * @author Julien Subercaze
 * 
 */
public class SendBehaviourAction implements Action {
    
    public synchronized void run(SWRLAgent A,
	    HashMap<String, String> parameters) {
	// Extract the behaviours (Collection of Imps)
	JenaOWLModel owlModel;
	try {
	    owlModel = ProtegeOWL
		    .createJenaOWLModelFromInputStream(new FileInputStream(
			    A.mem.config.INDIVIDUALS_FILE));
	    SWRLFactory factory = new SWRLFactory(owlModel);
	    Collection<SWRLImp> rules = factory.getImps();
	    Iterator<SWRLImp> it = rules.iterator();
	    Vector<SWRLImp> ruleset = new Vector<SWRLImp>();
	    JenaOWLModel owlModel2 = ProtegeOWL.createJenaOWLModel();
	     while (it.hasNext()) {
		SWRLImp temp = it.next();
		ruleset.add(temp);
		// Reparse dans l'autre sens
		A.log.info("Adding Rule to message" + temp.getBrowserText());

	    }
	    // Hardcoding agent receiver
	    String receiver = "AnswerAgent";
	    // Send the message
	    A.send(Message.createBehaviourMessage(A, receiver, ruleset));
	} catch (Exception e) {

	    e.printStackTrace();
	}
	// agent.core.onto.Config.BASE

    }
}
