package cos30018.assignment.logic;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.Timetable;
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
		
		
		
		// testing  (changed - Jake wrote this function)
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
				ACLMessage numHours = new ACLMessage(ACLMessage.INFORM);
					
					carCharge = new handleCarCharge(env);
					unavailableTimes = carCharge.getUnTimes(carID);
					msg.setContent(""+ unavailableTimes);
					numHours.setContent("" + carCharge.getHours());
					//System.out.println("The number of hours is: " + carCharge.getHours());
					msg.addReceiver(dest);
					send(msg);
					send(numHours);
					
//					// recieve message
//					ACLMessage response = blockingReceive();
//					try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getByteSequenceContent())) {
//						ObjectInputStream ois = new ObjectInputStream(bis);
//						// Map<CarID, List<Integer>>
//						Map<CarID, List<Integer>> timetable = (Map<CarID, List<Integer>>)ois.readObject();
//						return ActionResult.createResult(timetable);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}

					return ActionResult.createResult(testTimetable);
			}
			
		};
		try {
			addBehaviour(new UpdateServerBehaviour(env, carID, callback));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	 } // end of Setup
	 
	 
}// end of class