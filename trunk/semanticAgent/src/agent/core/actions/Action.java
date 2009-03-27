package agent.core.actions;

import java.util.HashMap;

import agent.core.swrlagent.SWRLAgent;

import jade.core.Agent;

/**
 * 
 * <br>
 * Interface for the low level actions<br>
 * Takes the Agent that will executes the action and the parameters of the
 * actions as parameters
 * 
 * @version 0.1
 * @author Julien Subercaze
 * 
 */
public interface Action {
    /**
     * Interface for low level actions
     * 
     * @param a
     * @param parameters
     */
    void run(SWRLAgent a, HashMap<String, String> parameters);
}
