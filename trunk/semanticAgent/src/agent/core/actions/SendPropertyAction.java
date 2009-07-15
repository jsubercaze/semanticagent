package agent.core.actions;

import java.io.FileInputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import agent.core.communication.Message;
import agent.core.onto.ReasonerAgentInterface;
import agent.core.swrlagent.SWRLAgent;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;

/**
 * Send a property, its subject and object to another agent <br>
 * Receiver parameter name : <b>SendTo</b><br>
 * Name of the property : <b>PropertyName</b><br>
 * Subject : <b>PropertySubject</b><br>
 * Object : <b>PropertyObject</b><br>
 * 
 * @author Julien Subercaze
 * 
 */
public class SendPropertyAction implements Action {

    public synchronized void run(SWRLAgent A, HashMap<String, String> parameters) {
	try {

	    OntModel model = A.mem.getModel();

	    // ----------- Get the resource name from the parameters ---------
	    String receiver = parameters.get("SendTo");
	    String propertyName = parameters.get("PropertyName");
	    String propertySubject = parameters.get("PropertySubject");
	    String propertyObject = parameters.get("PropertyObject");

	    // ----------- Prepare the property, its subject and object
	    // ---------
	    Property prop = model.getProperty(propertyName);
	    Individual subject = model.getIndividual(propertySubject);
	    RDFNode object = model.getIndividual(propertyObject);
	    String ontology = subject.getURI();

	    A.send(Message.createInformPropertyMessage(A, receiver, prop,
		    subject, object, ontology));
	    A.log.info("Property Sent");
	} catch (Exception e) {

	    e.printStackTrace();
	}
	// agent.core.onto.Config.BASE

    }
}
