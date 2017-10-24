package cos30018.assignment.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import cos30018.assignment.utils.RecievingMessage;
import cos30018.assignment.utils.handleCarCharge;

@SuppressWarnings("serial")
public class SchedulingAgent extends Agent {
	private static String State_A = "A";
	private static String State_B = "B";
	private static String State_C = "C";
	private handleCarCharge carCharge = new handleCarCharge();
	private List<String> requests= new ArrayList();
	private int[] requestTimes;
	
	@Override
	protected void setup() {
		
		FSMBehaviour fsm = new FSMBehaviour(this){
			@Override
			public int onEnd() {
				// TODO Auto-generated method stub
				return super.onEnd();
			}
		};
		
		fsm.registerFirstState(new Listen(this, 4000), State_A);
		fsm.registerState(new Update(), State_B);
		fsm.registerLastState(new NotifyAll(), State_C);
		
		fsm.registerDefaultTransition(State_A, State_B);
		fsm.registerDefaultTransition(State_B, State_C);
		
		addBehaviour(fsm);
		
		
	}
	
	
	
	private class Listen extends TickerBehaviour
	{
		
		
		public Listen(Agent a, long period) {
			super(a, period);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onTick() {
			// TODO Auto-generated method stub
		ACLMessage msg = receive();
	
		try{
			//handleCarCharge charge = new handleCarCharge(msg.getContent());
			requests.add(msg.getContent());
			handleCarCharge carCharge = new handleCarCharge(msg.getContent());
		
		} catch (NullPointerException e)
		{
			System.out.println("Stopping...");
			
//			for(int i = 0; i < carCharge.getHoursInDaySize(); i++)
//			{
//				System.out.println("The HourInDay has value: " + carCharge.getHoursInDay(i));
//			}
			requestTimes = new int[requests.size()];
			
			for(int i = 0; i < requests.size(); i++)
			{
				requestTimes[i] = Integer.parseInt(requests.get(i));
			}
			System.out.println("The number of request in array list is: " + requests.size());
			System.out.println("The number of request in int array is: " + requestTimes.length);
			
			for (int k = 0; k < requestTimes.length; k++)
			{
				System.out.println("The values are : " + requestTimes[k]);
			}
			
			stop();
		}

	}// end of onTick
} // end of Listen Class
	
	private class Update extends OneShotBehaviour
	{
		
		@Override
		public void action() {
			System.out.println("Update");
			
		}
	}// end of Update Class
	
	private class NotifyAll extends OneShotBehaviour
	{
		
		@Override
		public void action() {
			System.out.println("Notify All Cars");
			
		}
	}// end of Notify Class
	
} // end of Scheduling class

