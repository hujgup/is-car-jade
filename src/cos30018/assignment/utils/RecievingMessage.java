package cos30018.assignment.utils;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class RecievingMessage extends AchieveREResponder {
	
	
	public RecievingMessage(Agent a, MessageTemplate mt) {
		super(a, mt);
		Agent agent = a;
		MessageTemplate template = mt;
	}

	protected ACLMessage prepareResponse(ACLMessage request)
			throws NotUnderstoodException, RefuseException {
		System.out.println("Request Recieved from"+ request.getSender().getName() + " the query is: "+ request.getContent());
		
		if(checkAction())
		{
			System.out.println("sending agree");
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
		}else {
			System.out.println("Refused");
			throw new RefuseException("failed");
		}
		
	}
	
	protected ACLMessage prepareResultNotification(ACLMessage request,
			ACLMessage response) throws FailureException {
		
		
		if(performAction()) {
			System.out.println("Action successfully performed, informing initiator");
			ACLMessage inform = request.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			inform.setContent("That number was not in the timetable");
			return inform;
		} else {
			// Action failed
			System.out.println("Action failed, informing initiator");
			throw new FailureException("unexpected-error");
		}

	}
	
	private boolean checkAction() {
		return true;
	}
	
	private boolean performAction() {
		return true;
	}
}
