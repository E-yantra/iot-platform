package org.kyantra.services;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.kyantra.beans.*;

public class HibernateService {

    private static HibernateService mService = new HibernateService();
    private static SessionFactory sessionFactory ;

    private HibernateService() {

        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(UserBean.class)
                .addAnnotatedClass(RightsBean.class)
                .addAnnotatedClass(UnitBean.class)
                .addAnnotatedClass(DeviceAttributeBean.class)
                .addAnnotatedClass(DeviceBean.class)
                .addAnnotatedClass(ThingBean.class)
                .addAnnotatedClass(SessionBean.class)
                .addAnnotatedClass(ConfigBean.class)
                .addAnnotatedClass(CronBean.class)
                .addAnnotatedClass(SNSBean.class)
                .addAnnotatedClass(SNSSubscriptionBean.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static HibernateService getInstance(){
        return mService;
    }
}