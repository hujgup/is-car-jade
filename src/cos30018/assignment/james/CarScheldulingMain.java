package cos30018.assignment;

import java.util.Scanner;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class CarScheldulingMain {

	
	public static void main(String[] args) throws StaleProxyException, InterruptedException {
		init();
	}
	
	private static void startMainContainer() throws StaleProxyException, InterruptedException{
		// Get JADE at Runtime 
		Runtime rt = Runtime.instance();
		
		// Launch the Main Controller
		Profile pMain = new ProfileImpl(null,8888,null);
		pMain.setParameter(Profile.GUI, "true");
		ContainerController mainCtrl = rt.createMainContainer(pMain);
		// Wait 10 seconds
		Thread.sleep(10000);
		
		// Create and start agent
		AgentController agentCtrl = mainCtrl.createNewAgent("car1", CarAgent.class.getName(), new Object[0]);
		agentCtrl.start();
	}
	
	public static void init() throws StaleProxyException, InterruptedException
	{
		Scanner scan = new Scanner(System.in);
		int selection;
		
		
		System.out.println("Welcome to the Car Scheduling System");
		System.out.println("Please select what option you would like");
		
		
		System.out.println("1: Request Charge");
		System.out.println("2: Update");
		System.out.println("3: Exit");
		System.out.println("Selection: ");
		selection = Integer.parseInt(scan.nextLine());
		
		switch(selection){
		case 1: 
			System.out.println("You Selected: Request Charge");
			startMainContainer();
			break;
		case 2: 
			System.out.println("You Selected: Update");
			break;
		case 3: 
			System.out.println("You Selected: Exit");
			System.exit(0);
			break;
			
		}
		
		scan.close();
		
	}
	
	
	

}// end of Main
