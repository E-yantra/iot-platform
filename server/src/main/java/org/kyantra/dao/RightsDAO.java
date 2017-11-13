package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.services.HibernateService;

public class RightsDAO {

    static RightsDAO instance = new RightsDAO();
    public static RightsDAO getInstance(){ return instance; }


    HibernateService mService;


    private RightsDAO(){

        mService = HibernateService.getInstance();
    }

    public RightsBean add(RightsBean bean){
        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(bean);
        tx.commit();
        session.close();
        return bean;
    }


    public RightsBean get(Integer id) {
        Session session = mService.getSessionFactory().openSession();
        RightsBean bean = session.get(RightsBean.class,id);
        session.close();
        return bean;
    }

    public void delete(Integer id){

        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        RightsBean user = session.get(RightsBean.class, id);
        session.delete(user);
        tx.commit();
        session.close();

    }

    public void update(int id, UnitBean unit, RoleEnum role){
        if(id <=0)
            return;
        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        RightsBean bean = session.get(RightsBean.class, id);
        bean.setUnit(unit);
        bean.setRole(RoleEnum.ALL);
        tx.commit();
        session.close();
    }
}
