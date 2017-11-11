package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.UserBean;
import org.kyantra.services.HibernateService;

public class UserDAO {

    static UserDAO instance = new UserDAO();
    public static UserDAO getInstance(){ return instance; }


    HibernateService mService;


    private UserDAO(){

        mService = HibernateService.getInstance();
    }

    public UserBean add(UserBean bean){
        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(bean);
        tx.commit();
        session.close();
        return bean;
    }


    public UserBean get(Integer id) {
        Session session = mService.getSessionFactory().openSession();
        UserBean userBean = session.get(UserBean.class,id);
        session.close();
        return userBean;
    }

    public void delete(Integer id){

        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserBean user = session.get(UserBean.class, id);
        session.delete(user);
        tx.commit();
        session.close();

    }

    public void update(int id, String name, String email, String password){
        if(id <=0)
            return;
        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserBean user = session.get(UserBean.class, id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        tx.commit();
        session.close();
    }
}
