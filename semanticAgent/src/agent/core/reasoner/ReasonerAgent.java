package agent.core.reasoner;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import agent.core.actions.Action;
import agent.core.memory.SWRLMemory;
import agent.core.onto.ReasonerAgentInterface;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

/**
 * Agent Reasoner Load the default reasoner and start the agent behaviour
 * 
 * @author Julien Subercaze
 * @version 0.1
 */
public class ReasonerAgent extends ReasonerAgentInterface {

	/**
	 * Default Constructor
	 * 
	 */
	public ReasonerAgent() {

	}

	/**
	 * JADE method for agent setup - Overriden to load the SWRL Memory and to
	 * create the Ontology model
	 * 
	 */
	protected void setup() {

		log = Logger.getLogger(this.getClass().getName());
		Object[] args = getArguments();
		String memo = (String) args[0];
		String name = (String) args[1];
		log.info("Starting Agent " + name);
		this.mem = new SWRLMemory(memo, name);
		this.model = this.mem.getModel();
		log.info(mem.config.BASE);
		BehaviourReasoning hrmBehaviour = new BehaviourReasoning();
		addBehaviour(hrmBehaviour);

	}

	/**
	 * This Class is the heart of the system, it implements the engine of the
	 * agent as a Jade SimpleBehaviour
	 * 
	 * @author jsubercaze
	 * 
	 */
	class BehaviourReasoning extends SimpleBehaviour {
		private boolean done = false;

		@Override
		/*
		 *  Called when the behaviour is started. Prepare the model and the
		 * rules firing
		 */
		public void action() {
			// Load the behaviour ontology
			log.info(mem.config.BASE);
			log.info(mem.config.INDIVIDUALS_FILE);
			String local = mem.config.INDIVIDUALS_FILE;
			ont = mem.config.BASE;
			model = ModelFactory.createOntologyModel(
					PelletReasonerFactory.THE_SPEC, null);
			try {
				model.read(mem.config.BASE);
				model.read(new FileInputStream(local), null);
				// Fire the rules
				// printIndividuals();
				chainRules();
				log.info("End");
				// Try to extract behaviour:

				// answer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				done = true;
			}

			done = true;
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return done;
		}

	}

	/**
	 * Heart of the <b>Behaviour</b>, this method fires the rules in chain to go
	 * through the automata and start the related actions. Stops when special
	 * state "END" is reached.
	 * 
	 * 
	 */
	public void chainRules() {
		Individual CurrentState = model
				.getIndividual(mem.config.BASE+"#CurrentState");
		Individual NextState = model
				.getIndividual(mem.config.BASE+"#TheNextState");
		Individual ActionList = model
				.getIndividual(mem.config.BASE+"#AL");
		OntProperty content = model.getOntProperty("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasContents");
		OntProperty next = model
				.getOntProperty("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasNext");

		ObjectProperty stateValue = model
				.getObjectProperty(mem.config.BASE+"#hasStateValue");
		ObjectProperty actions = model
				.getObjectProperty(mem.config.BASE+"#hasList");
		model.prepare();
		printPropertyValues(CurrentState, stateValue);
		printPropertyValues(NextState, stateValue);
		printIterator(model.listAllOntProperties());
		Resource r1 = (Resource) ActionList.getPropertyValue(actions);
		Individual tmp = model
				.getIndividual(r1.getNameSpace()+ r1.getLocalName());
		ExecuteActions(tmp);
		Resource r = (Resource) NextState.getPropertyValue(stateValue);

		// Iterate until end state is reached
		while (r.getLocalName().compareTo("END") != 0) {
			// Get nextstate value
			Resource temp = (Resource) NextState.getPropertyValue(stateValue);
			// Remove currentstate value
			CurrentState.removeProperty(stateValue, CurrentState
					.getPropertyValue(stateValue));
			// Update currentstate
			CurrentState.addProperty(stateValue, temp);
			r = (Resource) NextState.getPropertyValue(stateValue);
			System.out
					.println(r.getNameSpace()
							+ r.getLocalName());
			// Get the actions
			r1 = (Resource) ActionList.getPropertyValue(actions);
			tmp = model
					.getIndividual(r1.getNameSpace()
							+ r1.getLocalName());
			printPropertyValues(CurrentState, stateValue);
			ExecuteActions(tmp);
		}
		printPropertyValues(CurrentState, stateValue);

		/*
		 * Individual CurrentState =model.getIndividual(
		 * mem.config.BASE+"#CurrentState"
		 * ); Individual NextState =model.getIndividual(
		 * mem.config.BASE+"#TheNextState"
		 * ); Individual List =model.getIndividual(
		 * mem.config.BASE+"#MyList"
		 * ); Individual ActionList =model.getIndividual(
		 * mem.config.BASE+"#AL"
		 * ); OntProperty content = model.getOntProperty(
		 * "http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasContents"
		 * ); OntProperty next = model.getOntProperty(
		 * "http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasNext"
		 * );
		 * 
		 * ObjectProperty stateValue =model.getObjectProperty(
		 * mem.config.BASE+"#hasStateValue"
		 * ); ObjectProperty actions =model.getObjectProperty(
		 * mem.config.BASE+"#hasList"
		 * ); model.prepare(); printPropertyValues(CurrentState, stateValue);
		 * printPropertyValues(List, content); printPropertyValues(List, next);
		 * ExecuteActions(List); Resource r = (Resource)
		 * NextState.getPropertyValue(stateValue); Resource r1; Individual tmp;
		 * // Iterate until end state is reached while
		 * (r.getLocalName().compareTo("END") != 0) { // Get nextstate value
		 * Resource temp = (Resource) NextState.getPropertyValue(stateValue); //
		 * Remove currentstate value CurrentState.removeProperty(stateValue,
		 * CurrentState .getPropertyValue(stateValue)); // Update currentstate
		 * CurrentState.addProperty(stateValue, temp); r = (Resource)
		 * NextState.getPropertyValue(stateValue); // Get the actions r1 =
		 * (Resource) ActionList.getPropertyValue(actions); tmp =
		 * model.getIndividual
		 * (mem.config.BASE+"#" +
		 * r1.getLocalName()); ExecuteActions(tmp); }
		 * printPropertyValues(CurrentState, stateValue);
		 */
	}

	

