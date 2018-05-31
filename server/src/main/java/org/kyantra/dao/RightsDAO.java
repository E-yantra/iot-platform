package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.services.HibernateService;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


    /**
     * Returns list of all rights, page by page
     * @param page
     * @param limit
     * @return
     */
    public List<RightsBean> list(int page, int limit){
        Session session = mService.getSessionFactory().openSession();
        String ql = "from RightsBean";
        Query query = session.createQuery(ql);
        query.setFirstResult(page*limit);
        query.setMaxResults(limit);
        List<RightsBean> list = query.getResultList();
        session.close();
        return list;
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

    // update the role of the user on the unit
    public RightsBean update(int id, RoleEnum role){
        if(id <=0)
            return null;
        Session session = mService.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        RightsBean bean = session.get(RightsBean.class, id);
//        bean.setUnit(unit);
        bean.setRole(role);
        tx.commit();
        session.close();
        return bean;
    }

    public Set<UnitBean> getUnitsByUser(UserBean userBean) {
        Session session = mService.getSessionFactory().openSession();
        String ql = "from RightsBean where user_id="+userBean.getId();
        Query query = session.createQuery(ql);
        List<RightsBean> list = query.getResultList();
        Set<UnitBean> units = list.stream().map(RightsBean::getUnit).collect(Collectors.toSet());
        session.close();
        return units;
    }

    public Set<RightsBean> getRightsByUser(UserBean userBean) {
        Session session = mService.getSessionFactory().openSession();
        String ql = "from RightsBean where user_id="+userBean.getId();
        Query query = session.createQuery(ql);
        List<RightsBean> list = query.getResultList();
        session.close();
        return new HashSet<>(list);
    }

    public Set<RightsBean> getRightsByUnit(UnitBean unitBean) {
        Session session = mService.getSessionFactory().openSession();
        String ql = "from RightsBean where unit_id="+unitBean.getId();
        Query query = session.createQuery(ql);
        List<RightsBean> list = query.getResultList();
        session.close();
        return new HashSet<>(list);
    }
}
