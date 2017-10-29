package cos30018.assignment.logic.io;

import java.io.IOException;
import java.util.HashMap;
import cos30018.assignment.data.CarID;
import cos30018.assignment.logic.ServerBehaviour;
import cos30018.assignment.logic.scheduling.InformContent;
import cos30018.assignment.ui.http.Responder;
import cos30018.assignment.ui.json.Action;
import cos30018.assignment.ui.json.Json;
import cos30018.assignment.ui.json.JsonData;
import cos30018.assignment.utils.Input;
import cos30018.assignment.utils.Mutable;
import cos30018.assignment.utils.Output;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

@SuppressWarnings("serial")
public class SpoolingServerBehaviour extends ServerBehaviour {
	private Agent a;
	private AgentContainer ac;
	private HashMap<String, AgentController> carAgents;
	private HashMap<String, AgentController> schAgents;
	private HashMap<String, AID> schAids;
	public SpoolingServerBehaviour(Agent a, AgentContainer ac) throws IOException {
		super(8080);
		this.a = a;
		this.ac = ac;
		carAgents = new HashMap<>();
		schAgents = new HashMap<>();
		schAids = new HashMap<>();
	}
	@Override
	protected void handle(IHTTPSession session, Responder responder) {
		switch (session.getMethod()) {
			case GET:
				if (session.getParms().containsKey("json")) {
					try {
						JsonData jsonData = JsonData.fromJson(session.getParms().get("json"));
						jsonData.validate(null, null, Action.ADD_CAR, Action.REMOVE_CAR, Action.GET_CARS);
						if (jsonData.isCarGet()) {
							responder.respond(Json.serialize(carAgents.keySet()));
						} else if (jsonData.isCarAdd()) {
							int cid = CarID.generateID();
							String schAidStr = "sch" + cid;
							AgentController schAc = ac.createNewAgent(schAidStr, "cos30018.assignment.logic.scheduling.SchedulingAgent", new Object[] { cid, schAids.values() });
							schAc.start();
							AID schAid = new AID(schAidStr, false);
							AgentController carAc = ac.createNewAgent("car" + cid, "cos30018.assignment.logic.io.CarAgent", new Object[] { cid, schAid });
							carAc.start();
							String cidStr = Integer.toString(cid);
							schAgents.put(cidStr, schAc);
							schAids.put(cidStr, schAid);
							carAgents.put(cidStr, carAc);
							responder.respond(cidStr);
						} else if (jsonData.isCarRemove()) {
							ACLMessage msg = Output.create(schAids.values());
							Output.write(msg, oos -> {
								oos.writeObject(InformContent.REMOVE_PROPAGATE);
								oos.writeInt(Integer.parseInt(jsonData.getID()));
							});
							a.send(msg);
							Mutable<Boolean> isError = new Mutable<>(false);
							Mutable<String> error = new Mutable<>("");
							for (int i = 0; i < schAids.size(); i++) {
								ACLMessage response = a.blockingReceive();
								Input.read(response, ois -> {
									boolean resIsError = ois.readBoolean();
									if (resIsError) {
										String err = (String)ois.readObject();
										System.err.println(err);
										if (isError.value) {
											error.value += "\n\n";
										}
										error.value += err;
										isError.value = true;
									}
								});
							}
							if (isError.value) {
								generateError(responder, error.value);
							} else {
								AgentController carAc = carAgents.get(jsonData.getID());
								if (carAc != null) {
									AgentController schAc = schAgents.get(jsonData.getID());
									carAc.kill();
									schAc.kill();
									carAgents.remove(jsonData.getID());
									schAgents.remove(jsonData.getID());
									schAids.remove(jsonData.getID());
									responder.respond("\"Car " + jsonData.getID() + " removed.\"");
								} else {
									responder.respond("\"Car " + jsonData.getID() + " does not exist - no need to remove.\"");
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						generateError(responder, e.getMessage());						
					} catch (Throwable e) {
						System.out.println("ERR: Data was not JSON.");
						e.printStackTrace();
						generateError(responder, "Key \"json\" contained malformed JSON: " + e.getMessage());
					}
				} else {
					System.out.println("ERR: No JSON key.");
					generateError(responder, "Key \"json\" was not present.");
				}
				break;
			default:
				System.out.println("ERR: Wrong method.");
				generateError(responder, "Not a POST request.");
				break;
		}
	}
}
