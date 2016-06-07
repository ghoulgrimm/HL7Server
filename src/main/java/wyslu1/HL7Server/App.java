package wyslu1.HL7Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.hoh.hapi.api.MessageSendable;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;

public class App{
	
	private static ArrayList<Message> msgs = new ArrayList<Message>();
	private static Scanner userInput = new Scanner(System.in);
	private static Sender sender;
	private static Receiver receiver = new Receiver();
	private static ReceiverServer receiverServer = new ReceiverServer(8080, "HL7","incoming", receiver );
	
	
	public static void main(String[] args) throws Exception {
		
		sender = new Sender("localhost", 8080, "/HL7/incoming");
		receiverServer.start();
		
		System.out.println("Welcome to the Lab Höheweg");
		command();

		
	}

	public static String input() {
		
		String input = userInput.nextLine();
		return input.toLowerCase();
	}
	
	public static void command() throws FileNotFoundException
	{
		System.out.println("please enter command:");
		switch (input()) {
		case "read":
			msgs.clear();
			System.out.println("specify filename to read:");
			FileReader reader = new FileReader(input());
			Hl7InputStreamMessageIterator iterator = new Hl7InputStreamMessageIterator(reader);
			System.out.println("the following messages have been found:");
			int i = 1;

			while(iterator.hasNext())
			{
				Message msg = iterator.next();
				msgs.add(msg);
				System.out.println(i+". message is:");
				try {
					System.out.println(msg.encode());
				} catch (HL7Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}
			command();
		break;
		case "send":
			for(Message msg : msgs)
			{
			try {
				sender.send(new MessageSendable(msg));
			} catch (EncodingNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HL7Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			command();
		break;
		}
	}
}
