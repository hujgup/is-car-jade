package cos30018.assignment.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class SendingMessage extends AchieveREInitiator {
	private int numOfArguments;
	
	public SendingMessage(Agent a, ACLMessage m, Object[] args) {
		super(a, m);
		Agent agent = a;
		ACLMessage msg = m;
		numOfArguments = args.length;
	}
	


	protected void handleAgree(ACLMessage agree) {
		System.out.println(agree.getSender().getName() + " has agreed");
	}
	
	
	protected void handleInform(ACLMessage inform) {
		System.out.println("Successfully completed action");
		System.out.println("The request time was: " + inform.getContent());
	}
	
	
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("refused the action");
		numOfArguments--;
	}
	
	
	protected void handleFailure(ACLMessage failure) {
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver (receiver does not exist)
			System.out.println("Responder does not exist");
		} else {
			System.out.println(failure.getSender().getName() + " failed to perform the requested action");
		}

	}
	
	
	protected void handleAllResultNotifications(Vector notifications) {
		if (notifications.size() < numOfArguments) {
			// Some responder didn't reply within the specified timeout
			System.out.println(
					"Timeout expired: missing " + (numOfArguments - notifications.size()) + " responses");
		} else {
			System.out.println("Received notifications about every responder");
			
		}

	}

}
