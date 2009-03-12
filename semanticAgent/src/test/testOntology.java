package test;

import java.io.FileInputStream;
import java.util.HashMap;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

public class testOntology {
	private static OntModel model;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Load ontology

		
		model = ModelFactory.createOntologyModel(
				PelletReasonerFactory.THE_SPEC, null);
		try {
			model.read(new FileInputStream("memory_init/answerBehaviour.owl"),
					null);
			model.read(new FileInputStream("memory_init/agentModel.owl"), null);
			model
					.read("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl");
			
			model.prepare();
			// Load the variable from the Model
			Individual CurrentState = model
					.getIndividual("http://liris.cnrs.fr/julien.subercaze/ontologies/agentModel.owl#CurrentState");
			Individual NextState = model
					.getIndividual("http://liris.cnrs.fr/julien.subercaze/ontologies/agentModel.owl#TheNextState");
			Individual ActionList = model
					.getIndividual("http://liris.cnrs.fr/julien.subercaze/ontologies/agentModel.owl#AL");
			OntProperty content = model
					.getOntProperty("http://www.co-ode.org/ontologies/list.owl#hasContents");
			OntProperty next = model
					.getOntProperty("http://www.co-ode.org/ontologies/list.owl#hasNext");

			ObjectProperty stateValue = model
					.getObjectProperty("http://liris.cnrs.fr/julien.subercaze/ontologies/agentModel.owl#hasStateValue");
			ObjectProperty actions = model
					.getObjectProperty("http://liris.cnrs.fr/julien.subercaze/ontologies/agentModel.owl#hasList");
			
			printPropertyValues(CurrentState, stateValue);
			printPropertyValues(NextState, stateValue);
			printIterator(model.listAllOntProperties());
			Resource r1 = (Resource) ActionList.getPropertyValue(actions);
			Individual tmp = model
					.getIndividual(r1.getNameSpace()
							+ r1.getLocalName());
			System.out.println(tmp.getNameSpace()+tmp.getLocalName());
			// printPropertyValues(ActionList, content);
			printPropertyValues(ActionList, actions);
			ExecuteActions(tmp);
			Resource r = (Resource) NextState.getPropertyValue(stateValue);

			// Iterate until end state is reached
			while (r.getLocalName().compareTo("END") != 0) {
				// Get nextstate value
				Resource temp = (Resource) NextState
						.getPropertyValue(stateValue);
				// Remove currentstate value
				CurrentState.removeProperty(stateValue, CurrentState
						.getPropertyValue(stateValue));
				// Update currentstate
				CurrentState.addProperty(stateValue, temp);
				r = (Resource) NextState.getPropertyValue(stateValue);
				System.out
						.println("http://liris.cnrs.fr/julien.subercaze/ontologies/agentModel.owl#"
								+ r.getLocalName());
				// Get the actions
				r1 = (Resource) ActionList.getPropertyValue(actions);
				tmp = model
						.getIndividual("http://liris.cnrs.fr/julien.subercaze/ontologies/agentModel.owl#"
								+ r1.getLocalName());
				// ExecuteActions(tmp);
			}
			printPropertyValues(CurrentState, stateValue);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	/**
	 * This method executes the actions contained in the list
	 * 
	 * @param list
	 */
	private static void ExecuteActions(Individual list) {
		System.out.println(list.getNameSpace()+list.getLocalName());
		// While the end of the list has not been reached, execute the actions
		OntProperty content = model.getOntProperty("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasContents");
		OntProperty next = model
				.getOntProperty("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasNext");
		//System.out.println(list.hasProperty(content));
		Individual temp = list;
		Resource r;
		do {
			Resource action  = ((Resource) temp.getPropertyValue(content));
			System.out.println(action.getLocalName());
			// Retrieve the parameter of the action
			Individual tmp = model.getIndividual(action.getNameSpace()+action.getLocalName());
			//HashMap<String, String> parameters = retrieveParameters(tmp);;
			// Execute Action
			//executeAction(actionName, parameters);
			// Get the next
			r = (Resource) temp.getPropertyValue(next);
			System.out.println(r.getNameSpace()+r.getLocalName());
			// System.out.println(ont + "#"+r.getLocalName());
			temp = model.getIndividual(r.getNameSpace() + r.getLocalName());

		} while (r.getLocalName().compareTo("finListe") != 0);

	}

	/*
	 * Debugging method (From Pellet examples)
	 * 
	 * @param ind
	 * 
	 * @param prop
	 */
	private static void printPropertyValues(Individual ind, Property prop) {
		System.out.print(ind.getLocalName() + " has " + prop.getLocalName()
				+ "(s): ");
		printIterator(ind.listPropertyValues(prop));
	}

	/**
	 * Debugging method (From Pellet examples)
	 * 
	 * @param ind
	 * @param prop
	 */
	private static void printCPropertyValues(OntClass ind, Property prop) {
		System.out.print(ind.getLocalName() + " has " + prop.getLocalName()
				+ "(s): ");
		printIterator(ind.listPropertyValues(prop));
	}

	/**
	 * Debugging method (From Pellet examples)
	 * 
	 * @param i
	 */
	private static void printIterator(ExtendedIterator i) {
		if (!i.hasNext()) {
			System.out.print("none");
		} else {
			while (i.hasNext()) {
				Resource val = (Resource) i.next();
				System.out.print(val.getNameSpace());
				System.out.print(val.getLocalName());

				if (i.hasNext())
					System.out.print(", ");
			}
		}
		System.out.println();
	}

	/**
	 * Debugging method (From Pellet examples)
	 * 
	 * @param cls
	 */
	public static void printInstances(OntClass cls) {
		System.out.print(cls.getLocalName() + " instances: ");
		printIterator(cls.listInstances());
	}
}
