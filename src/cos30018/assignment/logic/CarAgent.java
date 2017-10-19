package cos30018.assignment.logic;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;

import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import cos30018.assignment.utils.FSMProcessing;
import cos30018.assignment.utils.SendingMessage;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	private int numOfArguments;
	private String input = null;
	private Scanner scan;
	
	 public void setup() {
		Object[] args = getArguments();

		//System.out.println(car.getChargeCapacity());
		// testing the FSMProcessing
		
		numOfArguments = args.length;
//		System.out.println(getLocalName()+ ": What time would you like?");
//		scan = new Scanner(System.in);
//		input = scan.nextLine();
		
//		if(input != null || input != "")
		if(numOfArguments > 0){
				// Car Object that is intialised at the bottom of this code
				//Car car = createCarObject(50.0,100, 20, LocalTime.parse("10:00"), LocalTime.parse("12:00"));
				//System.out.println(getLocalName() +": has a charge capacity of "+car.getChargeCapacity());
				
				System.out.println("Requesting dummy-action to" + numOfArguments + " responders");
				
				//Create a Request Message
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				
				for(int i=0; i< args.length; i++)
				{
					// adding the master as a receiver 
					msg.addReceiver(new AID((String) args[i], AID.ISLOCALNAME));
				}
				
				// setting the protocol and the content of the message to what the the user inputted
				msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
				msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
				// set the message content that the car agent will send change to input
				msg.setContent(args[1].toString());
				
				addBehaviour(new SendingMessage(this, msg, args));
		
		
		} else {
			System.out.println("failed");
		}
	}
}
