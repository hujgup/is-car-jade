package cos30018.assignment.logic;


import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
/*
@author James
* The setup function has a method to find the agents that are currently running and gives back the ID
* This will be used o communicate between them through FIPA ACL Messaging
* Am Planning on completing this tonight, Please feel free to complete

* TO DO:

	- Create an CarAgent for each Sch Agent
	- Basic Communication between them and then,
	- Between Sch Agents

	- I have completed communications in my labs, it is just a matter of implementing them here

	 
	Issues
	- How to construct a Car Object using the Car and CarID Java files. 
	- Not sure how to initialise the LocalTimeRange List


*/
@SuppressWarnings("serial")
public class SchedulingAgent extends Agent {
	@Override
	public void setup() {
		// Get the current AID running
		AMSAgentDescription [] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
				agents = AMSService.search(this, new AMSAgentDescription(), c);
		} catch(Exception e) {
			System.out.println("Problem search AMS: " + e);
			e.printStackTrace();
		}
		
		AID AgentID = getAID();
		System.out.println(getAID().getName());
	}
	
	private void update(){};
	private void broadcast(){};
}
