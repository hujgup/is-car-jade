package cos30018.assignment.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import cos30018.assignment.data.CarID;
import cos30018.assignment.ui.http.Responder;
import cos30018.assignment.ui.json.Action;
import cos30018.assignment.ui.json.Json;
import cos30018.assignment.ui.json.JsonData;
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
	private HashMap<String, AgentController> agents;
	public SpoolingServerBehaviour(Agent a, AgentContainer ac) throws IOException {
		super(8080);
		this.a = a;
		this.ac = ac;
		agents = new HashMap<>();
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
							responder.respond(Json.serialize(agents.keySet()));
						} else if (jsonData.isCarAdd()) {
							int cid = CarID.generateID();
							AgentController carAc = ac.createNewAgent("car" + cid, "cos30018.assignment.logic.CarAgent", new Object[] { cid });
							carAc.start();
							String cidStr = Integer.toString(cid);
							agents.put(cidStr, carAc);
							responder.respond(cidStr);
						} else if (jsonData.isCarRemove()) {
							AID master = new AID("master", false);
							ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
							msg.addReceiver(master);
							try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
								ObjectOutputStream oos = new ObjectOutputStream(bos);
								oos.writeObject(InformContent.REMOVE);
								oos.writeInt(Integer.parseInt(jsonData.getID()));
								oos.flush();
								bos.flush();
								msg.setByteSequenceContent(bos.toByteArray());
							}
							a.send(msg);
							ACLMessage response = a.blockingReceive();
							try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getByteSequenceContent())) {
								ObjectInputStream ois = new ObjectInputStream(bis);
								boolean isError = ois.readBoolean();
								if (isError) {
									String err = (String)ois.readObject();
									System.out.println("ERR: Data was not JSON.");
									generateError(responder, err);
								} else {
									AgentController carAc = agents.get(jsonData.getID());
									if (carAc != null) {
										carAc.kill();
										agents.remove(jsonData.getID());
										responder.respond("\"Car " + jsonData.getID() + " removed.\"");
									} else {
										responder.respond("\"Car " + jsonData.getID() + " does not exist - no need to remove.\"");
									}
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
