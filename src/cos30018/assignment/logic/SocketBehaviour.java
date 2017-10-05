package cos30018.assignment.logic;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import cos30018.assignment.data.Car;
import cos30018.assignment.data.SystemData;
import cos30018.assignment.http.RequestOld;
import cos30018.assignment.http.Response;
import cos30018.assignment.json.UpdateGlobalsJson;
import cos30018.assignment.utils.Validate;
import jade.core.behaviours.CyclicBehaviour;

@SuppressWarnings("serial")
public class SocketBehaviour extends CyclicBehaviour implements AutoCloseable {
	private ServerSocket server;
	private SystemData data;
	public SocketBehaviour(SystemData data, int port) throws IOException {
		Validate.notNull(data, "data");
		Validate.positive(port, "port");
		this.data = data;
		server = new ServerSocket(port);
	}
	@Override
	public void action() {
		try (Socket connection = server.accept()) {
			RequestOld httpReq = new RequestOld(connection.getInputStream());
			UpdateGlobalsJson req = UpdateGlobalsJson.fromJson(httpReq.getBody());
			req.update(data);
			new Response.Builder()
				.bodyLine("found body:")
				.bodyText(httpReq.getBody())
				.echo(connection.getOutputStream());
			// TODO: Real implementation (Trigger timetable re-eval behaviour)
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void close() throws IOException {
		server.close();
	}
}
