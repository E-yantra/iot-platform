package org.kyantra.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.SessionBean;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.services.HibernateService;

import javax.persistence.Query;
import java.util.List;

public class UserDAO {

    static UserDAO instance = new UserDAO();
    public static UserDAO getInstance(){ return instance; }


    HibernateService mService;


    private UserDAO(){

        mService = HibernateService.getInstance();
    }

    public List<UserBean> list(int page, int limit){

        try {
            Session session = mService.getSessionFactory().openSession();
            String ql = "from UserBean";
            Query query = session.createQuery(ql);
            query.setFirstResult(page * limit);
            query.setMaxResults(limit);
            List<UserBean> list = query.getResultList();
            session.close();
            return list;
        }catch (Throwable t){
            t.printStackTrace();
        }
        return null;
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

    //TODO complete the method
    public UserBean addRights(UserBean userBean, UnitBean unitBean, RoleEnum role){
        RightsBean rightsBean = new RightsBean();
        rightsBean.setUnit(unitBean);
        rightsBean.setRole(role);
        userBean.getRights().add(rightsBean);
        //save the object
        return null;
    }


    public UserBean getByEmail(String email) {
        Session session = mService.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(UserBean.class);
        UserBean userBean = (UserBean) criteria.add(Restrictions.eq("email", email)).uniqueResult();
        session.close();
        return userBean;

    }

    public UserBean getByToken(String token) {
        Session session = mService.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(SessionBean.class);
        SessionBean sessionBean = (SessionBean) criteria.add(Restrictions.eq("token", token)).uniqueResult();
        sessionBean.getUser();
        session.close();
        return sessionBean.getUser();
    }
}
