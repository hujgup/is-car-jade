package cos30018.assignment.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import fi.iki.elonen.NanoHTTPD;

public interface BlockingServer {
	static class Implementation extends NanoHTTPD implements BlockingServer {
		private Queue<BlockingRequest> requests;
		private Semaphore mutateSem;
		private Semaphore requestsSem;
		private Implementation(int port) throws IOException {
			super(port);
			requests = new LinkedList<>();
			mutateSem = new Semaphore(1);
			requestsSem = new Semaphore(0);
			start();
		}
		@Override
		public Response serve(IHTTPSession session) {
			Response res;
			if (session.getUri().equals("/favicon.ico")) {
				res = NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "no favicon");
			} else {
				mutateSem.acquireUninterruptibly();
				BlockingRequest req = new BlockingRequest(session);
				try {
					session.parseBody(new HashMap<>());
					requests.add(req);
					requestsSem.release();
					mutateSem.release();
					res = req.getResponse(true);
				} catch (IOException | ResponseException e) {
					e.printStackTrace();
					res = null;
				}
			}
			return res;
		}
		@Override
		public BlockingRequest block() {
			requestsSem.acquireUninterruptibly();
			mutateSem.acquireUninterruptibly();
			BlockingRequest req = requests.poll();
			mutateSem.release();
			return req;
		}
	}
	
	BlockingRequest block();
	static BlockingServer create(int port) throws IOException {
		return new Implementation(port);
	}
}
