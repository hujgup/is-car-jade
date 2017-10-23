package cos30018.assignment.utils;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.introspection.AddedBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class RecievingMessage extends AchieveREResponder {
	
	private List <String> requests = new ArrayList();
	
	
	public RecievingMessage(Agent a, MessageTemplate mt) 
	{
		super(a, mt);
		Agent agent = a;
		MessageTemplate template = mt;
		
		
	}
	
	protected ACLMessage prepareResponse(ACLMessage request)
			throws NotUnderstoodException, RefuseException {
		
		if(request.getContent() != null)
		{
			requests.add(request.getContent());
		}
		System.out.println("The size of the array is :" + requests.size());
		
		System.out.println("Request Recieved from"+ request.getSender().getName() + " the query is: "+ request.getContent());
		
		// Listen for requests
		// cyclicBehaviour with a if else block();
		if(checkAction())
		{
			System.out.println("Sending agree");
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
			
		} else
		{
			System.out.println("Refused");
			throw new RefuseException("failed");
		}
		
	}
	
	protected ACLMessage prepareResultNotification(ACLMessage request,
			ACLMessage response) throws FailureException {
		
		if(checkAction()) {
			System.out.println("Action successfully performed, informing initiator");
			ACLMessage inform = request.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			inform.setContent("That number was not in the timetable");
				System.out.println("The behaviour is terminating...");
			return inform;
		} else {
			// Action failed
			System.out.println("Action failed, informing initiator");
			
			throw new FailureException("unexpected-error");
			
		}
		
	}
	

	
	// method that gets all the message: use a delayed queue
	// https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/DelayQueue.html
	// talk to Jake
	
	private boolean checkAction() {
		return true;
	}
}
