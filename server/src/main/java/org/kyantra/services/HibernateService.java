package org.kyantra.services;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.kyantra.beans.RightsBean;
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