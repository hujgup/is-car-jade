package cos30018.assignment.userinterface;

import cos30018.assignment.logic.CarAgent;
import cos30018.assignment.logic.SchedulingAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 * @author James
 * This is the main class to run the program
 */
public class AssignmentMain {
	
	
	/**
	 * @param args
	 * createAgents()- creates the agents for the program
	 * See initialisation of the method below
	 */
	public static void main(String[] args) {
		System.out.println("Hello World");
		//createAgents();
	}
	
	
	
	private static void createAgents(){
		// Get an instance of the program at runtime so you can maltipulate 
		Runtime rt = Runtime.instance();
		
		// Arguments holds the name of the master agent name
		Object[] agruments = new Object[1];
		agruments[0] = "master";
		
		// Profile Object is used to make the agent and it's neccessary attributes 
		Profile pMain = new ProfileImpl(null,8888,null);
		pMain.setParameter(Profile.GUI, "true");
		pMain.setParameter(Profile.CONTAINER_NAME, "Master Agent");
		ContainerController mainController = rt.createMainContainer(pMain);
		
		Profile pSecondary = new ProfileImpl(null, 8888, null);
		pSecondary.setParameter(Profile.CONTAINER_NAME, "Car Agents");
		ContainerController secondController = rt.createAgentContainer(pSecondary);
		
		// Starting the agents. The assignment specifies that we need at least 6 car agents
		try{
			
			AgentController masterAgent = mainController.createNewAgent("master", SchedulingAgent.class.getName(), new Object[0]);
			masterAgent.start();
			
			
//			for (int i = 0; i < 6; i++)
//			{
				AgentController carAgent = secondController.createNewAgent("car0",CarAgent.class.getName(), agruments);
				carAgent.start();
				
//			}
			
			
			
			
		} catch(StaleProxyException e){
			e.printStackTrace();
		}
		
	};
	
}
