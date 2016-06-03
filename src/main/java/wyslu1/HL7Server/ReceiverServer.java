package wyslu1.HL7Server;

import javax.servlet.Servlet;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class ReceiverServer {
	private int port;
	private Server server;

	public ReceiverServer(int port, String contextUri, String incomingUri, Receiver receiver) {

		this.port = port;

		// Create a Jetty server instance
		server = new Server(this.port);
		Context context = new Context(server, "/" + contextUri, Context.SESSIONS);
		Servlet servlet = receiver;

		context.addServlet(new ServletHolder(servlet), "/" + incomingUri);

	}

	public void start() throws Exception {
		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}
}
