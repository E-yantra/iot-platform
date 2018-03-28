package org.kyantra.aws;

import com.amazonaws.services.iot.model.CreateTopicRuleRequest;
import com.amazonaws.services.iot.model.CreateTopicRuleResult;
import com.amazonaws.services.iot.model.DeleteTopicRuleRequest;
import com.amazonaws.services.iot.model.DeleteTopicRuleResult;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicResult;
import org.kyantra.beans.RuleBean;
import org.kyantra.utils.AwsIotHelper;

public class RuleHelper {
    public static RuleHelper instance = new RuleHelper();
    public static RuleHelper getInstance() {
        return instance;
    }

//    public CreateTopicRuleResult createTopic(RuleBean ruleBean) {
//        CreateTopicRuleRequest createTopicRequest = new CreateTopicRuleRequest(snsBean.getTopic());
//        CreateTopicRuleResult createTopicResult = AwsIotHelper.getIotClient().createTopic(createTopicRequest);
//        return createTopicResult;
//    }

    public DeleteTopicRuleResult deleteRule(RuleBean ruleBean) {
        DeleteTopicRuleRequest deleteTopicRuleRequest = new DeleteTopicRuleRequest();
        deleteTopicRuleRequest.withRuleName(ruleBean.getName());

        DeleteTopicRuleResult deleteTopicRuleResult =
                AwsIotHelper.getIotClient().deleteTopicRule(deleteTopicRuleRequest);
        return deleteTopicRuleResult;
    }
}
