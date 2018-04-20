package org.kyantra.utils;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents;
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEventsClientBuilder;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iotdata.AWSIotData;
import com.amazonaws.services.iotdata.AWSIotDataClientBuilder;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.kyantra.config.AWSCredsProvider;
import org.kyantra.dao.ConfigDAO;

public class AwsIotHelper {

    public static AWSIot getIotClient() {
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();

        AWSIotClientBuilder clientBuilder = AWSIotClientBuilder.standard();
        clientBuilder.setCredentials(new AWSCredsProvider( new BasicAWSCredentials(awsKey,awsSecret)));
        clientBuilder.setRegion(Regions.AP_SOUTHEAST_1.getName());
        AWSIot client = clientBuilder.build();
        return client;
    }

    public static AmazonCloudWatchEvents getAmazonCloudWatchEvents() {
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        AmazonCloudWatchEventsClientBuilder clientBuilder = AmazonCloudWatchEventsClientBuilder.standard();
        clientBuilder.setCredentials(new AWSCredsProvider( new BasicAWSCredentials(awsKey,awsSecret)));
        clientBuilder.setRegion(Regions.AP_SOUTHEAST_1.getName());
        return clientBuilder.build();
    }

    public static AWSLambda getAWSLambdaClient() {
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        AWSLambdaClientBuilder clientBuilder = AWSLambdaClientBuilder.standard().standard();
        clientBuilder.setCredentials(new AWSCredsProvider( new BasicAWSCredentials(awsKey,awsSecret)));
        clientBuilder.setRegion(Regions.AP_SOUTHEAST_1.getName());
        return clientBuilder.build();
    }

    public static AWSIotData getIotDataClient() {

        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();

        AWSIotDataClientBuilder clientBuilder = AWSIotDataClientBuilder.standard();
        clientBuilder.setCredentials(new AWSCredsProvider( new BasicAWSCredentials(awsKey,awsSecret)));
        clientBuilder.setRegion(Regions.AP_SOUTHEAST_1.getName());
        AWSIotData client = clientBuilder.build();
        return client;
    }

    public static AWSIotMqttClient getMQTT() {
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        String endPoint = ConfigDAO.getInstance().get("endpoint").getValue();
        AWSIotMqttClient client = new AWSIotMqttClient(endPoint, "server", awsKey, awsSecret);
        return client;
    }

    public static AmazonSNS getAmazonSNSClient() {
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        AmazonSNSClientBuilder clientBuilder = AmazonSNSClientBuilder.standard();
        clientBuilder.setCredentials(new AWSCredsProvider(new BasicAWSCredentials(awsKey, awsSecret)));
        clientBuilder.setRegion(Regions.AP_SOUTHEAST_1.getName());
        return clientBuilder.build();
    }

    public static AmazonDynamoDB getAmazonDynamoDBClient() {
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        AmazonDynamoDBClientBuilder clientBuilder = AmazonDynamoDBClientBuilder.standard();
        clientBuilder.setCredentials(new AWSCredsProvider(new BasicAWSCredentials(awsKey, awsSecret)));
        clientBuilder.setRegion(Regions.AP_SOUTHEAST_1.getName());
        return clientBuilder.build();
    }

}
