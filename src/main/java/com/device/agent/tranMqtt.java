package com.device.agent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;



public class tranMqtt implements MqttCallback {
	


	private MqttClient 			subClient;
	private MqttClient 			pubClient;
	private String 				subBrokerUrl;
	private String 				pubBrokerUrl;
	private MqttConnectOptions 	conOpt;

	public tranMqtt(String subBrokerUrl, String pubBrokerUrl) {
		super();
		this.subBrokerUrl = subBrokerUrl;
		this.pubBrokerUrl = pubBrokerUrl;
	}


    public void createMqttClient(MqttDefaultFilePersistence dataStore ) throws MqttException {
   	
    	//This sample stores in a temporary directory... where messages temporarily
    	// stored until the message has been delivered to the server.
    	//..a real application ought to store them somewhere
    	// where they are not likely to get deleted or tampered with


    	try {
    		// Construct the connection options object that contains connection parameters
    		// such as cleanSession and LWT
	    	conOpt = new MqttConnectOptions();
/*	    	conOpt.setCleanSession(clean);
	    	if(password != null ) {
	    	  conOpt.setPassword(this.password.toCharArray());
	    	}
	    	if(userName != null) {
	    	  conOpt.setUserName(this.userName);
	    	}
*/
    		// Construct an MQTT blocking mode client
	    	
	    	subClient = new MqttClient(this.subBrokerUrl,"sub1", dataStore);
	    	pubClient = new MqttClient(this.pubBrokerUrl,"pub1");
			// Set this wrapper as the callback handler
	    	subClient.setCallback(this);
	    	//pubClient.setCallback(this);
	    	pubClient.connect();
	    	subClient.connect(conOpt);
	    	log("Connected to "+subBrokerUrl+" with client ID "+subClient.getClientId());	    	
	    	log("Connected to "+pubBrokerUrl+" with client ID "+pubClient.getClientId());	
		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: "+e.toString());
			System.exit(1);
		}
    }	
	
	
    public void myPublish(String topicName, int qos, byte[] payload) throws MqttException {


    	// Create and configure a message
   		MqttMessage message = new MqttMessage(payload);
    	message.setQos(qos);

    	// Send the message to the server, control is not returned until
    	// it has been delivered to the server meeting the specified
    	// quality of service.
    	pubClient.publish(topicName, message);

    }

    /**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     * @param topicName to subscribe to (can be wild carded)
     * @param qos the maximum quality of service to receive messages at for this subscription
     * @throws MqttException
     */
    public void mySubscribe(String topicName, int qos) throws MqttException {

    	// Connect to the MQTT server


    	// Subscribe to the requested topic
    	// The QoS specified is the maximum level that messages will be sent to the client at.
    	// For instance if QoS 1 is specified, any messages originally published at QoS 2 will
    	// be downgraded to 1 when delivering to the client but messages published at 1 and 0
    	// will be received at the same level they were published at.
    	log("Subscribing to topic \""+topicName+"\" qos "+qos);
    	subClient.subscribe(topicName, qos);
   // 	subClient.disconnect();

    }

    /**
     * Utility method to handle logging. If 'quietMode' is set, this method does nothing
     * @param message the message to log
     */
    private void log(String message) {
    		System.out.println(message);
    }




	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("  Topic:\t" + topic +
                "  Message:\t" + new String(message.getPayload()) +
                "  QoS:\t" + message.getQos());		
		
		//recieve the message, tran to the server
		myPublish(topic,message.getQos(), message.getPayload());
		
		
		
		
	}

}
