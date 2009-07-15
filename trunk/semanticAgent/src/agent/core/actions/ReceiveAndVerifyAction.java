package agent.core.actions;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import agent.core.communication.Message;
import agent.core.onto.ReasonerAgentInterface;
import agent.core.swrlagent.SWRLAgent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author Julien Subercaze
 * 
 */
public class ReceiveAndVerifyAction implements Action {

    

    public synchronized void run(SWRLAgent a,
	    final HashMap<String, String> parameters) {
	System.err.println("Waiting for the message");
	ACLMessage msg = null;
	msg = a.blockingReceive();
	String content = msg.getContent();
	String ontology = msg.getOntology();
	a.log.info("!!!!! Content " + content);

	// Add the received
	OntModel model = a.mem.getModel();
	model.setStrictMode(false);
	//
	model.read(new ByteArrayInputStream(content.getBytes()), ontology);
	//model.read(new ByteArrayInputStream(content.getBytes()));
	/*
	 * model.setStrictMode(false); model.read(mindswappers); Property role =
	 * model.getProperty(foaf + "mbox"); Individual individual =
	 * model.getIndividual(mindswappers + "Christian.Halaschek");
	 * 
	 * RDFNode mbox = model.getIndividual("mailto:kolovski@cs.umd.edu");
	 * 
	 * individual.addProperty(role, mbox); // perform incremental
	 * consistency check
	 */

	model.prepare();

	// print time and validation report
	printIterator(model.validate().getReports(), "Validation Results");

    }

    public static void printIterator(Iterator<?> i, String header) {
	System.out.println(header);
	for (int c = 0; c < header.length(); c++)
	    System.out.print("=");
	System.out.println();

	if (i.hasNext()) {
	    while (i.hasNext())
		System.out.println(i.next());
	} else
	    System.out.println("<EMPTY>");

	System.out.println();
    }

   

}
