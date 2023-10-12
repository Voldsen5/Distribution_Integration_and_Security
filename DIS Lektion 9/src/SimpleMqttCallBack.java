import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
public class SimpleMqttCallBack implements MqttCallback {
    int status = 0;
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String res= new String(mqttMessage.getPayload());
        System.out.println(res);
        JSONObject jo = new JSONObject(res);
        JSONObject jo2 = jo.getJSONObject("AM2301");
        int humi = (int) jo2.getDouble("Humidity");
        if (humi < 85.00){
            MQTTprogram.publishMessage(MQTTprogram.sampleClient,"cmnd/grp7764/Power1","0");
        } else
            MQTTprogram.publishMessage(MQTTprogram.sampleClient,"cmnd/grp7764/Power1","1");
    }
        // res indeholder en mÂling som et JSON-object
        // put real stuff here     < --------    !!!!!!!!!!

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
} 