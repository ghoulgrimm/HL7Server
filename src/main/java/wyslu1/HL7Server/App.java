package wyslu1.HL7Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;

public class App{
	
	private static Scanner userInput = new Scanner(System.in);
	private static Sender sender;
	private static Receiver receiver = new Receiver();
	private static ReceiverServer receiverServer = new ReceiverServer(8080, "HL7","incoming", receiver );
	
	
	public static void main(String[] args) throws Exception {
		sender = new Sender("localhost", 8080, "HL7/incoming");
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
			System.out.println("specify filename to read:");
			FileReader reader = new FileReader(input());
			Hl7InputStreamMessageIterator iterator = new Hl7InputStreamMessageIterator(reader);
			System.out.println("the following messages have been found:");
			int i = 1;
			while(iterator.hasNext())
			{
				System.out.println(i+". message is:");
				System.out.println(iterator.next());
				i++;
			}
			command();
		break;
		case "send":
			
			command();
		break;
		}
	}
}
