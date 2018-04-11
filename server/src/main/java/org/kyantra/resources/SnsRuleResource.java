package org.kyantra.resources;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.sns.model.CreateTopicResult;
import org.kyantra.aws.RuleHelper;
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

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SnsRuleResource extends BaseResource {

    /* TODO: Atomicity with AWS and DB
     * For transactions, like if DB transactions don't commit properly rollback the AWS operations too*/

    @POST
    @Session
    @Path("/create/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL}, subjectType = "rule", subjectField = "parentId")
    public String create(@PathParam("id") Integer parentThingId,
                         @FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("topic") String topic,
                         @FormParam("data") String data,
                         @FormParam("condition") String condition,
                         @FormParam("sns_topic") String snsTopic) {
        /*
         * Steps:
         * 1. create SnsBean
         * 2. create SNSAction in AWS
         * 3. create rule in AWS
         * 4. create RuleBean
         * 5. add rule to DB
         * 6. link rule and SNS in DB
         * */

//        String ruleCondition = " WHERE ";
//
//        //constructed names of entities
//        String thingName = "thing" + parentThingId;
//        String ruleName = name;
//
//        System.out.println(condition);
//        if(!condition.equals(""))
//            ruleCondition = ruleCondition + condition;
//        else
//            ruleCondition = condition;
        // create SnsBean
        SnsBean snsBean = new SnsBean();
        snsBean.setTopic(snsTopic);

        // create RuleBean
        RuleBean ruleBean = new RuleBean();
        ruleBean.setName(name);
        ruleBean.setDescription(description);
        ruleBean.setTopic(topic);
        ruleBean.setData(data);
        ruleBean.setCondition(condition);
        ruleBean.setType("sns");
        ruleBean.setParentThing(ThingDAO.getInstance().get(parentThingId));

        // create SNSAction in AWS
        CreateTopicResult createTopicResult = SNSHelper.getInstance().createTopic(snsBean);
        snsBean.setTopicARN(createTopicResult.getTopicArn());


        // create rule in AWS
//        SnsAction snsAction = new SnsAction();
//        snsAction.withTargetArn(snsBean.getTopicARN())
//                .withMessageFormat(MessageFormat.RAW)
//                .withRoleArn(ConfigDAO.getInstance().get("IoTRoleARN").getValue());
//
//        Action action = new Action().withSns(snsAction);
//        List<Action> actionList = new ArrayList<>();
//        actionList.add(action);
//
//        TopicRulePayload rulePayload = new TopicRulePayload();
//        rulePayload.withDescription(description)
//                .withSql("SELECT " + data + " FROM '$aws/things/" + thingName + "/shadow/update'" + ruleCondition)
//                .withRuleDisabled(false)
//                .withAwsIotSqlVersion("2016-03-23")
//                .withActions(actionList);
//
//        CreateTopicRuleRequest topicRuleRequest = new CreateTopicRuleRequest();
//        topicRuleRequest.withRuleName(thingName + "_sns_" + ruleName)
//                .withTopicRulePayload(rulePayload);
//
//        AwsIotHelper.getIotClient().createTopicRule(topicRuleRequest);

        // create rule in AWS
        try {
            RuleHelper.getInstance().createTopicRule(ruleBean, "sns", snsBean);

            // add rule to DB
            RuleDAO.getInstance().add(ruleBean);

            // link rule and SNS in DB
            snsBean.setParentRule(ruleBean);
            SnsDAO.getInstance().add(snsBean);

            // Get updated ruleBean
            ruleBean = RuleDAO.getInstance().get(ruleBean.getId());
        } catch (Exception e) {
            return "{\"success\": false}";
        }
        return gson.toJson(ruleBean);
    }

    @GET
    @Session
    @Path("/get/{id}")
    @Secure(roles = {RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer ruleId) {
        RuleBean ruleBean = RuleDAO.getInstance().get(ruleId);
        return gson.toJson(ruleBean);
    }

    @POST
    @Session
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL}, subjectType = "rule", subjectField = "parentId")
    public String update(@PathParam("id") Integer ruleId,
                         @FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("topic") String topic,
                         @FormParam("data") String data,
                         @FormParam("condition") String condition,
                         @FormParam("parentThing") Integer parentThingId) {

        // create RuleBean
        RuleBean ruleBean = RuleDAO.getInstance().get(ruleId);
        ruleBean.setName(name);
        ruleBean.setDescription(description);
//        ruleBean.setTopic(topic);
        ruleBean.setData(data);
        ruleBean.setCondition(condition);
//        ruleBean.setType("sns");
//        ruleBean.setParentThing(ThingDAO.getInstance().get(parentThingId));

        RuleHelper.getInstance().replaceTopicRule(ruleBean, "sns", ruleBean.getSnsAction());
        return gson.toJson(ruleBean);
    }

    /*TODO: Delete SNS topics when there is no rule in AWS pointing to it*/
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Session()
    @Secure(roles = {RoleEnum.ALL})
    public String delete(@PathParam("id") Integer ruleId) {
        /*
         * Steps:
         * 1. Get ruleBean
         * 2. delete rule in AWS
         * 3. delete rule in DB which should also delete entries from SNS and SNSSubscriptions
         * */

        // get ruleBean
        RuleBean ruleBean = RuleDAO.getInstance().get(ruleId);

        // delete rule in AWS
        DeleteTopicRuleResult deleteTopicRuleResult = RuleHelper.getInstance().deleteRule(ruleBean);

        // delete rule bean which should also delete entries from SNS and SNSSubscriptions
        RuleDAO.getInstance().delete(ruleId);
        return "{\"success\": true}";
    }

    @POST
    @Session
    @Path("/delete/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL})
    public String deleteByName(@NotNull @FormParam("name") String ruleName,
                               @NotNull @FormParam("parentThing") Integer Id) {
        /*
         * Steps:
         * 1. Get ruleBean
         * 2. delete rule in AWS
         * 3. delete rule in DB which should also delete entries from SNS and SNSSubscriptions
         * */

        // get ruleBean
        RuleBean ruleBean = RuleDAO.getInstance().getByName(ruleName);

        // delete rule in AWS
        DeleteTopicRuleResult deleteTopicRuleResult = RuleHelper.getInstance().deleteRule(ruleBean);

        // delete rule bean which should also delete entries from SNS and SNSSubscriptions
        RuleDAO.getInstance().deleteByName(ruleName);
        return "{\"success\": true}";
    }


    @POST
    @Session
    @Secure(roles = {RoleEnum.ALL})
    @Path("/subscribe/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String subscribeSNSTopic(@PathParam("id") Integer snsId,
                                    @FormParam("type") String type,
                                    @FormParam("value") String value) {
        SnsSubscriptionBean snsSubscriptionBean = new SnsSubscriptionBean();
        snsSubscriptionBean.setType(type);
        snsSubscriptionBean.setValue(value);
        snsSubscriptionBean.setParentSNSBean(SnsDAO.getInstance().get(snsId));

        SNSHelper.getInstance().subscibeTopic(snsSubscriptionBean);

        SnsSubscriptionDAO.getInstance().add(snsSubscriptionBean);

        return gson.toJson(snsSubscriptionBean);
    }

    @GET
    @Path("/thing/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Session
    @Secure(roles = {RoleEnum.ALL})
    public String getByThing(@PathParam("id") Integer parentThingId) {
        Set<RuleBean> ruleBean = RuleDAO.getInstance().getByThing(parentThingId);
        return gson.toJson(ruleBean);
    }
}
