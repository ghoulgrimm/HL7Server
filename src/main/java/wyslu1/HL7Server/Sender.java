package wyslu1.HL7Server;

import java.io.IOException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.hoh.api.DecodeException;
import ca.uhn.hl7v2.hoh.api.EncodeException;
import ca.uhn.hl7v2.hoh.api.IAuthorizationClientCallback;
import ca.uhn.hl7v2.hoh.api.IReceivable;
import ca.uhn.hl7v2.hoh.api.ISendable;
import ca.uhn.hl7v2.hoh.api.MessageMetadataKeys;
import ca.uhn.hl7v2.hoh.auth.SingleCredentialClientCallback;
import ca.uhn.hl7v2.hoh.hapi.api.MessageSendable;
import ca.uhn.hl7v2.hoh.hapi.client.HohClientSimple;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.datatype.NM;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class Sender {
	
	public Sender() throws HL7Exception, IOException{
		
		/*
		 * http://localhost:8080/AppContext
		 */
		String host = "localhost";
		int port = 8080;
		String uri = "/Hl7Listener/Incoming";

		// Create a parser
		Parser parser = PipeParser.getInstanceWithNoValidation();

		// Create a client
		HohClientSimple client = new HohClientSimple(host, port, uri, parser);

		// Optionally, if credentials should be sent, they 
		// can be provided using a credential callback
//		IAuthorizationClientCallback authCalback = new SingleCredentialClientCallback("ausername", "somepassword");
//		client.setAuthorizationCallback(authCalback);
		// The ISendable defines the object that provides the actual
				// message to send
				ORU_R01 oru = new ORU_R01();
				oru.initQuickstart("ORU", "R01", "T");

				ORU_R01_ORDER_OBSERVATION orderObservation = oru.getPATIENT_RESULT().getORDER_OBSERVATION();

				// Populate the OBR
				OBR obr = orderObservation.getOBR();
				obr.getSetIDOBR().setValue("1");
				obr.getFillerOrderNumber().getEntityIdentifier().setValue("1234");
				obr.getFillerOrderNumber().getNamespaceID().setValue("LAB");
				obr.getUniversalServiceIdentifier().getIdentifier().setValue("88304");

				ORU_R01_OBSERVATION observation = orderObservation.getOBSERVATION(0);
				OBX obx = observation.getOBX();
				obx.getSetIDOBX().setValue("1");
				obx.getObservationIdentifier().getIdentifier().setValue("88304");
				obx.getObservationSubID().setValue("1");

				obx.getValueType().setValue("NM");
				NM nm = new NM(oru);
				nm.setValue("CREATININE");
				// Varies value = obx.getObservationValue(0);
				// value.setData(nm);
				System.out.println(oru.encode());
				// .. set other values on the message ..

				// The MessageSendable provides the message to send
				ISendable sendable = new MessageSendable(oru);

		try {
		        // sendAndReceive actually sends the message
		        IReceivable<Message> receivable = client.sendAndReceiveMessage(sendable);
		        
		        // receivavle.getRawMessage() provides the response
		        Message message = receivable.getMessage();
		        System.out.println("Response was:\n" + message.encode());
		        
		        // IReceivable also stores metadata about the message
		        String remoteHostIp = (String) receivable.getMetadata().get(MessageMetadataKeys.REMOTE_HOST_ADDRESS);
		        System.out.println("From:\n" + remoteHostIp);
		        
		        /*
		         * Note that the client may be reused as many times as you like,
		         * by calling sendAndReceiveMessage repeatedly
		         */
		        
		} catch (DecodeException e) {
		        // Thrown if the response can't be read
		        e.printStackTrace();
		} catch (IOException e) {
		        // Thrown if communication fails
		        e.printStackTrace();
		} catch (EncodeException e) {
		        // Thrown if the message can't be encoded (generally a programming bug)
		        e.printStackTrace();
		}	
		
	}
}
