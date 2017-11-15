package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
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

    //TODO complete the method -Done
    public UserBean addRights(UserBean userBean, UnitBean unitBean, RoleEnum role){
        RightsBean rightsBean = new RightsBean();
        rightsBean.setUnit(unitBean);
        rightsBean.setRole(role);
        userBean.getRights().add(rightsBean);

        //save the object
        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(userBean);
        session.save(rightsBean); //TODO check if required

        tx.commit();
        session.close();
        return userBean;
    }

    //TODO -Done
    public UserBean getByEmail(String email) {
        Session session = mService.getSessionFactory().openSession();
        Query query = session.createQuery("from UserBean where email = :emailId"); //TODO: use LIKE '%%'?
        query.setParameter("emailId",email);
        List<UserBean> users = query.getResultList();
        session.close();

        if (users.size()>0){
            //user found
            return users.get(0);
        }

        return null;
    }

    //TODO get from SessionBean -Done
    public UserBean getByToken(String token) {
        Session session = mService.getSessionFactory().openSession();
        Query query = session.createQuery("Select ub.* from SessionBean as sb join UserBean as ub where sb.token = :sessionToken");
        query.setParameter("sessionToken",token);
        List<UserBean> users = query.getResultList();
        session.close();

        if (users.size()>0){
            //user found
            return users.get(0);
        }

        return null;
    }

    public List<UnitBean> getUserUnits(UserBean user){
        Session session = mService.getSessionFactory().openSession();
        //TODO: check query
        Query query = session.createQuery
                ("Select ub.* from UnitBean as ub join UserBean as ub where user_id = :userId");
        query.setParameter("userId",user.getId());
        List<UnitBean> units = query.getResultList();
        session.close();

        return units;
    }
}
