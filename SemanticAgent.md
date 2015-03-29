# Description #

SemanticAgent is a project of programming agents using Semantic Web
technologies. Unlike other agents dealing with semantic data, SemanticAgent language is based on SWRL and its knowledge base is in OWL. Therefore the agent language is perfectly adapated to manipulate knowledge from the knowledge base in order to take decision.





In English, a [book chapter on semantic agent](http://liris.cnrs.fr/Documents/Liris-4793.pdf) and a [conference article](http://liris.cnrs.fr/Documents/Liris-4495.pdf) are available.

For the french, a detailed description is given in my [PhD](http://theses.insa-lyon.fr/publication/2010ISAL0104/these.pdf)

## Running the demo ##

During the demo, two agents named askAgent and answerAgent will communicate and answerAgent will send its behaviour composed of SWRL rules to askAgent.

The goal of the demo is to show how the agent interpreter is working. Each agent are of type: agent.core.swrlagent.SWRLAgent. The behaviours of the agents are **only** described in swrl:

**memory\_init/answerBehaviour.swrl** memory\_init/askBehaviour.swrl

Behaviours are described are transition rules in an extended finite state machine.

After the demo runs, answerAgent has received the behaviour of askAgent and stored it in memory\_init/answerBehaviourAfter.swrl

### Demo with Sniffer ###

If you start the demo with the sniffer, before pressing a key to start the first agent, use the sniffer agent to sniff communications between DF,askAgent and answerAgent. Therefore you can observe the message containing the behaviour.