package cos30018.assignment.logic;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.SystemData;
import cos30018.assignment.http.Request;
import cos30018.assignment.json.ClientRequest;
import cos30018.assignment.utils.Validate;
import jade.core.behaviours.CyclicBehaviour;

@SuppressWarnings("serial")
public class SocketBehaviour extends CyclicBehaviour implements AutoCloseable {
	private ServerSocket server;
	private SystemData data;
	private Car car;
	public SocketBehaviour(SystemData data, Car car, int port) throws IOException {
		Validate.notNull(data, "data");
		Validate.notNull(car, "car");
		Validate.positive(port, "port");
		this.data = data;
		this.car = car;
		server = new ServerSocket(port);
	}
	@Override
	public void action() {
		try (Socket connection = server.accept()) {
			Request httpReq = new Request(connection.getInputStream());
			ClientRequest req = ClientRequest.fromJson(httpReq.getBody());
			PrintStream out = new PrintStream(connection.getOutputStream());
			req.update(data, car);
			// TODO: Trigger timetable re-eval behaviour
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void close() throws IOException {
		server.close();
	}
}
