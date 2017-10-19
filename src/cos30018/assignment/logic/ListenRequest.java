package cos30018.assignment.logic;

import java.util.ArrayList;

import cos30018.assignment.utils.RecievingMessage;
import cos30018.assignment.utils.SendingMessage;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ListenRequest extends OneShotBehaviour {
	private Agent agent;
	private MessageTemplate msgTemp;
	
	MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
				, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
	
	public ListenRequest(Agent a, MessageTemplate mt) {
		agent = a;
		msgTemp = mt;
	}
		
		@Override
		public void action() {
			// Add timetable 
			System.out.println("This is listening to the requests");
			System.out.println("Adding Requests...");
			
			RecievingMessage readingMsg = new RecievingMessage(agent, msgTemp);
			
			
		}
		
		public void getTimes()
		{
			
		}
}
