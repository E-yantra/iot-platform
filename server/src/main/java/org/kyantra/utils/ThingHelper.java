package org.kyantra.utils;

/*Helper function for thing resource
 * Contains functions for performing operation on AWS such as generating
 * certificates, attaching policies, etc.*/

import com.amazonaws.services.iot.model.*;
import org.kyantra.dao.ConfigDAO;

import java.util.ArrayList;
import java.util.List;

public class ThingHelper {

    public static ThingHelper thingHelper = new ThingHelper();

    public static ThingHelper getThingHelper() {
        return thingHelper;
    }

    public String[] generateCertificatePair() {

        //Generate certificates as strings
        CreateKeysAndCertificateRequest certificateRequest = new CreateKeysAndCertificateRequest();
        certificateRequest.withSetAsActive(true);
        CreateKeysAndCertificateResult certificateResult = AwsIotHelper.getIotClient().createKeysAndCertificate(certificateRequest);

        String[] certificateList = new String[3];
        certificateList[0] = certificateResult.getCertificatePem();
        certificateList[1] = certificateResult.getKeyPair().getPrivateKey();
        certificateList[2] = certificateResult.getKeyPair().getPublicKey();

        return certificateList;
    }

    public void createStorageRule(Integer id) {
        String thingName = "thing" + id;

        // create actions
        DynamoDBAction dynamoDBAction = new DynamoDBAction();
        dynamoDBAction.withTableName("ThingDB")
                .withHashKeyField("id")
                .withHashKeyType("STRING")
                .withHashKeyValue("${topic()}")
                .withRangeKeyField("timestamp")
                .withRangeKeyType("NUMBER")
                .withRangeKeyValue("${timestamp()}")
                .withRoleArn(ConfigDAO.getInstance().get("iotRoleArn").getValue());

        // add actions as required
        Action action = new Action().withDynamoDB(dynamoDBAction);
        List<Action> actionList = new ArrayList<>();
        actionList.add(action);

        TopicRulePayload rulePayload = new TopicRulePayload();
        rulePayload.withDescription("Rule for " + thingName)
                .withSql("SELECT * FROM '$aws/things/" + thingName + "/shadow/update'")
                .withRuleDisabled(true)
                .withAwsIotSqlVersion("2016-03-23")
                .withActions(actionList);

        CreateTopicRuleRequest topicRuleRequest = new CreateTopicRuleRequest();
        topicRuleRequest.withRuleName(thingName)
                .withTopicRulePayload(rulePayload);

        AwsIotHelper.getIotClient().createTopicRule(topicRuleRequest);
    }

}