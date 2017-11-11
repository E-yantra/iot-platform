package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.UserBean;
import org.kyantra.services.HibernateService;

public class UserDAO {

    HibernateService mService;

    public UserDAO(){
        mService = HibernateService.getInstance();
    }

    public void addUser(UserBean bean){
        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(bean);
        tx.commit();
        session.close();
    }
}
