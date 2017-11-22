package org.kyantra.client.beans;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

public class Message extends AWSIotMessage{

    public Message(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }


}
