package cos30018.assignment;

import java.util.Scanner;

import org.omg.SendingContext.RunTime;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;

public class CarAgent extends Agent {
	private Scanner scan;
	private String input;
	
	protected void setup() {
		System.out.println(getLocalName() + "... waiting for requests");
		System.out.println("Please state a time you would like to charge: ");
		scan = new Scanner(System.in);
		
		
		try {
			input = scan.nextLine();
			System.out.println(input);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
//		input = scan.nextLine();
//		
//		
//		if (input != null || input != "") {
//			
//			// Attach the start the Init Agent
//			
//			
//		// Message templete to listen only for messages matching the correct interaction protocol and performatives
//		MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
//				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
//		
//		//Add Behaviour which implements the responder role in a FIPA_Request interaction protocol
//		// The responder can either choose to agree to request or refuse request
//		addBehaviour(new AchieveREResponder(this, template){
//			protected ACLMessage prepareResponse(ACLMessage request)
//					throws NotUnderstoodException, RefuseException {
//				System.out.println(getLocalName() + ": Request recieved from " + request.getSender().getName()+ ". Query is: " + request.getContent());
//				
//				// Method to determine how to respond to request
//				if (checkAction())
//				{
//					System.out.println(getLocalName() + ": Agreeing to the request and responding with AGREE");
//					ACLMessage agree = request.createReply();
//					agree.setPerformative(ACLMessage.AGREE);
//					return agree;
//				} // end of if statement
//				else {
//					System.out.println("Agent " + getLocalName() + ": Refuse");
//					throw new RefuseException("check-failed");
//				}
//			}
//			
//			
//			protected ACLMessage prepareResultNotification(ACLMessage request,
//					ACLMessage response) throws FailureException {
//				if(performAction())
//				{
//					System.out.println(getLocalName() + ": Action successfully performed, informing initiator");
//					ACLMessage inform = request.createReply();
//					inform.setPerformative(ACLMessage.INFORM);
//					inform.setContent(input);
////					inform.setContent(String.valueOf(Math.random()));
//					return inform;
//				} // end of if statement
//				else {
//					// Action failed
//					System.out.println(getLocalName() + ": Action failed, informing initiator");
//					throw new FailureException("unexpected-error");
//				}
//			}
//		});
//	} // end of input Validation input if statement	
		
		
	} // end of setup
	

private boolean checkAction() {
	return true;
//	return (Math.random() > 0.2);
}

private boolean performAction() {
	return true;
}

}
