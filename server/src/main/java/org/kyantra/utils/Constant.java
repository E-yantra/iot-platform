package org.kyantra.utils;

public class Constant {

    // string
    public static final String CERT_ROOT = "/opt/aws-iot-certificates/";
    public static final String DEFAULT_POLICY = "mos-default";
    public static final String SNS_MESSAGE = "This is an ALERT. One of your set thresholds have crossed the limits." +
            " Login to IoT Platform for more details.";
    public static final String SNS_SUBJECT = "ALERT from e-Yantra IoT Platform";

    // integer
    public static final int SNS_INTERVAL = 15;

}
