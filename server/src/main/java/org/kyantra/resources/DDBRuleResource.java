package org.kyantra.resources;

import com.amazonaws.services.iot.model.*;
import org.kyantra.beans.ConfigBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.utils.AwsIotHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class DDBRuleResource extends BaseResource {

    @GET
    @Path("/create/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@PathParam("id") Integer id) {

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
        rulePayload.withDescription("DynamoDB rule for " + thingName)
                .withSql("SELECT * FROM '$aws/things/" + thingName + "/shadow/update'")
                .withRuleDisabled(true)
                .withAwsIotSqlVersion("2016-03-23")
                .withActions(actionList);

        CreateTopicRuleRequest topicRuleRequest = new CreateTopicRuleRequest();
        topicRuleRequest.withRuleName(thingName)
                .withTopicRulePayload(rulePayload);

        AwsIotHelper.getIotClient().createTopicRule(topicRuleRequest);
        return "{\"success\":true}";

    }

    @POST
    @Path("/enable/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    // TODO: Provide appropriate output
    @Produces(MediaType.TEXT_PLAIN)
    public String enable(@PathParam("id") Integer id,
                         @FormParam("enable") Boolean enable) {
        String ruleName = "thing" + id;
        if(enable.equals(true)) {
            EnableTopicRuleRequest topicRuleRequest = new EnableTopicRuleRequest();
            topicRuleRequest.withRuleName(ruleName);
            AwsIotHelper.getIotClient().enableTopicRule(topicRuleRequest);
        } else {
            DisableTopicRuleRequest topicRuleRequest = new DisableTopicRuleRequest();
            topicRuleRequest.withRuleName(ruleName);
            AwsIotHelper.getIotClient().disableTopicRule(topicRuleRequest);
        }

        ThingDAO.getInstance().setStorageEnabled(id, enable);

        return "{\"success\":true}";
    }

}
