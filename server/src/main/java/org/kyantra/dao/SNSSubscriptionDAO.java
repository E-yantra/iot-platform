package org.kyantra.dao;

import org.hibernate.Session;
import org.kyantra.beans.SnsBean;
import org.kyantra.beans.SnsSubscriptionBean;

public class SNSSubscriptionDAO extends BaseDAO{
    static SNSSubscriptionDAO instance = new SNSSubscriptionDAO();
    public static SNSSubscriptionDAO getInstance() {
        return instance;
    }

    public SnsSubscriptionBean add(SnsSubscriptionBean subscriptionBean) {

        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();

        SnsBean snsBean = SnsDAO.getInstance().get(subscriptionBean.getParentSNSBean().getId());
        SnsSubscriptionBean snsSubscriptionBean = snsBean.addSubscription(subscriptionBean);

        System.out.println(subscriptionBean.hashCode());
        System.out.println(snsSubscriptionBean.hashCode());

        session.saveOrUpdate(snsBean);
        session.getTransaction().commit();
        session.close();
        return snsSubscriptionBean;
    }
}
