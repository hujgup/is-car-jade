package cos30018.assignment.logic;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import cos30018.assignment.utils.RecievingMessage;

@SuppressWarnings("serial")
public class SchedulingAgent extends Agent {
	@Override
	public void setup() {
		System.out.println("... waiting for request");
		MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
						, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		
		addBehaviour(new RecievingMessage(this, template));
	}
}
