package org.kyantra.resources;

import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.sns.model.CreateTopicResult;
import org.kyantra.aws.SNSHelper;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.SnsBean;
import org.kyantra.beans.SnsSubscriptionBean;
import org.kyantra.dao.*;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.AwsIotHelper;
import org.springframework.context.annotation.Role;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SNSRuleResource extends BaseResource {

    // TODO: Atomicity with AWS and DB
    @POST
    @Session
    @Path("/create/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "rule", subjectField = "parentId")
    public String createRule(@PathParam("id") Integer thingId,
                             @FormParam("name") String ruleName,
                             @FormParam("description") String description,
                             @FormParam("topic") String topic,
                             @FormParam("data") String data,
                             @FormParam("condition") String condition,
                             @FormParam("sns_topic") String snsTopic) {

        /*
         Steps:
         1. create SnsBean
         2. create SNSAction in AWS
         3. create rule in AWS
         4. create RuleBean
         5. add rule to DB
         6. link rule and SNS in DB
        */

        // create SnsBean
        SnsBean snsBean = new SnsBean();
        snsBean.setTopic(snsTopic);

        // create SNSAction in AWS
        CreateTopicResult createTopicResult = SNSHelper.getInstance().createTopic(snsBean);
        snsBean.setTopicARN(createTopicResult.getTopicArn());

//        SnsDAO.getInstance().add(snsBean);

        String thingName = "thing" + thingId;

        // create rule in AWS
        SnsAction snsAction = new SnsAction();
        snsAction.withTargetArn(snsBean.getTopicARN())
                .withMessageFormat(MessageFormat.JSON)
                .withRoleArn(ConfigDAO.getInstance().get("IoTRoleARN").getValue());

        Action action = new Action().withSns(snsAction);
        List<Action> actionList = new ArrayList<>();
        actionList.add(action);

        TopicRulePayload rulePayload = new TopicRulePayload();
        rulePayload.withDescription(description)
                .withSql("SELECT " + data + " FROM '$aws/things/" + thingName + "/shadow/update'")
                .withRuleDisabled(false)
                .withAwsIotSqlVersion("2016-03-23")
                .withActions(actionList);

        CreateTopicRuleRequest topicRuleRequest = new CreateTopicRuleRequest();
        topicRuleRequest.withRuleName(thingName + "_sns_" + ruleName)
                .withTopicRulePayload(rulePayload);

        AwsIotHelper.getIotClient().createTopicRule(topicRuleRequest);

        // add rule to DB
        RuleBean ruleBean = new RuleBean();
        ruleBean.setName(ruleName);
        ruleBean.setDescription(description);
        ruleBean.setTopic(topic);
        ruleBean.setData(data);
        ruleBean.setCondition(condition);
        ruleBean.setParentThing(ThingDAO.getInstance().get(thingId));

        RuleDAO.getInstance().add(ruleBean);

        // link rule and SNS in DB
        snsBean.setParentRule(ruleBean);
        SnsDAO.getInstance().add(snsBean);

        return "{\"success\":true}";
    }

    @GET
    @Session
    @Path("/thing/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByThing(@PathParam("id") Integer thingId) {
        Set<RuleBean> ruleBean = RuleDAO.getInstance().getByThing(thingId);
        return gson.toJson(ruleBean);
    }
}
