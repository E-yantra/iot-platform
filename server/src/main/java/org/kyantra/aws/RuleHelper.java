package org.kyantra.aws;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicResult;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.SnsBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.RuleDAO;
import org.kyantra.utils.AwsIotHelper;

import java.util.ArrayList;
import java.util.List;

public class RuleHelper {

    public static RuleHelper instance = new RuleHelper();

    public static RuleHelper getInstance() {
        return instance;
    }


    public CreateTopicRuleResult createTopicRule(RuleBean ruleBean, Object actionBean) {

        CreateTopicRuleRequest topicRuleRequest = new CreateTopicRuleRequest();

        if (ruleBean.getType().equals("SNS")) {
            SnsBean snsBean = (SnsBean) actionBean;

            // constructed names of entities
            String thingName = "thing" + ruleBean.getParentThing().getId();
            String ruleName = ruleBean.getName();

            // create rule payload: {using data provided in ruleBean and snsBean}
            TopicRulePayload rulePayload = this.createSnsRulePayload(ruleBean, snsBean);

            // create rule at AWS
            topicRuleRequest.withRuleName(thingName + "_sns_" + ruleName)
                    .withTopicRulePayload(rulePayload);

        }

        return AwsIotHelper.getIotClient().createTopicRule(topicRuleRequest);
    }



    public ReplaceTopicRuleResult replaceTopicRule(RuleBean ruleBean, Object actionBean) {
        ReplaceTopicRuleRequest topicRuleRequest = new ReplaceTopicRuleRequest();

        if (ruleBean.getType().equals("SNS")) {
            SnsBean snsBean = (SnsBean) actionBean;

            //constructed names of entities
            String thingName = "thing" + ruleBean.getParentThing().getId();
            String ruleName = ruleBean.getName();

            // create rule payload: {using data provided in ruleBean and snsBean}
            TopicRulePayload rulePayload = this.createSnsRulePayload(ruleBean, snsBean);

            // replace rule at AWS
            topicRuleRequest.withRuleName(thingName + "_sns_" + ruleName)
                    .withTopicRulePayload(rulePayload);

            // Update the rule in DB
            RuleDAO.getInstance().update(ruleBean);
        }
        return AwsIotHelper.getIotClient().replaceTopicRule(topicRuleRequest);
    }


    public TopicRulePayload createSnsRulePayload(RuleBean ruleBean, SnsBean snsBean) {

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
                .withSql("SELECT " + ruleBean.getData()
                        + " FROM '$aws/things/thing"
                        + ruleBean.getParentThing().getId()
                        + "/shadow/update'" + ruleCondition)
                .withRuleDisabled(false)
                .withAwsIotSqlVersion("2016-03-23")
                .withActions(actionList);

        return rulePayload;
    }


    public DeleteTopicRuleResult deleteRule(RuleBean ruleBean) {
        DeleteTopicRuleRequest deleteTopicRuleRequest = new DeleteTopicRuleRequest();
        deleteTopicRuleRequest.withRuleName("thing" + ruleBean.getParentThing().getId() + "_sns_" + ruleBean.getName());

        DeleteTopicRuleResult deleteTopicRuleResult =
                AwsIotHelper.getIotClient().deleteTopicRule(deleteTopicRuleRequest);
        return deleteTopicRuleResult;
    }
}
