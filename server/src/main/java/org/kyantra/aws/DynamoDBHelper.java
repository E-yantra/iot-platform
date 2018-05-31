package org.kyantra.aws;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.util.HashMap;

public class DynamoDBHelper {

    private static DynamoDBHelper ddbHelper = new DynamoDBHelper();

    public static DynamoDBHelper getInstance() {
        return ddbHelper;
    }

//    public putItem(HashMap<String, AttributeValue>) {
//        HashMap<String, AttributeValue> item_value = new HashMap<String, AttributeValue>();
//
//    }

}
