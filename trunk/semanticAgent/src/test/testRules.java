package test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;

public class testRules {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JenaOWLModel owlModel;
		//String ur = "http://owldl.com/ontologies/dl-safe.owl";
		String ur = "http://localhost/behaviour.owl";
		try {
			owlModel = ProtegeOWL.createJenaOWLModelFromURI(ur);		
			SWRLFactory gros = new SWRLFactory(owlModel);
			SWRLFactory factory = new SWRLFactory(owlModel);
			SWRLParser parser = new SWRLParser(owlModel);
			Collection rules = factory.getImps();
			Iterator it = rules.iterator();
			Vector<SWRLImp> ruleset = new Vector<SWRLImp>();
			while (it.hasNext()) {
				SWRLImp temp = (SWRLImp) it.next();
				ruleset.add(temp);
				//Reparse dans l'autre sens
				System.out.println(temp.getBrowserText());
				parser.parse(temp.getBrowserText());
				
			}
			//Hardcoding agent receiver
			String receiver = "AnswerAgent";
			//Send the message
			//A.send(Message.createBehaviourMessage(A, receiver, ruleset));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
