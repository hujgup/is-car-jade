package cos30018.assignment;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.domain.FIPANames;

import java.util.Date;
import java.util.Vector;


public class RequestInitAgent extends Agent {
	private int numOfCars;
	private int request[];
	
	protected void setup() {
		// Read the names of the agents
		Object[] args = getArguments();
		if(args != null && args.length > 0)
		{
			numOfCars = args.length;
			System.out.println(getLocalName() +": There is "+args.length + " agents responding" );
			
			// ACLMessage Request 
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			// Adding the receivers
			for(int i=0; i < args.length; ++i)
			{
				msg.addReceiver(new AID((String) args[i], AID.ISLOCALNAME));
			}
			
			// set protocol the FIPA protocol
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			//Specify the reply deadline (10 seconds)
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			//setting content of the message (msg) variable
			msg.setContent("What time would you like to request?");

			
			// Intailse and add the behaviour to the agent
			addBehaviour(new AchieveREInitiator(this, msg){
				
				
				protected void handleAgree(ACLMessage agree) {
					System.out.println(getLocalName() +": " + agree.getSender().getName()+ " has agreed");
				}
				
				
				protected void handleInform(ACLMessage inform) {
					System.out.println(getLocalName() +": " + inform.getSender().getName() + " successfully performed the requested action");
					System.out.println(getLocalName() +": "+ inform.getSender().getName() + " has requested a new time of: "+ inform.getContent());
					
				}
				
				
				protected void handleRefuse(ACLMessage refuse) {
					System.out.println(getLocalName() +": " + refuse.getSender().getName() + " refused to perform the action");
				}
				
				protected void handleFailure(ACLMessage failure) {
					if(failure.getSender().equals(myAgent.getAMS()))
					{
						System.out.println(getLocalName() + ": Responder doesn't exist");
					} else {
						System.out.println(getLocalName() + ": " + failure.getSender().getName() + " failed to perform the requested action");
					}
				}
				
				
				protected void handleAllResultNotifications(
						Vector resultNotifications) {
					if(resultNotifications.size() < numOfCars)
					{
						System.out.println(getLocalName() + ": " + "Timeout expired: missing" + (numOfCars - resultNotifications.size()) + " responses");
						
					} else {
						System.out.println(getLocalName() + ": " + "Received notifications about every responder");
					}
				}
				
			});
			
		} // end of if statement
		else {
			System.out.println(getLocalName() + ": You have not specified any arguments");
		}
	}

}// end of project





















