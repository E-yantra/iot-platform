package org.kyantra.dao;

import com.amazonaws.services.sns.AmazonSNS;
import org.hibernate.Session;
import org.kyantra.beans.SNSBean;
import org.kyantra.utils.AwsIotHelper;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;

public class SnsDAO extends BaseDAO {
    static SnsDAO instance = new SnsDAO();
    public static SnsDAO getInstance() { return instance; }

    public SNSBean add(SNSBean snsBean) {
        AmazonSNS amazonSNSClient = AwsIotHelper.getAmazonSNSClient();
        CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsBean.getTopic());
        //TODO: Handle thrown exceptions
        CreateTopicResult createTopicResult = amazonSNSClient.createTopic(createTopicRequest);
        snsBean.setTopicARN(createTopicResult.getTopicArn());
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(snsBean);
        session.getTransaction().commit();
        session.close();
        return  snsBean;
    }
}
