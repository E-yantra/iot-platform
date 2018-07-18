package org.kyantra.resources;

import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.AddPermissionRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import io.swagger.annotations.Api;
import org.kyantra.aws.RuleHelper;
import org.kyantra.aws.SnsHelper;
import org.kyantra.beans.*;
import org.kyantra.dao.*;
import org.kyantra.exceptionhandling.DataNotFoundException;
import org.kyantra.exceptionhandling.ExceptionMessage;
import org.kyantra.helper.AuthorizationHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.services.ValidatorService;
import org.kyantra.utils.AwsIotHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Set;
import java.util.UUID;

@Api(value = "")
public class SnsRuleResource extends BaseResource {

    /* TODO: Atomicity with AWS and DB
     * For transactions, like if DB transactions don't commit properly rollback the AWS operations too*/

    // TODO: 5/25/18 Change id path-param to form-param

    public SnsRuleResource(SecurityContext sc, HttpServletRequest request) {
        super(sc, request);
    }

    @POST
    @Path("/create/{id}")
    @Session
    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL}, subjectType = "rule", subjectField = "parentId")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
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

        ThingBean targetThing = ThingDAO.getInstance().get(parentThingId);
        UserBean user = (UserBean) getSecurityContext().getUserPrincipal();

        if (targetThing == null) 
            throw  new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        
        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
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
                throw e;
            }
            return gson.toJson(ruleBean);
        }
        else throw new ForbiddenException(ExceptionMessage.FORBIDDEN);
    }


    @GET
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer ruleId) {
        RuleBean ruleBean = RuleDAO.getInstance().get(ruleId);

        ThingBean targetThing = ruleBean.getParentThing();
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetThing == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (!AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            return gson.toJson(ruleBean);
        }
        else throw new ForbiddenException(ExceptionMessage.FORBIDDEN);
    }


    @PUT
    @Path("/update/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL}, subjectType = "rule", subjectField = "parentId")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@PathParam("id") Integer ruleId,
                         @FormParam("description") String description,
                         @FormParam("data") String data,
                         @FormParam("condition") String condition,
                         @FormParam("parentThing") Integer parentThingId) {

        // create RuleBean
        RuleBean ruleBean = RuleDAO.getInstance().get(ruleId);

        ThingBean targetThing = ruleBean.getParentThing();
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetThing == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
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
        else throw new ForbiddenException(ExceptionMessage.FORBIDDEN);
    }


    /*TODO: Delete SNS topics when there is no rule in AWS pointing to it*/
    @DELETE
    @Path("/delete/{id}")
    @Session
    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer ruleId) {
        /*
         * Steps:
         * 1. Get ruleBean
         * 2. delete rule in AWS
         * 3. delete rule in DB which should also delete entries from SNS and SNSSubscriptions
         * */

        // get ruleBean
        RuleBean ruleBean = RuleDAO.getInstance().get(ruleId);

        ThingBean targetThing = ruleBean.getParentThing();
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetThing == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            // delete rule in AWS
            DeleteTopicRuleResult deleteTopicRuleResult = RuleHelper.getInstance().deleteRule(ruleBean);

            // delete rule bean which should also delete entries from SNS and SNSSubscriptions
            RuleDAO.getInstance().delete(ruleId);
            return "{\"success\": true}";
        }
        else throw new ForbiddenException(ExceptionMessage.FORBIDDEN);
    }


    @DELETE
    @Path("/delete/")
    @Session
    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
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

        ThingBean targetThing = ruleBean.getParentThing();
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetThing == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            // delete rule in AWS
            DeleteTopicRuleResult deleteTopicRuleResult = RuleHelper.getInstance().deleteRule(ruleBean);

            // delete rule bean which should also delete entries from SNS and SNSSubscriptions
            RuleDAO.getInstance().deleteByName(ruleName);
            return "{\"success\": true}";
        }
        else throw new ForbiddenException(ExceptionMessage.FORBIDDEN);
    }


    // TODO: 5/25/18 Add authorization here
    @POST
    @Path("/subscribe/{id}")
    @Session
    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL})
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
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    public String getByThing(@PathParam("id") Integer parentThingId) {
        ThingBean targetThing = ThingDAO.getInstance().get(parentThingId);
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetThing == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if(AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            Set<RuleBean> ruleBean = RuleDAO.getInstance().getByThing(parentThingId);
            return gson.toJson(ruleBean);
        }
        else throw new ForbiddenException(ExceptionMessage.FORBIDDEN);
    }
}
