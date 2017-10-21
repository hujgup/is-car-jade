package cos30018.assignment.utils;

import cos30018.assignment.logic.ListenRequest;
import cos30018.assignment.logic.NotifyAll;
import cos30018.assignment.logic.UpdateRequest;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.MessageTemplate;

public class FSMProcessing extends FSMBehaviour {
	
	private static String STATE_A = "A";
	private static String STATE_B = "B";
	private static String STATE_C = "C";
	private Agent agent;
	public MessageTemplate msgTemp;
	//private static String STATE_D = "D";
	
	public FSMProcessing(Agent a, MessageTemplate mt) {
		 agent = a;
		 msgTemp= mt;
		
	}
	
	public FSMBehaviour runFSM()
	{
		FSMBehaviour fsm = new FSMBehaviour(agent){
			@Override
			public int onEnd() {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};// end of onEnd function
		
		fsm.registerFirstState(new ListenRequest(agent, msgTemp), STATE_A);
		fsm.registerState(new UpdateRequest(), STATE_B);
		fsm.registerLastState(new NotifyAll(), STATE_C);
		
		fsm.registerDefaultTransition(STATE_A, STATE_B);
		fsm.registerDefaultTransition(STATE_B, STATE_C);
		fsm.registerTransition(STATE_C, STATE_A, 0);
		
		return fsm;
		
	}
	
	
	

}
