package org.kyantra.client.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import org.kyantra.client.beans.Message;

public class MQTTService {

    private static MQTTService instance;

    String endPoint;
    String key;
    String secret;
    String clientId;
    AWSIotMqttClient client;
    int timeout = 5000;

    public MQTTService(String clientId, String endPoint, String key, String secret) {
        this.clientId = clientId;
        this.endPoint = endPoint;
        this.key = key;
        this.secret = secret;
        client = new AWSIotMqttClient(endPoint, clientId, key, secret);
    }

    public void start() throws AWSIotException {
        client.connect();
    }

    public void stop() throws AWSIotException{
        client.disconnect();
    }

    public void publish(Message msg) throws AWSIotException {
        client.publish(msg,timeout);
    }

    public static MQTTService getInstance(String clientId, String endPoint, String key, String secret){
        if(instance!=null)
            return instance;

        instance = new MQTTService(clientId,endPoint,key,secret);
        return instance;
    }
}
