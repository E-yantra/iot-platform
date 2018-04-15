package org.kyantra.aws;

public class DynamoDBHelper {

    private static DynamoDBHelper ddbHelper = new DynamoDBHelper();

    public static DynamoDBHelper getInstance() {
        return ddbHelper;
    }

}
