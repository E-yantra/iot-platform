package org.kyantra.aws;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import org.kyantra.beans.SnsBean;
import org.kyantra.beans.SnsSubscriptionBean;
import org.kyantra.utils.AwsIotHelper;

public class SNSHelper {

    private static SNSHelper instance = new SNSHelper();

    public static SNSHelper getInstance() {
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

}
