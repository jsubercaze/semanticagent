#!/bin/bash
#Modified from agentOWL.sourceforge.net
#local lib ! EDIT this line !
CP=/usr/local/j2sdk/jre/lib/rt.jar
for i in ./lib/*.jar
	  do CP=$CP:$i
done
CP=./dist/lib/AgentOWL.jar:$CP

java -cp $CP  agent.demo.swrlagent.verification.DemoAskVerification &
    

