//opg5pgm

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

public class MQTTprogram {
    public static MqttClient sampleClient;
    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        String broker = "tcp://192.168.1.1:1883";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            sampleClient = new MqttClient(broker,  MqttClient.generateClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.setCallback(new SimpleMqttCallBack());
            sampleClient.connect(connOpts);
//            publishMessage(sampleClient,"cmnd/grp7764/Power1","0");
            System.out.println("Connected");
            sampleClient.subscribe("tele/grp7764/SENSOR");


            Thread.sleep(200000);

            // put real stuff here        < -------- !!!!

            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();

        }
    }
    //opg6pgm
    public static void  publishMessage(MqttClient sampleClient,String topicsend,String content) throws MqttPersistenceException, MqttException {
        // Laver en publish pÂ sampleClient med topic topicsend og indhold content.
        MqttMessage message = new MqttMessage();
        message.setPayload(content.getBytes());
        System.out.println(content.getBytes());
        sampleClient.publish(topicsend, message);
        System.out.println("Message published");
    }

    public static void fugtighedskontrol(String s, MqttClient sampleClient) throws MqttException {
        JSONObject jo = new JSONObject(s);
        JSONObject jo2 = jo.getJSONObject("AM2301");
        int humi = (int) jo2.getDouble("Humidity");
        if (humi < 85.00){
            publishMessage(sampleClient,"cmnd/grp7764/Power1","0");
        } else
            publishMessage(sampleClient,"cmnd/grp7764/Power1","1");
    }


}

