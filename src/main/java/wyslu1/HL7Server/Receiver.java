package wyslu1.HL7Server;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.hoh.api.IMessageHandler;
import ca.uhn.hl7v2.hoh.api.IReceivable;
import ca.uhn.hl7v2.hoh.api.IResponseSendable;
import ca.uhn.hl7v2.hoh.api.MessageProcessingException;
import ca.uhn.hl7v2.hoh.raw.api.RawSendable;
import ca.uhn.hl7v2.hoh.raw.server.HohRawServlet;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.builder.support.NoValidationBuilder;

public class Receiver extends HohRawServlet{

	public Receiver(){
		setMessageHandler(new MessageHandler());
		
	}
	
	private static class MessageHandler implements IMessageHandler<String>
	{

		public IResponseSendable<String> messageReceived(IReceivable<String> received) throws MessageProcessingException {
			String incomingRawMsg = received.getMessage();
			System.out.println("Received message: \n"+ incomingRawMsg);
		
			HapiContext ctx = new DefaultHapiContext();
			ctx.setValidationRuleBuilder(new NoValidationBuilder());
			PipeParser parser = new PipeParser();
			parser.setValidationContext(ctx.getValidationContext());

			Message message = null;
			Message ack = null;
			String response = null;
			try {
				message = parser.parse(incomingRawMsg);
				ack = message.generateACK();
				response = ack.encode();
			} catch (Exception e) {
				return new RawSendable("MSH|lol");
				
			} 
			return new RawSendable(response);
	            }
            
           
			
			
		}
		
	}
