package wyslu1.HL7Server;

import javax.servlet.Servlet;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	// The port to listen on
    	int port = 8080;

    	// Create a Jetty server instance
    	Server server = new Server(port);
    	Context context = new Context(server, "/Hl7Listener", Context.SESSIONS);
    	Servlet servlet = new Receiver();

    	/* 
    	 * Adds the servlet to listen at 
    	 * http://localhost:8080/Hl7Listener/Incoming
    	 */
    	context.addServlet(new ServletHolder(servlet), "/Incoming");

    	// Start the server
    	server.start();

    	// .. let the application run ..

    	/*
    	 * Later it will probably be appropriate to shut the server
    	 * down.
    	 */
    	//server.stop();
    	
    	Sender sender = new Sender();
    }
}
