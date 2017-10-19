package cos30018.assignment.logic;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import cos30018.assignment.utils.FSMProcessing;
import cos30018.assignment.utils.RecievingMessage;

@SuppressWarnings("serial")
public class SchedulingAgent extends Agent {
	private static String STATE_A= "A";
	private static String STATE_B= "B";
	private static String STATE_C= "C";
	@Override
	public void setup() {
		
		System.out.println("... waiting for request");
		MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
						, MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		
		
//		addBehaviour(new RecievingMessage(this, template));
		FSMBehaviour fsm = new FSMBehaviour(this){
			@Override
			public int onEnd() {
				// TODO Auto-generated method stub
				return super.onEnd();
			}
		};// end of onEnd
		
		fsm.registerFirstState(new RecievingMessage(this, template), STATE_A);
		fsm.registerState(new UpdateRequest(), STATE_B);
		fsm.registerLastState(new NotifyAll(), STATE_C);
		
		fsm.registerDefaultTransition(STATE_A, STATE_B);
		fsm.registerDefaultTransition(STATE_B, STATE_C);
		fsm.registerTransition(STATE_C, STATE_A, 1);
		
		addBehaviour(fsm);
		
		
	}
}
