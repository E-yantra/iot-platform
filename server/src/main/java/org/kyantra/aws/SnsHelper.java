package org.kyantra.aws;

import com.amazonaws.services.iot.model.Action;
import com.amazonaws.services.iot.model.TopicRulePayload;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.SnsBean;
import org.kyantra.beans.SnsSubscriptionBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.utils.AwsIotHelper;

import java.util.ArrayList;
import java.util.List;

public class SnsHelper {

    private static SnsHelper instance = new SnsHelper();

    public static SnsHelper getInstance() {
        return instance;
    }

    public CreateTopicResult createTopic(SnsBean snsBean) {
        AmazonSNS amazonSNSClient = AwsIotHelper.getAmazonSNSClient();
        CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsBean.getTopic());
        //TODO: Handle thrown exceptions
        CreateTopicResult createTopicResult = amazonSNSClient.createTopic(createTopicRequest);
        return createTopicResult;
    }


    public SubscribeResult subscibeTopic(SnsSubscriptionBean subscriptionBean) {
        String topicARN = subscriptionBean.getParentSNSBean().getTopicARN();
        SubscribeRequest request = new SubscribeRequest(
                topicARN,
                subscriptionBean.getType(),
                subscriptionBean.getValue()
        );
        SubscribeResult subscribeResult= AwsIotHelper.getAmazonSNSClient().subscribe(request);
        return subscribeResult;
    }

    public TopicRulePayload createSnsRulePayload(RuleBean ruleBean, SnsBean snsBean, String suffix) {

        // set up for rules
        String ruleCondition = " WHERE ";
        System.out.println(ruleBean.getCondition());
        if (!ruleBean.getCondition().equals(""))
            ruleCondition = ruleCondition + ruleBean.getCondition();
        else
            ruleCondition = ruleBean.getCondition();

        // create rule payload: {lambda function that uses SNS}
        Action action = ActionHelper.getInstance()
                .createLambdaAction(ConfigDAO.getInstance().get("lambdaNotificationArn").getValue());
        List<Action> actionList = new ArrayList<>();
        actionList.add(action);


        // 3. create rulePayload
        TopicRulePayload rulePayload = new TopicRulePayload();
        rulePayload.withDescription(ruleBean.getDescription())
                .withSql("SELECT " + ruleBean.getData() + suffix
                        + " FROM '$aws/things/thing"
                        + ruleBean.getParentThing().getId()
                        + "/shadow/update'" + ruleCondition)
                .withRuleDisabled(false)
                .withAwsIotSqlVersion("2016-03-23")
                .withActions(actionList);

        return rulePayload;
    }
}
