package org.kyantra.resources;

import com.amazonaws.services.iot.model.*;
import io.swagger.annotations.Api;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.exceptionhandling.AccessDeniedException;
import org.kyantra.helper.AuthorizationHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.AwsIotHelper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Api(value = "")
public class DDBRuleResource extends BaseResource {

    public DDBRuleResource(SecurityContext sc, HttpServletRequest request) {
        super(sc, request);
    }

    @POST
    @Path("/create/{id}")
    @Session
    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@PathParam("id") Integer id) throws AccessDeniedException {
        ThingBean targetThing = ThingDAO.getInstance().get(id);
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();
        if(AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
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
        else throw new AccessDeniedException();
    }


    @POST
    @Path("/enable/{id}")
    @Session
    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String enable(@PathParam("id") Integer id,
                         @FormParam("enable") Boolean enable) throws AccessDeniedException {
        ThingBean targetThing = ThingDAO.getInstance().get(id);
        UserBean user = (UserBean) getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            String ruleName = "thing" + id;
            if (enable.equals(true)) {
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
        else throw new AccessDeniedException();
    }
}
