package cos30018.assignment.logic;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.Timetable;
import cos30018.assignment.utils.LocalTimeRange;
import cos30018.assignment.utils.handleCarCharge;

@SuppressWarnings("serial")
public class CarAgent extends Agent {
	//private int numOfArguments;
	//private SequentialBehaviour listener;
	private List<Integer> unavailableTimes;
	private List<Integer> finalTimes;
	private handleCarCharge carCharge;
	private List<Integer> chargeTimes = new ArrayList<>();
	
	public void toList(String s, List<Integer> l) {
		String replace = s.replace("[","");
		String replace1 = replace.replace("]","");
		String replace2 = replace1.replace(" ","");
		List<String> arrayList = new ArrayList<String>    (Arrays.asList(replace2.split(",")));
		for(String fav:arrayList){
		    l.add(Integer.parseInt(fav.trim()));
		}
	}
	
	 public void setup() {
		Object[] args = getArguments();
		
		// Test Time Table
		Timetable testTimetable = new Timetable();
		AID carAID = getAID();
		//String masterAID = "master";
		String masterAID = args[0].toString();
//		String carAID = (String) args[1];
//		String inputValue = (String) args[2];
		CarID carID = CarID.create(getAID());
		
		
		
		// testing 
		Environment env = Environment.createDummyData();
		
		
//		List<LocalTimeRange> timeList = new ArrayList<>();
//		LocalTimeRange time = new LocalTimeRange(LocalTime.of(8, 00), false, LocalTime.of(12, 30), false);
//		timeList.add(time);
//		Car testCar = new Car(CarID.create(getAID()), 500, 1000, 100, 50, timeList);
//		Environment env2 = new Environment(100.00, testCar);
//		
		
		
		// env, carid, callback
		
		// Environment data, CarID id, Function<Boolean, ActionResult<Timetable>> messageSender
		CarAgent t = this;
		Function<Boolean, ActionResult<Timetable>> callback = new Function<Boolean, ActionResult<Timetable>>() {
			@Override
			public ActionResult<Timetable> apply(Boolean isConstraintUpdate) {
				AID dest = new AID(masterAID,AID.ISLOCALNAME);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					
					carCharge = new handleCarCharge(env);
					unavailableTimes = carCharge.getUnTimes(carID);
					msg.setContent(""+ unavailableTimes);
					msg.addReceiver(dest);
					send(msg);

					return ActionResult.createResult(testTimetable);
			}
			
		};
		try {
			addBehaviour(new UpdateServerBehaviour(env, carID, callback));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		addBehaviour(new TickerBehaviour(this, 3000) {
//		    public void onTick() 
//		    {
//			System.out.println("We are in the ticker");
//			ACLMessage msgRec = receive();
//			
//			if(msgRec!=null) {
//				toList(msgRec.getContent(), chargeTimes);
//				System.out.println(getLocalName() + "charge times are : ");
//				for (int i=0; i < chargeTimes.size(); i++)
//				{
//					System.out.println(chargeTimes.get(i));
//				}
//			}
//		    }
//		});
		
		
	 } // end of Setup
	 
	 
}// end of class