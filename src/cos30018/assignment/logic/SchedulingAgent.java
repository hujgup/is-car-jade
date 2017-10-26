package cos30018.assignment.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.CarID;
import cos30018.assignment.data.Environment;
import cos30018.assignment.data.ImmutableCar;
import cos30018.assignment.data.Timetable;
import cos30018.assignment.data.TimetableEntry;
import cos30018.assignment.utils.LocalTimeRange;
import cos30018.assignment.utils.SimpleRange;
import jade.core.AID;
//import org.Json.JSONArray;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SchedulingAgent extends Agent {
	private AID C1AID;
	private AID C2AID;
	private AID C3AID;
	private AID C4AID;
	private AID C5AID;
	private AID C6AID;
	
	private int prefC1ChargeTimes = 6;
	private int prefC2ChargeTimes = 4;
	private int prefC3ChargeTimes = 3;
	private int prefC4ChargeTimes = 3;
	private int prefC5ChargeTimes = 4;
	private int prefC6ChargeTimes = 6;
	
	private boolean C1timesSat;
	private boolean C2timesSat;
	private boolean C3timesSat;
	private boolean C4timesSat;
	private boolean C5timesSat;
	private boolean C6timesSat;
	
	private int chargeTimes;
	private int totalChargeTime;
	private int chargeTimeUpper;
	private int arrayPos;
	
	private List<Integer> C1UnavailableTimes = new ArrayList<>();
	private List<Integer> C2UnavailableTimes = new ArrayList<>();
	private List<Integer> C3UnavailableTimes = new ArrayList<>();
	private List<Integer> C4UnavailableTimes = new ArrayList<>();
	private List<Integer> C5UnavailableTimes = new ArrayList<>();
	private List<Integer> C6UnavailableTimes = new ArrayList<>();
	
	private List<Integer> Car1ChargeTimes = new ArrayList<>();
	private List<Integer> Car2ChargeTimes = new ArrayList<>();
	private List<Integer> Car3ChargeTimes = new ArrayList<>();
	private List<Integer> Car4ChargeTimes = new ArrayList<>();
	private List<Integer> Car5ChargeTimes = new ArrayList<>();
	private List<Integer> Car6ChargeTimes = new ArrayList<>();
	
	private Integer[] C1ChargeTimes = new Integer[prefC1ChargeTimes];
	private Integer[] C2ChargeTimes = new Integer[prefC2ChargeTimes];
	private Integer[] C3ChargeTimes = new Integer[prefC3ChargeTimes];
	private Integer[] C4ChargeTimes = new Integer[prefC4ChargeTimes];
	private Integer[] C5ChargeTimes = new Integer[prefC5ChargeTimes];
	private Integer[] C6ChargeTimes = new Integer[prefC6ChargeTimes];
		
	private Integer[] HoursInDay = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
	
	private String errorMsg = "";
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String a) {
		errorMsg = a;
	}
	
	public int getArrayPos() {
		return arrayPos;
	}
	public void setArrayPos(int c) {
		arrayPos = c;
	}
	
	public int getChargeTimes() {
		return chargeTimes;
	}
	public void setChargeTimes(int c) {
		chargeTimes = c;
	}
	
	public int getChargeTimeUpper() {
		return chargeTimeUpper;
	}
	public void setChargeTimeUpper(int c) {
		chargeTimeUpper= c;
	}
	
	public int getTotalChargeTime() {
		return totalChargeTime;
	}
	public void setTotalChargeTime(int c) {
		totalChargeTime = c;
	}
	
	public int getC1PrefChargeTimes() {
		return prefC1ChargeTimes;
	}
	public void setC1PrefChargeTimes(int c) {
		prefC1ChargeTimes = c;
	}
	
	public int getC2PrefChargeTimes() {
		return prefC2ChargeTimes;
	}
	public void setC2PrefChargeTimes(int c) {
		prefC2ChargeTimes = c;
	}
	
	public int getC3PrefChargeTimes() {
		return prefC3ChargeTimes;
	}
	public void setC3PrefChargeTimes(int c) {
		prefC3ChargeTimes = c;
	}
	
	public int getC4PrefChargeTimes() {
		return prefC4ChargeTimes;
	}
	public void setC4PrefChargeTimes(int c) {
		prefC4ChargeTimes = c;
	}
	
	public int getC5PrefChargeTimes() {
		return prefC5ChargeTimes;
	}
	public void setC5PrefChargeTimes(int c) {
		prefC5ChargeTimes = c;
	}
	
	public int getC6PrefChargeTimes() {
		return prefC6ChargeTimes;
	}
	public void setC6PrefChargeTimes(int c) {
		prefC6ChargeTimes = c;
	}
	
	public void addInitialChargeTimes()
	{
		setTotalChargeTime(0);
		setChargeTimes(8);
		setChargeTimeUpper(24);
		setArrayPos(0); 
		boolean check = false;
		
		for(int j = 0; j < HoursInDay.length ; j++)
        {
            for(int i = 0 ; i < C1UnavailableTimes.size() ; i++)
            {
                if(C1UnavailableTimes.get(i) == HoursInDay[j]) {
                	check = false;
                    break;
                    
			    } else {
			    	check = true;
			    }
            }
            if (check == true) {
            	C1ChargeTimes[getArrayPos()] = HoursInDay[j];
    		    setArrayPos(getArrayPos() + 1);
    		    setTotalChargeTime(getTotalChargeTime() + 1);
    		    
    		    if ((getTotalChargeTime() >= getC1PrefChargeTimes() || HoursInDay[j] >= getChargeTimeUpper())) {
    		    	break;
    		    }
    		    else {
    		    	continue;
    		    }
            } else {
            	continue;
            }
        }
	}
	
	public void addStepTwoChargeTimes()
	{
		setTotalChargeTime(0);
		setChargeTimeUpper(24);
		setArrayPos(0); 
		boolean check = false;
		
		for(int j = 0; j < HoursInDay.length ; j++)
        {
			for(int i = 0 ; i < C2UnavailableTimes.size() ; i++)
            {
                if(C2UnavailableTimes.get(i) == HoursInDay[j]) {
                	check = false;
                    break;
                    
			    } else {
			    	check = true;
			    }
            }
			
			if (check == true) {
				for(int i = 0 ; i < C1ChargeTimes.length ; i++)
	            {
	                if(C1ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
		
            if (check == true) {
            	C2ChargeTimes[getArrayPos()] = HoursInDay[j];
    		    setArrayPos(getArrayPos() + 1);
    		    setTotalChargeTime(getTotalChargeTime() + 1);
    		    
    		    if ((getTotalChargeTime() >= getC2PrefChargeTimes() || HoursInDay[j] >= getChargeTimeUpper())) {
    		    	break;
    		    }
    		    else {
    		    	continue;
    		    }
            } else {
            	continue;
            }
        }
	}
	
	public void addStepThreeChargeTimes() {
		setTotalChargeTime(0);
		setChargeTimeUpper(24);
		setArrayPos(0); 
		boolean check = false;
		
		for(int j = 0; j < HoursInDay.length ; j++)
        {
			for(int i = 0 ; i < C3UnavailableTimes.size() ; i++)
            {
                if(C3UnavailableTimes.get(i) == HoursInDay[j]) {
                	check = false;
                    break;
                    
			    } else {
			    	check = true;
			    }
            }
			
			if (check == true) {
				for(int i = 0 ; i < C2ChargeTimes.length ; i++)
	            {
	                if(C2ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C1ChargeTimes.length ; i++)
	            {
	                if(C1ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
		
            if (check == true) {
            	C3ChargeTimes[getArrayPos()] = HoursInDay[j];
    		    setArrayPos(getArrayPos() + 1);
    		    setTotalChargeTime(getTotalChargeTime() + 1);
    		    
    		    if ((getTotalChargeTime() >= getC3PrefChargeTimes() || HoursInDay[j] >= getChargeTimeUpper())) {
    		    	break;
    		    }
    		    else {
    		    	continue;
    		    }
            } else {
            	continue;
            }
        }
	}
	
	public void addStepFourChargeTimes() {
		setTotalChargeTime(0);
		setChargeTimeUpper(24);
		setArrayPos(0); 
		boolean check = false;
		
		for(int j = 0; j < HoursInDay.length ; j++)
        {
			for(int i = 0 ; i < C4UnavailableTimes.size() ; i++)
            {
                if(C4UnavailableTimes.get(i) == HoursInDay[j]) {
                	check = false;
                    break;
                    
			    } else {
			    	check = true;
			    }
            }
			
			if (check == true) {
				for(int i = 0 ; i < C3ChargeTimes.length ; i++)
	            {
	                if(C3ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C2ChargeTimes.length ; i++)
	            {
	                if(C2ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C1ChargeTimes.length ; i++)
	            {
	                if(C1ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
		
            if (check == true) {
            	C4ChargeTimes[getArrayPos()] = HoursInDay[j];
    		    setArrayPos(getArrayPos() + 1);
    		    setTotalChargeTime(getTotalChargeTime() + 1);
    		    
    		    if ((getTotalChargeTime() >= getC4PrefChargeTimes() || HoursInDay[j] >= getChargeTimeUpper())) {
    		    	break;
    		    }
    		    else {
    		    	continue;
    		    }
            } else {
            	continue;
            }
        }
	}
	
	public void addStepFiveChargeTimes() {
		setTotalChargeTime(0);
		setChargeTimeUpper(24);
		setArrayPos(0); 
		boolean check = false;
		
		for(int j = 0; j < HoursInDay.length ; j++)
        {
			for(int i = 0 ; i < C5UnavailableTimes.size() ; i++)
            {
                if(C5UnavailableTimes.get(i) == HoursInDay[j]) {
                	check = false;
                    break;
                    
			    } else {
			    	check = true;
			    }
            }
			
			if (check == true) {
				for(int i = 0 ; i < C4ChargeTimes.length ; i++)
	            {
	                if(C4ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C3ChargeTimes.length ; i++)
	            {
	                if(C3ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C2ChargeTimes.length ; i++)
	            {
	                if(C2ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C1ChargeTimes.length ; i++)
	            {
	                if(C1ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
		
            if (check == true) {
            	C5ChargeTimes[getArrayPos()] = HoursInDay[j];
    		    setArrayPos(getArrayPos() + 1);
    		    setTotalChargeTime(getTotalChargeTime() + 1);
    		    
    		    if ((getTotalChargeTime() >= getC5PrefChargeTimes() || HoursInDay[j] >= getChargeTimeUpper())) {
    		    	break;
    		    }
    		    else {
    		    	continue;
    		    }
            } else {
            	continue;
            }
        }
	}
	
	public void addStepSixChargeTimes() {
		setTotalChargeTime(0);
		setChargeTimeUpper(24);
		setArrayPos(0); 
		boolean check = false;
		
		for(int j = 0; j < HoursInDay.length ; j++)
        {
			for(int i = 0 ; i < C6UnavailableTimes.size() ; i++)
            {
                if(C6UnavailableTimes.get(i) == HoursInDay[j]) {
                	check = false;
                    break;
                    
			    } else {
			    	check = true;
			    }
            }
			
			if (check == true) {
				for(int i = 0 ; i < C5ChargeTimes.length ; i++)
	            {
	                if(C5ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C4ChargeTimes.length ; i++)
	            {
	                if(C4ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C3ChargeTimes.length ; i++)
	            {
	                if(C3ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C2ChargeTimes.length ; i++)
	            {
	                if(C2ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
			
			if (check == true) {
				for(int i = 0 ; i < C1ChargeTimes.length ; i++)
	            {
	                if(C1ChargeTimes[i] == HoursInDay[j]) {
	                	check = false;
	                    break;
	                    
				    } else {
				    	check = true;
				    }
	            }
			} else {}
		
            if (check == true) {
            	C6ChargeTimes[getArrayPos()] = HoursInDay[j];
    		    setArrayPos(getArrayPos() + 1);
    		    setTotalChargeTime(getTotalChargeTime() + 1);
    		    
    		    if ((getTotalChargeTime() >= getC6PrefChargeTimes() || HoursInDay[j] >= getChargeTimeUpper())) {
    		    	break;
    		    }
    		    else {
    		    	continue;
    		    }
            } else {
            	continue;
            }
        }
	}
	
	public boolean satArrays(Integer[] A) {
		int EmptySlot = 0;
		boolean check = false;
		
		for (int i = 0; i < A.length; i++) {
			if (A[i] == null) {
				EmptySlot++;
			} else {}
		}
		
		if (EmptySlot >= 2) {
			check = false;
		} else {
			check = true;
		}
		return check;
	}
	
	public boolean satArrays2(Integer[] A) {
		int EmptySlot = 0;
		boolean check = false;
		
		for (int i = 0; i < A.length; i++) {
			if (A[i] == null) {
				EmptySlot++;
			} else {}
		}
		
		if (EmptySlot >
		3) {
			check = false;
		} else {
			check = true;
		}
		return check;
	}
	
	public int emptySlots(Integer[] A) {
		int EmptySlot = 0;
		
		for (int i = 0; i < A.length; i++) {
			if (A[i] == 0) {
				EmptySlot++;
			} else {}
		}
		return EmptySlot;
	}
	
	public void fillArray(Integer[] a, Integer[] b, List<Integer> c) {
		boolean check = false;
		boolean check2 = false;
		int availTime = 0;
		
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < c.size(); j++) {
				if(c.get(j) == a[i]) {
                	check = false;
                    break;
			    } else {
			    	check = true;
			    	availTime = a[i];
			    }
				
				if(check == true) {
					for(int k = 0; k < a.length; k++) {
						if(a[k] == availTime) {
							a[k] = 0;
							for(int l = 0; l < b.length; l++) {
								if(b[l] == null) {
									check2 = true;
									b[l] = availTime;
								} else {}
								
								if(check2 == true) {
									check2 = false;
									break;
								} else {
									continue;
								}
							}
						} else {}
					}
				} else {}
			}
			
			if((satArrays(b) == false) && (satArrays(a) == true)) {
				continue;
			} else {
				break;
			}
		}
	}
	
	public void organizeTimetables(Integer[] a, Integer[] b, Integer[] c, Integer[] d, Integer[] e, Integer[] f) {
		
		if (C6AID != null) {
			if ((satArrays(f) == false) && (satArrays(a) == true)) {
		        fillArray(a, f, C1UnavailableTimes);
			} if ((satArrays(f) == false) && (satArrays(a) == false) && (satArrays(b) == true)) {
				fillArray(b, f, C2UnavailableTimes);
			} if ((satArrays(f) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == true)) {
				fillArray(c, f, C3UnavailableTimes);
			} if ((satArrays(f) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(d) == true)) {
				fillArray(d, f, C4UnavailableTimes);
			} if ((satArrays(f) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(d) == false) && (satArrays(e) == true)) {
				fillArray(e, f, C5UnavailableTimes);
			}
		}
		
		if (C5AID != null) {
			if ((satArrays(e) == false) && (satArrays(a) == true)) {
		        fillArray(a, e, C1UnavailableTimes);
			} if ((satArrays(e) == false) && (satArrays(a) == false) && (satArrays(b) == true)) {
				fillArray(b, e, C2UnavailableTimes);
			} if ((satArrays(e) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == true)) {
				fillArray(c, e, C3UnavailableTimes);
			} if ((satArrays(e) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(d) == true)) {
				fillArray(d, e, C4UnavailableTimes);
			} if (C6AID != null && (satArrays(e) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(d) == false) && (satArrays(f) == true)) {
				fillArray(f, e, C6UnavailableTimes);
			}
		}
		
		if (C4AID != null) {
			if ((satArrays(d) == false) && (satArrays(a) == true)) {
		        fillArray(a, d, C1UnavailableTimes);
			} if ((satArrays(d) == false) && (satArrays(a) == false) && (satArrays(b) == true)) {
				fillArray(b, d, C2UnavailableTimes);
			} if ((satArrays(d) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == true)) {
				fillArray(c, d, C3UnavailableTimes);
			} if (C5AID != null && (satArrays(d) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(e) == true)) {
				fillArray(e, d, C5UnavailableTimes);
			} if (C6AID != null && (satArrays(d) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(e) == false) && (satArrays(f) == true)) {
				fillArray(f, d, C6UnavailableTimes);
			}
		}
		
		if (C3AID != null) {
			if ((satArrays(c) == false) && (satArrays(a) == true)) {
		        fillArray(a, c, C1UnavailableTimes);
			} if ((satArrays(c) == false) && (satArrays(a) == false) && (satArrays(b) == true)) {
				fillArray(b, c, C2UnavailableTimes);
			} if (C4AID != null && (satArrays(c) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(d) == true)) {
				fillArray(d, c, C4UnavailableTimes);
			} if (C5AID != null && (satArrays(c) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(d) == false) && (satArrays(e) == true)) {
				fillArray(e, c, C5UnavailableTimes);
			} if (C6AID != null && (satArrays(c) == false) && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(d) == false) && (satArrays(e) == false) && (satArrays(f) == true)) {
				fillArray(f, c, C6UnavailableTimes);
			}
		}
		
		if (C2AID != null) {
			if ((satArrays(b) == false) && (satArrays(a) == true)) {
		        fillArray(a, b, C1UnavailableTimes);
			} if (C3AID != null && (satArrays(b) == false) && (satArrays(a) == false) && (satArrays(c) == true)) {
				fillArray(c, b, C3UnavailableTimes);
			} if (C4AID != null && (satArrays(b) == false) && (satArrays(a) == false) && (satArrays(c) == false) && (satArrays(d) == true)) {
				fillArray(d, b, C4UnavailableTimes);
			} if (C5AID != null && (satArrays(b) == false) && (satArrays(a) == false) && (satArrays(c) == false) && (satArrays(d) == false) && (satArrays(e) == true)) {
				fillArray(e, b, C5UnavailableTimes);
			} if (C6AID != null && (satArrays(b) == false) && (satArrays(a) == false) && (satArrays(c) == false) && (satArrays(d) == false) && (satArrays(e) == false) && (satArrays(f) == true)) {
				fillArray(f, b, C6UnavailableTimes);
			}
		}
		
		if (C2AID != null && (satArrays(a) == false) && (satArrays(b) == true)) {
	        fillArray(b, a, C2UnavailableTimes);
		} if (C3AID != null && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == true)) {
			fillArray(c, a, C3UnavailableTimes);
		} if (C4AID != null && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(d) == true)) {
			fillArray(d, a, C4UnavailableTimes);
		} if (C5AID != null && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(d) == false) && (satArrays(e) == true)) {
			fillArray(e, a, C5UnavailableTimes);
		} if (C6AID != null && (satArrays(a) == false) && (satArrays(b) == false) && (satArrays(c) == false) && (satArrays(d) == false) && (satArrays(e) == false) && (satArrays(f) == true)) {
			fillArray(f, a, C6UnavailableTimes);
		}
		
		if (satArrays2(a) == false) {
			setErrorMsg("Can't satisfy Car1's timetable");
		} else {}
		if (C2AID != null && satArrays2(b) == false) {
			setErrorMsg(getErrorMsg() + "--Can't satisfy Car2's timetable");
		} else {}
		if (C3AID != null && satArrays2(c) == false) {
			setErrorMsg(getErrorMsg() + "--Can't satisfy Car3's timetable");
		} else {}
		if (C4AID != null && satArrays2(d) == false) {
			setErrorMsg(getErrorMsg() + "--Can't satisfy Car4's timetable");
		} else {}
		if (C5AID != null && satArrays2(e) == false) {
			setErrorMsg(getErrorMsg() + "--Can't satisfy Car5's timetable");
		} else {}
		if (C6AID != null && satArrays2(f) == false) {
			setErrorMsg(getErrorMsg() + "--Can't satisfy Car6's timetable");
		} else {}
	}
	
	public boolean timetableSatisified() {
		
		boolean check = false;
		
		if ((C1timesSat == true) && (C2timesSat == true) && (C3timesSat == true) && (C4timesSat == true) && (C5timesSat == true) && (C6timesSat == true)) {
          check = true;
		} else {
		  check = false;
		}
		return check;
	}
	
	public void toList(String s, List<Integer> l) {
		String replace = s.replace("[","");
		String replace1 = replace.replace("]","");
		String replace2 = replace1.replace(" ","");
		List<String> arrayList = new ArrayList<String>    (Arrays.asList(replace2.split(",")));
		for(String fav:arrayList){
		    l.add(Integer.parseInt(fav.trim()));
		}
	}
	
	public void scheduleCars() throws IllegalAccessException {
		
		addInitialChargeTimes();
		if (C2AID != null) {
			addStepTwoChargeTimes();
			if (C3AID != null) {
				addStepThreeChargeTimes();
				if (C4AID != null) {
					addStepFourChargeTimes();
					if (C5AID != null) {
						addStepFiveChargeTimes();
						if (C6AID != null) {
							addStepSixChargeTimes();
						}
					}
				}
			}
		}
		
		int i2 = 1;
		Integer[] value;
		for (Field f : fChargeTimes) {
			System.out.println("C" + i2 + "ChargeTimes: ");
			value = (Integer[])f.get(this);
			for (int i=0; i < value.length; i++)
			{
				System.out.println(value[i]);
			}
			i2++;
		}
		
		C1timesSat = satArrays2(C1ChargeTimes);
		if (C2AID != null) {
			C2timesSat = satArrays2(C2ChargeTimes);
			if (C3AID != null) {
				C3timesSat = satArrays2(C3ChargeTimes);
				if (C4AID != null) {
					C4timesSat = satArrays2(C4ChargeTimes);
					if (C5AID != null) {
						C5timesSat = satArrays2(C5ChargeTimes);
						if (C6AID != null) {
							C6timesSat = satArrays2(C6ChargeTimes);
						}
					}
				}
			}
		}
		
		i2 = 1;
		for (Field f : fChargeTimes) {
			System.out.println("C" + i2 + "ChargeTimes: ");
			value = (Integer[])f.get(this);
			for (int i=0; i < value.length; i++)
			{
				System.out.println(value[i]);
			}
			i2++;
		}
		
		System.out.println("Timetables are satisfied: " + timetableSatisified());
		
		organizeTimetables(C1ChargeTimes, C2ChargeTimes, C3ChargeTimes, C4ChargeTimes, C5ChargeTimes, C6ChargeTimes);
		
		i2 = 1;
		for (Field f : fChargeTimes) {
			System.out.println("C" + i2 + "ChargeTimes: ");
			value = (Integer[])f.get(this);
			for (int i=0; i < value.length; i++)
			{
				System.out.println(value[i]);
			}
			i2++;
		}
		
		C1timesSat = satArrays2(C1ChargeTimes);
		if (C2AID != null) {
			C2timesSat = satArrays2(C2ChargeTimes);
			if (C3AID != null) {
				C3timesSat = satArrays2(C3ChargeTimes);
				if (C4AID != null) {
					C4timesSat = satArrays2(C4ChargeTimes);
					if (C5AID != null) {
						C5timesSat = satArrays2(C5ChargeTimes);
						if (C6AID != null) {
							C6timesSat = satArrays2(C6ChargeTimes);
						}
					}
				}
			}
		}
		
		System.out.println("Timetables are satisfied: " + timetableSatisified());
		System.out.println("" + getErrorMsg());
		
		System.out.println(C1UnavailableTimes);
		System.out.println(C2UnavailableTimes);
		System.out.println(C3UnavailableTimes);
		System.out.println(C4UnavailableTimes);
		System.out.println(C5UnavailableTimes);
		System.out.println(C6UnavailableTimes);
		
		for (int i=0; i<C1ChargeTimes.length; i++)
		{
		    Car1ChargeTimes.add(C1ChargeTimes[i]);
		}
		if (C2AID != null) {
			for (int i=0; i<C2ChargeTimes.length; i++)
			{
			    Car2ChargeTimes.add(C2ChargeTimes[i]);
			}
			if (C3AID != null) {
				for (int i=0; i<C3ChargeTimes.length; i++)
				{
				    Car3ChargeTimes.add(C3ChargeTimes[i]);
				}
				if (C4AID != null) {
					for (int i=0; i<C4ChargeTimes.length; i++)
					{
					    Car4ChargeTimes.add(C4ChargeTimes[i]);
					}
					if (C5AID != null) {
						for (int i=0; i<C5ChargeTimes.length; i++)
						{
						    Car5ChargeTimes.add(C5ChargeTimes[i]);
						}
						if (C6AID != null) {
							for (int i=0; i<C6ChargeTimes.length; i++)
							{
							    Car6ChargeTimes.add(C6ChargeTimes[i]);
							}
						}
					}
				}
			}
		}
	}
	
	private Environment env;
	// Reflection because for some reason the rest of this code doesn't use object arrays
	private static ArrayList<Field> ids = new ArrayList<>(6);
	private static ArrayList<Field> prefTimes = new ArrayList<>(6);
	private static ArrayList<Field> uTimes = new ArrayList<>(6);
	private static ArrayList<Field> fChargeTimes = new ArrayList<>(6);
	static {
		Class<?> c = SchedulingAgent.class;
		try {
			for (int i = 1; i <= 6; i++) {
				ids.add(c.getDeclaredField("C" + i + "AID"));
				prefTimes.add(c.getDeclaredField("prefC" + i + "ChargeTimes"));
				uTimes.add(c.getDeclaredField("C" + i + "UnavailableTimes"));
				fChargeTimes.add(c.getDeclaredField("C" + i + "ChargeTimes"));
			}
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void writeTimetable(ObjectOutputStream oos, Timetable tt) throws IOException {
		oos.writeBoolean(false);
		oos.writeObject(tt);
	}
	private void writeError(ObjectOutputStream oos, String msg) throws IOException {
		oos.writeBoolean(true);
		oos.writeObject(msg);
	}
	private void convertSentValues() throws IllegalAccessException {
		Collection<Car> cars = env.getAllCars().values();
		for (Field id : ids) {
			id.set(this, null);
		}
		int i = 0;
		ArrayList<Integer> selfUTimes;
		SimpleRange<Integer> uTime;
		double toCharge;
		int chargeHours;
		int low;
		int high;
		int j;
		for (Car car : cars) {
			toCharge = car.getChargeCapacity() - car.getCurrentCharge();
			chargeHours = (int)Math.ceil(toCharge/car.getChargePerHour());
			ids.get(i).set(this, car.getOwner().getCar());
			prefTimes.get(i).set(this, chargeHours);
			fChargeTimes.get(i).set(this, new Integer[chargeHours]);
			selfUTimes = new ArrayList<>();
			for (LocalTimeRange range : car.getUnavailableTimes()) {
				uTime = range.toHourRange();
				// +1 here because Java time is zero-based but code here is one-based (for some reason)
				low = uTime.getLowerBound().getPivot() + 1;
				high = uTime.getUpperBound().getPivot() + 1;
				if (low <= high) {
					for (j = low; j <= high; j++) {
						selfUTimes.add(j);
					}
				} else {
					for (j = low; j <= 24; j++) {
						selfUTimes.add(j);
					}
					for (j = 0; j <= high; j++) {
						selfUTimes.add(j);
					}
				}
			}
			uTimes.get(i).set(this, selfUTimes);
			i++;
		}
	}
	private void resolveRange(int low, int high, Integer[] times, ArrayList<LocalTimeRange> ranges) {
		if (low == 0) {
			// Check for wrap around midnight
			int i = times.length - 1;
			int expected = 24;
			while (i > 0 && times[i] == expected) {
				low = expected - 1;
				expected--;
				i--;
			}
		}
		System.out.println("[" + low + ", " + high + "]");
		ranges.add(new LocalTimeRange(LocalTime.of(low, 0), true, LocalTime.of(high, 0), true));
	}
	private Timetable createTimetable() throws IllegalAccessException {
		Timetable res = new Timetable();
		int i = 0;
		for (Field cTime : fChargeTimes) {
			if (i < env.getAllCars().size()) {
				Arrays.sort((Integer[])cTime.get(this));
				i++;
			} else {
				break;
			}
		}
		ArrayList<LocalTimeRange> ranges = new ArrayList<>();
		i = 0;
		Integer[] times;
		int low = -1;
		int high = -1;
		int last = -1;
		for (ImmutableCar car : env.getAllCars().values()) {
			times = (Integer[])fChargeTimes.get(i).get(this);
			System.out.println(times.length);
			for (int time : times) {
				high = -1;
				time--; // 1-indexed back to 0-indexed
				if (low == -1) {
					low = time;
				} else if (last != time - 1) {
					high = time - 1;
					resolveRange(low, high, times, ranges);
					low = time;
				}
				last = time;
			}
			if (high == -1) {
				high = times[times.length - 1];
				resolveRange(low, high, times, ranges);
			}
			for (LocalTimeRange range : ranges) {
				res.addEntry(new TimetableEntry(car.getOwner(), range));
			}
			ranges.clear();
			i++;
		}
		return res;
	}
	@Override
	protected void setup() {
		env = null;
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage msg = blockingReceive();
				System.out.println("Got message");
				try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
					ObjectOutputStream oos = new ObjectOutputStream(bos);
					ACLMessage response = msg.createReply();
					try {
						response.setPerformative(ACLMessage.INFORM);
						try (ByteArrayInputStream bis = new ByteArrayInputStream(msg.getByteSequenceContent())) {
							ObjectInputStream ois = new ObjectInputStream(bis);
							boolean isConstraintUpdate = ois.readBoolean();
							if (isConstraintUpdate) {
								Environment env2 = (Environment)ois.readObject();
								// Flooring unavailable times to the hour
								for (ImmutableCar car : env2.getAllCars().values()) {
									for (LocalTimeRange range : car.getUnavailableTimes()) {
										range.getLowerBound().getPivot().minusMinutes(range.getLowerBound().getPivot().getMinute());
										range.getLowerBound().setInclusive(true);
										range.getUpperBound().getPivot().minusMinutes(range.getUpperBound().getPivot().getMinute());
										range.getUpperBound().setInclusive(true);
									}
								}
								// Merging environments
								if (env == null) {
									env = env2;
								} else {
									LinkedList<Entry<CarID, Car>> toReplace = new LinkedList<>();
									for (Entry<CarID, Car> kvp : env2.getAllCars().entrySet()) {
										if (env.hasCar(kvp.getKey())) {
											toReplace.add(kvp);
										}
									}
									for (Entry<CarID, Car> kvp : toReplace) {
										env.removeCar(kvp.getKey());
										env.addCar(kvp.getValue());
									}
								}
							}
							if (env != null) {
								convertSentValues();
								scheduleCars();
								writeTimetable(oos, createTimetable());
							} else {
								writeError(oos, "Cannot negotiate a timetable when the environment has not been set.");								
							}
						}
					} catch (IOException | ClassNotFoundException | IllegalAccessException e) {
						e.printStackTrace();
						writeError(oos, e.getMessage());
					}
					System.out.println("Sending reply");
					bos.flush();
					response.setByteSequenceContent(bos.toByteArray());
					send(response);
				} catch (IOException e) {
					e.printStackTrace();					
				}
				
				/*
				if(msg!=null) {
					if(C1AID != null) {
						if(C2AID != null) {
							if(C3AID != null) {
								if(C4AID != null) {
									if(C5AID != null) {
										if(C6AID != null) {
										}
										else {
											C6AID = msg.getSender();
											toList(msg.getContent(), C6UnavailableTimes);
										}
									}
									else {
										C5AID = msg.getSender();
										toList(msg.getContent(), C5UnavailableTimes);
									}
								}
								else {
									C4AID = msg.getSender();
									toList(msg.getContent(), C4UnavailableTimes);
								}
							}
							else {
								C3AID = msg.getSender();
								toList(msg.getContent(), C3UnavailableTimes);
							}
						}
						else {
							C2AID = msg.getSender();
							toList(msg.getContent(), C2UnavailableTimes);
						}
					}
					else {
						C1AID = msg.getSender();
						toList(msg.getContent(), C1UnavailableTimes);
					}
					
					if((C1AID != null) && (C2AID != null) && (C3AID != null) && (C4AID != null) && (C5AID != null) && (C6AID != null) && (runTime == 0)) {
						runTime++;
						
						scheduleCars();
						
						System.out.println("\n");
						
						System.out.println(Car1ChargeTimes);
						System.out.println(Car2ChargeTimes);
						System.out.println(Car3ChargeTimes);
						System.out.println(Car4ChargeTimes);
						System.out.println(Car5ChargeTimes);
						System.out.println(Car6ChargeTimes);
						
						ACLMessage msg1=new ACLMessage(ACLMessage.INFORM);
						msg1.setContent("" + Car1ChargeTimes);
						msg1.addReceiver(C1AID);
						send(msg1);
					}
				}	
			*/
			}
		});	
	}
		
}

