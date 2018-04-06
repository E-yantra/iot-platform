package org.kyantra.aws;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicResult;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.SnsBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.utils.AwsIotHelper;

import java.util.ArrayList;
import java.util.List;

public class RuleHelper {
    public static RuleHelper instance = new RuleHelper();

    public static RuleHelper getInstance() {
        return instance;
    }

    public CreateTopicRuleResult createTopicRule(RuleBean ruleBean, String actionType, Object actionBean) {

        CreateTopicRuleRequest topicRuleRequest = new CreateTopicRuleRequest();

        if (actionType.equals("sns")) {
            SnsBean snsBean = (SnsBean) actionBean;

            String ruleCondition = " WHERE ";

            //constructed names of entities
            String thingName = "thing" + ruleBean.getParentThing().getId();
            String ruleName = ruleBean.getName();

            System.out.println(ruleBean.getCondition());
            if (!ruleBean.getCondition().equals(""))
                ruleCondition = ruleCondition + ruleBean.getCondition();
            else
                ruleCondition = ruleBean.getCondition();

            // create rule in AWS
            SnsAction snsAction = new SnsAction();
            snsAction.withTargetArn(snsBean.getTopicARN())
                    .withMessageFormat(MessageFormat.RAW)
                    .withRoleArn(ConfigDAO.getInstance().get("IoTRoleARN").getValue());

            Action action = new Action().withSns(snsAction);
            List<Action> actionList = new ArrayList<>();
            actionList.add(action);

            TopicRulePayload rulePayload = new TopicRulePayload();
            rulePayload.withDescription(ruleBean.getDescription())
                    .withSql("SELECT " + ruleBean.getData() + " FROM '$aws/things/" +
                            ruleBean.getParentThing().getName() + "/shadow/update'" + ruleCondition)
                    .withRuleDisabled(false)
                    .withAwsIotSqlVersion("2016-03-23")
                    .withActions(actionList);

            topicRuleRequest.withRuleName(thingName + "_sns_" + ruleName)
                    .withTopicRulePayload(rulePayload);

        }

        return AwsIotHelper.getIotClient().createTopicRule(topicRuleRequest);
    }

    public ReplaceTopicRuleResult replaceTopicRule(RuleBean ruleBean, String actionType, Object actionBean) {
        ReplaceTopicRuleRequest topicRuleRequest = new ReplaceTopicRuleRequest();

        if (actionType.equals("sns")) {
            SnsBean snsBean = (SnsBean) actionBean;

            String ruleCondition = " WHERE ";

            //constructed names of entities
            String thingName = "thing" + ruleBean.getParentThing().getId();
            String ruleName = ruleBean.getName();

            System.out.println(ruleBean.getCondition());
            if (!ruleBean.getCondition().equals(""))
                ruleCondition = ruleCondition + ruleBean.getCondition();
            else
                ruleCondition = ruleBean.getCondition();

            // create rule in AWS
            SnsAction snsAction = new SnsAction();
            snsAction.withTargetArn(snsBean.getTopicARN())
                    .withMessageFormat(MessageFormat.RAW)
                    .withRoleArn(ConfigDAO.getInstance().get("IoTRoleARN").getValue());

            Action action = new Action().withSns(snsAction);
            List<Action> actionList = new ArrayList<>();
            actionList.add(action);

            TopicRulePayload rulePayload = new TopicRulePayload();

            rulePayload.withDescription(ruleBean.getDescription())
                    .withSql("SELECT " + ruleBean.getData() + " FROM '$aws/things/" +
                            ruleBean.getParentThing().getName() + "/shadow/update'" + ruleCondition)
                    .withRuleDisabled(false)
                    .withAwsIotSqlVersion("2016-03-23");
//                    .withActions(actionList);

            topicRuleRequest.withRuleName(thingName + "_sns_" + ruleName)
                    .withTopicRulePayload(rulePayload);
        }
        return AwsIotHelper.getIotClient().replaceTopicRule(topicRuleRequest);
    }

    public DeleteTopicRuleResult deleteRule(RuleBean ruleBean) {
        DeleteTopicRuleRequest deleteTopicRuleRequest = new DeleteTopicRuleRequest();
        deleteTopicRuleRequest.withRuleName(ruleBean.getName());

        DeleteTopicRuleResult deleteTopicRuleResult =
                AwsIotHelper.getIotClient().deleteTopicRule(deleteTopicRuleRequest);
        return deleteTopicRuleResult;
    }
}
