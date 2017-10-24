package cos30018.assignment.utils;

import jade.util.leap.Iterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class handleCarCharge {
	
	// Prepare the variables needed for the class
	private String carID;
	private ArrayList<String> cars;
	// hold all the hours in a day
	private ArrayList<Integer> hoursInDay = new ArrayList<>();
	private int hoursToCharge;
	// Will hold the unavailableTimes of the car
	private ArrayList<Integer> unavaliableTimes;
	// This is going to hold all the unavilableTimes
	private HashMap<String,int[]> carTimes = new HashMap<String,int[]>();
	private HashMap<String,int[]> temp = new HashMap<String,int[]>();
	private int totalCapacity;
	private int currentCharge;
	private int requestedTime = 5;
	
	
	//Constructor function for creating the hoursInDay array
	public handleCarCharge() {
		// checking if the hoursInDay is empty
		if(hoursInDay.isEmpty())
		{
			for(Integer i = 1; i < 25; i++)
			{
				System.out.println("Adding the days...");
				hoursInDay.add(i);
				
				System.out.println(hoursInDay.size());
				
			}
		} else
		{
			System.out.println("HoursInDay is not empty");
		}
		
		
		
	}// end of handleCarCharge
	
	// handle the userInput
	public handleCarCharge(String a) {
		totalCapacity = 1000;
		
		// adding the car's ID to the array
//		if(cars.isEmpty()){
//			cars.add(carID);
//		}
		
		// setting the current charge to the userInput
		currentCharge = Integer.parseInt(a);
		
		setHours(currentCharge);
		
		System.out.println("The hours is: " + getHours());
	}
	
	
	
	
	public int getHoursInDaySize()
	{
		return hoursInDay.size();
	}
	
	public int getHoursInDay(int num)
	{	
		return hoursInDay.get(num);
	}
	
	private void setHours(int current)
	{
		hoursToCharge = (totalCapacity - current) / 50;
	}
	
	
	public int getHours()
	{
		return hoursToCharge;
	}
	
	public int[] getCarTimes(String s)
	{
		return carTimes.get(s);
	}
	
	private void populateUnavaliableTimes()
	{
		// make a new instance of the static array
		unavaliableTimes = new ArrayList<Integer>();
		
		
		
	}
	
	
	public void checkTimes(){
		if(!(carTimes.isEmpty())){
			// Iterator through the HashMap
			Iterator it = (Iterator) carTimes.entrySet().iterator();
			while(it.hasNext())
			{
				// get the  the 
				Map.Entry temp= (Map.Entry)it.next();
				
				int[] tempArray = (int[])temp.getValue();
			}
			
			
			
		} else {
			System.out.println("There is no times to check");
		}
	}
	
	
	
	
	

}
