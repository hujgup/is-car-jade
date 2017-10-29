package cos30018.assignment.logic;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MainClass {
	private MainClass() {
	}
	public static void main(String[] args) throws ControllerException {
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl(null, 8079, null);
		AgentContainer ac = rt.createMainContainer(p);
		ac.start(); // Blocks until container is started
		System.out.println("Container started.");
		ac.createNewAgent("spooler", "cos30018.assignment.logic.io.SpoolingAgent", new Object[] { ac }).start();
	}
}
