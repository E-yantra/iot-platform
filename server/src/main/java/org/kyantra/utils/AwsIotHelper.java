package org.kyantra.utils;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import org.kyantra.config.AWSCredsProvider;
import org.kyantra.dao.ConfigDAO;

public class AwsIotHelper {

    public static AWSIot getIotClient(){

        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();

        AWSIotClientBuilder clientBuilder = AWSIotClientBuilder.standard();
        clientBuilder.setCredentials(new AWSCredsProvider( new BasicAWSCredentials(awsKey,awsSecret)));
        clientBuilder.setRegion(Regions.AP_SOUTHEAST_1.getName());
        AWSIot client = clientBuilder.build();
        return client;
    }

    public static AWSIotMqttClient getMQTT(){
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        String endPoint = ConfigDAO.getInstance().get("endpoint").getValue();
        AWSIotMqttClient client = new AWSIotMqttClient(endPoint, "server", awsKey, awsSecret);
        return client;

    }
}
