package cos30018.assignment.userinterface;

import java.util.Scanner;

import cos30018.assignment.logic.SchedulingAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class BasicGui {
	Scanner scan;
	int selection;
	
	
	
	public BasicGui()
	{
		initMenu();
		menu();
	}
	
	public void initMenu()
	{
		System.out.println("Please specify how many schelduling agents: ");
		scan = new Scanner(System.in);
		selection = Integer.parseInt(scan.nextLine());
		
		ScheduleAgent(selection);
	}
	
	
	
	public void menu()
	{
		System.out.println("Please choose a selection: ");
		System.out.println("1: Register Car");
		System.out.println("2: TimeTable");
		System.out.println("3: Exit");
		System.out.println("Selection: ");
		scan = new Scanner(System.in);
		selection = Integer.parseInt(scan.nextLine());
		
		switch(selection)
		{
			case 1:
				 System.out.println("Registering your car");
				 // Check if the car has been registered before
				 	// If it has then Get the id and return to the menu
				 	// if not, then make a new Car object
				 menu();
				 break;
			case 2:
				System.out.println("TimeTable");
				menu();
				break;
			case 3:
				System.out.println("Go Back");
				menu();
				break;
		}
	} // end menu
	
	private void ScheduleAgent(int num){
		Runtime rt = Runtime.instance();
		
		Profile pMain = new ProfileImpl(null,8888,null);
		pMain.setParameter(Profile.GUI, "true");
		pMain.setParameter(Profile.CONTAINER_NAME, "Scheduling Agents");
		ContainerController mainCtrl = rt.createMainContainer(pMain);
			
		for(int i = 0; i < num; i++)
		{
			try{
			
				AgentController schedulingAgents = mainCtrl.createNewAgent("schl"+i, SchedulingAgent.class.getName(), new Object[0]);
				schedulingAgents.start();
			
			}catch(StaleProxyException e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	

}
