package org.kyantra.services;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.kyantra.config.EnvironmentConfig;
import org.kyantra.beans.*;

public class HibernateService {

    private static HibernateService mService = new HibernateService();
    private static SessionFactory sessionFactory;

    private HibernateService() {

        Configuration configuration = new Configuration();

        if (System.getProperty("environment") == null)
            configuration.configure("hibernate.cfg.xml");
        else if (System.getProperty("environment").equals(EnvironmentConfig.TEST))
            configuration.configure("hibernate-test.cfg.xml");
        else configuration.configure("hibernate.cfg.xml");

        configuration.addAnnotatedClass(UserBean.class)
                .addAnnotatedClass(RightsBean.class)
                .addAnnotatedClass(UnitBean.class)
                .addAnnotatedClass(DeviceAttributeBean.class)
                .addAnnotatedClass(DeviceBean.class)
                .addAnnotatedClass(ThingBean.class)
                .addAnnotatedClass(SessionBean.class)
                .addAnnotatedClass(ConfigBean.class)
                .addAnnotatedClass(CronBean.class)
                .addAnnotatedClass(RuleBean.class)
                .addAnnotatedClass(SnsBean.class)
                .addAnnotatedClass(SnsSubscriptionBean.class);

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