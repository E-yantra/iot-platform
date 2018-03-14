package org.kyantra.resources;

import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.sns.model.CreateTopicResult;
import org.kyantra.aws.SNSHelper;
import org.kyantra.beans.SnsBean;
import org.kyantra.beans.SnsSubscriptionBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.SNSSubscriptionDAO;
import org.kyantra.dao.SnsDAO;
import org.kyantra.utils.AwsIotHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class SNSRuleResource extends BaseResource {

    @POST
    @Path("/createrule/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String createRule(@PathParam("id") Integer thingId,
                             @FormParam("topic") String topic) {

        SnsBean snsBean = new SnsBean();
        snsBean.setTopic(topic);

        CreateTopicResult createTopicResult = SNSHelper.getInstance().createTopic(snsBean);
        snsBean.setTopicARN(createTopicResult.getTopicArn());

        SnsDAO.getInstance().add(snsBean);

        String thingName = "thing" + thingId;
        SnsAction snsAction = new SnsAction();
        snsAction.withTargetArn(snsBean.getTopicARN())
                .withMessageFormat(MessageFormat.JSON)
                .withRoleArn(ConfigDAO.getInstance().get("IoTRoleARN").getValue());

        Action action = new Action().withSns(snsAction);
        List<Action> actionList = new ArrayList<>();
        actionList.add(action);

        TopicRulePayload rulePayload = new TopicRulePayload();
        rulePayload.withDescription("SNS rule for " + thingName)
                .withSql("SELECT * FROM '$aws/things/" + thingName + "/shadow/update'")
                .withRuleDisabled(false)
                .withAwsIotSqlVersion("2016-03-23")
                .withActions(actionList);

        CreateTopicRuleRequest topicRuleRequest = new CreateTopicRuleRequest();
        topicRuleRequest.withRuleName(thingName + "_sns")
                .withTopicRulePayload(rulePayload);

        AwsIotHelper.getIotClient().createTopicRule(topicRuleRequest);
        return "{\"success\":true}";
    }


    @GET
    @Path("/create/{topic}")
    @Produces(MediaType.TEXT_PLAIN)
    public String createSNSTopic(@PathParam("topic") String topic) {
        SnsBean snsBean = new SnsBean();
        snsBean.setTopic(topic);

        CreateTopicResult createTopicResult = SNSHelper.getInstance().createTopic(snsBean);
        snsBean.setTopicARN(createTopicResult.getTopicArn());

        SnsDAO.getInstance().add(snsBean);
        return "success";
    }

    @POST
    @Path("/subscribe/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String subscribeSNSTopic(@PathParam("id") Integer parentId,
                                    @FormParam("type") String type,
                                    @FormParam("value") String value) {
        SnsSubscriptionBean snsSubscriptionBean = new SnsSubscriptionBean();
        snsSubscriptionBean.setType(type);
        snsSubscriptionBean.setValue(value);
        snsSubscriptionBean.setParentSNSBean(SnsDAO.getInstance().get(parentId));
        SNSHelper.getInstance().subscibeTopic(snsSubscriptionBean);
        SNSSubscriptionDAO.getInstance().add(snsSubscriptionBean);
        return "success";
    }

}
