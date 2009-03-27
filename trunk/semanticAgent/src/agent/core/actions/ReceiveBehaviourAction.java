package agent.core.actions;

import jade.lang.acl.ACLMessage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.repository.util.RepositoryFileManager;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;

import agent.core.communication.Message;
import agent.core.onto.ReasonerAgentInterface;
import agent.core.swrlagent.SWRLAgent;

/**
 * Implementation of Behaviour Reception - Receive the rules in the message,
 * parse and add in the Knowledge Base<br>
 * <br>
 * Uses Protege Model to parse Rules, model is then converted as a Jena Model
 * and replace the former Jena model. The protege-owl.jar is slightly modified to avoid import bugs.
 * 
 * @author Julien Subercaze
 * 
 */
public class ReceiveBehaviourAction implements Action {

    public synchronized void run(SWRLAgent a,
	    HashMap<String, String> parameters) {
	a.log.info("Start the receive");
	// Receive the message of type Inform
	ACLMessage msg = null;
	msg = a.blockingReceive();
	// Handle Content
	String content = msg.getContent();
	a.log.info("Content " + content);
	String[] ruleset = content.split(";");
	JenaOWLModel owlModel;
	try {
	    owlModel = ProtegeOWL
		    .createJenaOWLModelFromInputStream(new FileInputStream(
			    a.mem.config.INDIVIDUALS_FILE));
	    owlModel
		    .getNamespaceManager()
		    .setPrefix(
			    new URI(
				    a.mem.config.BASE),
			    "dl-safe");
	    //DEBUG System.out.println(owlModel.getNamespaceManager().getNamespaceForPrefix("dl-safe"));
	    Set<String> gros = owlModel.getAllImports();
	    Iterator it = gros.iterator();

	   /* while (it.hasNext()) {
		System.out.println(it.next().toString());
	    }*/
	    ImportHelper importHelper = new ImportHelper(
		    (JenaOWLModel) owlModel);
	    URI importUri = URIUtilities
		    .createURI(a.mem.config.BASE);
	    importHelper.addImport(importUri);
	    importHelper.importOntologies(false);
	    SWRLFactory factory = new SWRLFactory(owlModel);
	    
	    for (int i = 0; i < ruleset.length; i++) {
		String rules = ruleset[i].trim();
		String[] rule = rules.split("%", 2);
		a.log.info("Parsing : " + rule[0].trim() + " : "
			+ rule[1].trim());
		factory.createImp(rule[0], rule[1]);
	    }
	    // TODO Remove Test the bouzin
	    a.mem.setModel(owlModel.getOntModel());
	    a.log.info("Saving the memory of the agent");
	    a.mem.memory2owl();

	    // Send ack
	    a.send(Message.createInformMessage(a, msg.getSender().getName(),
		    null));
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
