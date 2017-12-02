package org.kyantra.services;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.kyantra.beans.ConfigBean;
import org.kyantra.beans.CronBean;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.SessionBean;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;

public class HibernateService {

    private static HibernateService mService = new HibernateService();
    private static SessionFactory sessionFactory ;

    private HibernateService() {

        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(UserBean.class);
        configuration.addAnnotatedClass(RightsBean.class);
        configuration.addAnnotatedClass(UnitBean.class);
        configuration.addAnnotatedClass(DeviceAttributeBean.class);
        configuration.addAnnotatedClass(DeviceBean.class);
        configuration.addAnnotatedClass(ThingBean.class);
        configuration.addAnnotatedClass(SessionBean.class);
        configuration.addAnnotatedClass(ConfigBean.class);
        configuration.addAnnotatedClass(CronBean.class);
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