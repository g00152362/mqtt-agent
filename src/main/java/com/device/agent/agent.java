package com.device.agent;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class agent {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
			// Default settings://
		//	boolean quietMode 	= false;
//			String topic 		= "";

			int qos 			=1;
			
	// RELEASE
			String inbroker 	= "tcp://127.0.0.1:1883";
			String exBroker     ="tcp://10.0.0.200:1883";
			
	///////////
	//DEBUG		
//			String inbroker 		= "tcp://192.168.100.253:1883";
//			String exBroker     ="tcp://127.0.0.1:1883";
			
//			int port 			= 1883;
//			String clientId 	= null;
//			boolean cleanSession = true;			// Non durable subscriptions

//			String password = null;
//			String userName = null;
			if (args.length == 1){
				exBroker = args[0];
			}
				


			try {
				// start client to connect the device API mqtt server	and  extern server	
				
		    	String tmpDir = System.getProperty("java.io.tmpdir");
		    	MqttDefaultFilePersistence dataStore1 = new MqttDefaultFilePersistence(tmpDir);
		    	MqttDefaultFilePersistence dataStore2 = new MqttDefaultFilePersistence(tmpDir);
				tranMqtt tran = new tranMqtt(inbroker, exBroker);
				tran.createMqttClient(dataStore1);
				
				// Perform the requested action

				tran.mySubscribe("gateway/#",qos);
	//			tran.subscribe("gateway/cellularInfo",qos);
			//	tran.subscribe("gateway/upsInfo",qos);		
				
//				tranMqtt tran1 = new tranMqtt(inbroker, exBroker);
//				tran1.createMqttClient(dataStore2);
////					String tt ="12345678";
//						tran1.myPublish("www/123",2,tt.getBytes());

			} catch(MqttException me) {
				// Display full details of any exception that occurs
				System.out.println("reason "+me.getReasonCode());
				System.out.println("msg "+me.getMessage());
				System.out.println("loc "+me.getLocalizedMessage());
				System.out.println("cause "+me.getCause());
				System.out.println("excep "+me);
				me.printStackTrace();
			}
		}			
}
