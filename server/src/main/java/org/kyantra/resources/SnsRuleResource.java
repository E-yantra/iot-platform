package org.kyantra.resources;

import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.AddPermissionRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import io.swagger.annotations.Api;
import org.kyantra.aws.RuleHelper;
import org.kyantra.aws.SnsHelper;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.SnsBean;
import org.kyantra.beans.SnsSubscriptionBean;
import org.kyantra.dao.*;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.services.ValidatorService;
import org.kyantra.utils.AwsIotHelper;

import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.UUID;

@Api(value = "")
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
                         @FormParam("data") String data,
                         @FormParam("condition") String condition,
                         @FormParam("subject") String subject,
                         @FormParam("message") String message,
                         @FormParam("interval") Integer interval,
                         @FormParam("sns_topic") String snsTopic) {
        /*
         * Steps:
         * 1. create SnsBean
         * 2. create SNSAction in AWS (but actually lambda)
         * 3. create rule in AWS
         * 4. create RuleBean
         * 5. add rule to DB
         * 6. link rule and SNS in DB
         * */

        // create SnsBean
        SnsBean snsBean = new SnsBean();
        snsBean.setTopic(snsTopic);
        // set parameters or revert to default
        snsBean.setSubject(subject);
        snsBean.setMessage(message);
        snsBean.setInterval(interval);

        // create RuleBean
        RuleBean ruleBean = new RuleBean();
        ruleBean.setName(name);
        ruleBean.setDescription(description);
        ruleBean.setData(data);
        ruleBean.setCondition(condition);
        ruleBean.setType("SNS");
        ruleBean.setParentThing(ThingDAO.getInstance().get(parentThingId));


        Set<ConstraintViolation<RuleBean>> constraintViolations = ValidatorService.getValidator().validate(ruleBean);

        System.out.println(constraintViolations);

        // create SnsAction in AWS
        CreateTopicResult createTopicResult = SnsHelper.getInstance().createTopic(snsBean);
        snsBean.setTopicARN(createTopicResult.getTopicArn());

        try {
            // create rule in AWS
            CreateTopicRuleResult ruleResult = RuleHelper.getInstance().createTopicRule(ruleBean, snsBean);

            // get the rule from AWS with for its ARN
            String ruleArn = RuleHelper.getInstance().getTopicRule("thing" + parentThingId + "_sns_" + ruleBean.getName()).getRuleArn();

            // add trigger permission in lambda function (notificationService) for this rule
            AWSLambda lambda = AwsIotHelper.getAWSLambdaClient();
            String functionArn = ConfigDAO.getInstance().get("lambdaNotificationArn").getValue();
            lambda.addPermission(new AddPermissionRequest()
                    .withFunctionName(functionArn)
                    .withStatementId(UUID.randomUUID().toString())
                    .withPrincipal("iot.amazonaws.com")
                    .withSourceArn(ruleArn)
                    .withAction("lambda:InvokeFunction"));

            // add rule to DB
            RuleDAO.getInstance().add(ruleBean);

            // link rule and SNS in DB
            snsBean.setParentRule(ruleBean);
            SnsDAO.getInstance().add(snsBean);

            // Get updated ruleBean
            ruleBean = RuleDAO.getInstance().get(ruleBean.getId());

        } catch (Exception e) {
            e.printStackTrace();
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

    @PUT
    @Session
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL}, subjectType = "rule", subjectField = "parentId")
    public String update(@PathParam("id") Integer ruleId,
                         @FormParam("description") String description,
                         @FormParam("data") String data,
                         @FormParam("condition") String condition,
                         @FormParam("parentThing") Integer parentThingId) {

        // create RuleBean
        RuleBean ruleBean = RuleDAO.getInstance().get(ruleId);
        ruleBean.setDescription(description);
        ruleBean.setData(data);
        ruleBean.setCondition(condition);

        SnsBean snsBean = ruleBean.getSnsAction();

        // update rule in AWS
        RuleHelper.getInstance().replaceTopicRule(ruleBean, snsBean);

        // add rule to DB
        RuleDAO.getInstance().update(ruleBean);

        // link rule and SNS in DB
        snsBean.setParentRule(ruleBean);
        SnsDAO.getInstance().add(snsBean);

        // Get updated ruleBean
        ruleBean = RuleDAO.getInstance().get(ruleBean.getId());

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

    @DELETE
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

        SnsHelper.getInstance().subscibeTopic(snsSubscriptionBean);

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
