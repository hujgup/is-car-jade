package cos30018.assignment.logic;

import java.util.ArrayList;

import cos30018.assignment.utils.RecievingMessage;
import cos30018.assignment.utils.SendingMessage;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ListenRequest extends Behaviour {
	private Agent agent;
	private MessageTemplate msgTemp;
	private int[] requestArray;
	private Behaviour ListenForRequests;
	
	MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
				, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
	
	public ListenRequest(Agent a) {
		agent = a;
		s
		requestArray = new int[6];
		ListenForRequests = new TickerBehaviour(a, 3000) {
			
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				
			}
		};
			
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				
			}
		};
		
	}
}
