package org.kyantra.aws;

import com.amazonaws.services.iot.model.GetTopicRuleRequest;
import com.amazonaws.services.iot.model.GetTopicRuleResult;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.AddPermissionRequest;
import com.amazonaws.services.lambda.model.AddPermissionResult;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.utils.AwsIotHelper;

import java.util.UUID;

public class LambdaHelper {

    public AddPermissionResult addPermission(String ruleName) {

        AWSLambda lambda = AwsIotHelper.getAWSLambdaClient();
        GetTopicRuleRequest ruleRequest = new GetTopicRuleRequest().withRuleName(ruleName);
        GetTopicRuleResult ruleResult = AwsIotHelper.getIotClient().getTopicRule(ruleRequest);

        String functionArn = ConfigDAO.getInstance().get("lambdaNotificationArn").getValue();
        return lambda.addPermission(new AddPermissionRequest()
                .withFunctionName(functionArn)
                .withStatementId(UUID.randomUUID().toString())
                .withPrincipal("iot.amazonaws.com")
                .withSourceArn(ruleResult.getRuleArn())
                .withAction("lambda:InvokeFunction"));
    }
}
