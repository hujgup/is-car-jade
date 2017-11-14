package cos30018.assignment.utils;

import jade.util.leap.Iterator;
import jade.util.leap.Serializable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;

public class handleCarCharge implements Serializable{
	
	// Prepare the variables needed for the class
	private CarID carID;
	private Map<CarID, Car> allCars;
	private Collection<Car> carCollection;
	private Car car;
	
	//private ArrayList<String> cars;
	// hold all the hours in a day
	private ArrayList<Integer> hoursInDay = new ArrayList<>();
	private double hoursToCharge;
	// Will hold the unavailableTimes of the car
	private ArrayList<Integer> unavaliableTimes;
	private ArrayList<Integer> finalTimes;
	
	// This is going to hold all the unavilableTimes
	private HashMap<CarID,ArrayList<Integer>> carTimes = new HashMap<CarID,ArrayList<Integer>>();
	private double totalCapacity;
	private int currentCharge;
//	private int requestedTime = 5;
//	private LocalTimeRange timeRange;
//	private LocalTimeRange tempRange;
//	private LocalTime low;
//	private LocalTime high;
//	private List<LocalTimeRange> timeList;
//	double currentChargeDouble;
	private Environment env;
	
	//Constructor function for creating the hoursInDay array
	public handleCarCharge(Environment e) {
		env = e;
		allCars = env.getAllCars();
		carCollection = allCars.values();
		car = carCollection.iterator().next();
		totalCapacity = car.getChargeCapacity();
		setHours(car.getCurrentCharge());
		populateUnavaliableTimes();
	}// end of handleCarCharge with environment
	
//	// handle the userInput
//	public handleCarCharge(String a) {
//		totalCapacity = 1000;
//		
//		// adding the car's ID to the array
////		if(cars.isEmpty()){
////			cars.add(carID);
////		}
//		
//		// setting the current charge to the userInput
//		currentCharge = Integer.parseInt(a);
//		
//		setHours(currentCharge);
//		
//		populateUnavaliableTimes();
//	}
	
	
	
	
	public int getHoursInDaySize()
	{
		return hoursInDay.size();
	}
	
	public int getHoursInDay(int num)
	{	
		return hoursInDay.get(num);
	}
	
	private void setHours(double current)
	{
		hoursToCharge = (totalCapacity - current) / car.getChargePerHour();
	}
	
	
	public double getHours()
	{
		return hoursToCharge;
	}
	
	private void populateUnavaliableTimes()
	{
		// make a new instance of the static array
		unavaliableTimes = new ArrayList<Integer>();
		finalTimes = new ArrayList<Integer>();
		
//		if(carCollection.iterator().hasNext())
//		{
//			car = carCollection.iterator().next();
//		}
		
		for (LocalTimeRange uTime : car.getUnavailableTimes())
		{
		    SimpleRange<Integer> span = uTime.toHourRange();
		    if (span.getLowerBound().getPivot() > span.getUpperBound().getPivot()) {
		        for (int i = span.getLowerBound().getPivot(); i < 24; i++) {
		            unavaliableTimes.add(i);
		        }
		        for (int i = 0; i <= span.getLowerBound().getPivot(); i++) {
		            unavaliableTimes.add(i);
		        }
		    } else {
		        for (int i = span.getLowerBound().getPivot(); i <= span.getUpperBound().getPivot(); i++) {
		            unavaliableTimes.add(i);
		        }
		    }
		    
		    
		}
		finalTimes = checkDuplicates(unavaliableTimes);
		carTimes.put(car.getOwner(), finalTimes);
		
	}
	
	public ArrayList<Integer> getUnTimes(CarID id)
	{
		return carTimes.get(id);
	}
	
	
	private ArrayList<Integer> checkDuplicates(ArrayList<Integer> a)
	 {
		 ArrayList<Integer> mainArray = a;
		 Set<Integer> set = new HashSet<>();
		 set.addAll(mainArray);
		 mainArray.clear();
		 mainArray.addAll(set);
		 Collections.sort(mainArray);
		 return mainArray;
	 }
	 
	
}// end of class
