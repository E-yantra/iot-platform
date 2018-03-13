package org.kyantra.dao;

import com.amazonaws.services.sns.model.SubscribeResult;
import org.hibernate.Session;
import org.kyantra.beans.SNSBean;
import org.kyantra.beans.SNSSubscriptionBean;
import com.amazonaws.services.sns.model.SubscribeRequest;
import org.kyantra.utils.AwsIotHelper;

public class SNSSubscriptionDAO extends BaseDAO{
    static SNSSubscriptionDAO instance = new SNSSubscriptionDAO();
    public static SNSSubscriptionDAO getInstance() {
        return instance;
    }

    public SNSSubscriptionBean add(SNSSubscriptionBean subscriptionBean) {
        String topicARN = subscriptionBean.getParentSNSBean().getTopicARN();
        SubscribeRequest request = new SubscribeRequest(topicARN, subscriptionBean.getType(), subscriptionBean.getValue());
        SubscribeResult result = AwsIotHelper.getAmazonSNSClient().subscribe(request);

        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();

        SNSBean snsBean = SnsDAO.getInstance().get(subscriptionBean.getParentSNSBean().getId());
        SNSSubscriptionBean snsSubscriptionBean = snsBean.addSubscription(subscriptionBean);

        System.out.println(subscriptionBean.hashCode());
        System.out.println(snsSubscriptionBean.hashCode());

        session.saveOrUpdate(snsBean);
        session.getTransaction().commit();
        session.close();
        return snsSubscriptionBean;
    }
}
