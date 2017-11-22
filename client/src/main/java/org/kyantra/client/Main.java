package org.kyantra.client;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotQos;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.kyantra.client.beans.Message;
import org.kyantra.client.service.MQTTService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    public static void main(String[] args) throws ParseException, AWSIotException {

        Options options = new Options();
        options.addOption("awsKey", true, "AWS Key");
        options.addOption("awsSecret", true, "AWS Secret");
        options.addOption("endpoint",true,"Endpoint");
        options.addOption("clientId", true, "Client Id");
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse( options, args);

        MQTTService mService = MQTTService.getInstance(cmd.getOptionValue("clientId"),
                cmd.getOptionValue("endpoint"),
                cmd.getOptionValue("awsKey"),
                cmd.getOptionValue("awsSecret"));
        mService.start();

        executorService.scheduleWithFixedDelay(()->{
            try {
                mService.publish(new Message("/request", AWSIotQos.QOS0, "Hello World"));
            } catch (AWSIotException e) {
                e.printStackTrace();
            }

        },1000, 1000, TimeUnit.MILLISECONDS);


    }
}