	/**
	 * This method executes the actions contained in the list
	 * 
	 * @param list
	 */
	private void ExecuteActions(Individual list) {
		log.info("Executing actions from "+list.getNameSpace()+list.getLocalName());
		// While the end of the list has not been reached, execute the actions
		OntProperty content = model
				.getOntProperty("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasContents");
		OntProperty next = model
				.getOntProperty("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl#hasNext");

		Individual temp = list;
		Resource r;
		do {
			Resource action  = ((Resource) temp.getPropertyValue(content));
			
			log.info(action.getLocalName());
			// Retrieve the parameter of the action
			Individual tmp = model.getIndividual(action.getNameSpace()+action.getLocalName());
			HashMap<String, String> parameters = retrieveParameters(tmp);
			// Execute Action
			executeAction(action.getLocalName(), parameters);
			// Get the next
			r = (Resource) temp.getPropertyValue(next);
			// System.out.println(ont + "#"+r.getLocalName());
			temp = model.getIndividual(r.getNameSpace() + r.getLocalName());

		} while (r.getLocalName().compareTo("finListe") != 0);

	}

	/**
	 * Execute a unique low level action using Java Reflection. Hashmap
	 * parameters contains the parameters as <name,value>, both are
	 * java.lang.String
	 * 
	 * @param actionName
	 * @param parameters
	 */
	private void executeAction(String actionName,
			HashMap<String, String> parameters) {
		// Test réflection
		Class cl;
		try {
			cl = Class.forName("agent.core.actions." + actionName + "Action");
			Action j = (Action) cl.newInstance();
			Agent A = this;
			j.run(this, parameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Retrieve the parameters of Individual ind and store in a
	 * HashMap<name,value>
	 * 
	 * @param ind
	 * @return
	 */
	private HashMap<String, String> retrieveParameters(Individual ind) {
		System.out.println("Printing parameters for "+ind.getLocalName());
		// Create the hashmap
		HashMap<String, String> result = new HashMap<String, String>();
		// Initialize the properties
		OntProperty parameterName = model
				.getObjectProperty(mem.config.BASE+"#hasParameterName");
		OntProperty parameterValue = model
				.getObjectProperty(mem.config.BASE+"#hasParameterValue");
		// Create an iterator with all the names
		ExtendedIterator i = ind.listPropertyValues(parameterName);
		while (i.hasNext()) {
			Resource val = (Resource) i.next();
			// Get the individual with this value
			// System.out.println(val.getLocalName());
			Individual tempIndiv = model.getIndividual(val.getNameSpace()
					+ val.getLocalName());
			// Get the value of the parameter
			Resource r = (Resource) tempIndiv.getPropertyValue(parameterValue);
			// System.out.println(r.getLocalName());
			result.put(val.getLocalName(), r.getLocalName());
		}
		return result;

	}
	/*
	 * Debugging method (From Pellet examples)
	 * 
	 * @param ind
	 * 
	 * @param prop
	 */
	private void printPropertyValues(Individual ind, Property prop) {
		log.info(ind.getLocalName() + " has " + prop.getLocalName() + "(s): ");
		printIterator(ind.listPropertyValues(prop));
	}

	/**
	 * Debugging method (From Pellet examples)
	 * 
	 * @param ind
	 * @param prop
	 */
	private void printCPropertyValues(OntClass ind, Property prop) {
		log.info(ind.getLocalName() + " has " + prop.getLocalName() + "(s): ");
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

	public void printIndividuals() {

		printIterator(model.listIndividuals());
	}

	/**
	 * Debugging method (From Pellet examples)
	 * 
	 * @param cls
	 */
	public void printInstances(OntClass cls) {
		System.out.print(cls.getLocalName() + " instances: ");
		printIterator(cls.listInstances());
	}

	private void printIteratore(StmtIterator listProperties) {
		while(listProperties.hasNext()){
			Statement stmt = listProperties.nextStatement();
			System.out.println(stmt.toString());
		}
	}
	
}
