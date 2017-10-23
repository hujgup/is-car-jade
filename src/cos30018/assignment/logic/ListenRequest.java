package cos30018.assignment.logic;

import java.util.ArrayList;

import cos30018.assignment.utils.RecievingMessage;
import cos30018.assignment.utils.SendingMessage;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.introspection.AddedBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ListenRequest extends CyclicBehaviour {
	Agent agent;
	
	public ListenRequest(Agent a) {
		agent = a;
		
	}

	@Override
	public void action() {
		CyclicBehaviour cylicBehavior = new CyclicBehaviour(agent) 
		{
			
			@Override
			public void action() {
				ACLMessage msg = agent.receive();
				if(msg !=null)
				{
					System.out.println("The Message is: " + msg);
					
				} else block();
				
			}
		};
		
	}
}
