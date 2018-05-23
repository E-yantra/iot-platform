package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitDAO extends BaseDAO{
    static UnitDAO instance = new UnitDAO();
    public static UnitDAO getInstance(){ return instance; }

    /**
     * Returns list of all units, page by page
     * @param page
     * @param limit
     * @return
     */
    public List<UnitBean> list(int page, int limit){
        Session session = getService().getSessionFactory().openSession();
        String ql = "from UnitBean";
        Query query = session.createQuery(ql);
        query.setFirstResult(page*limit);
        query.setMaxResults(limit);
        List<UnitBean> list = query.getResultList();
        session.close();
        return list;
    }

    /**
     * Returns list of all units under a parent Unit
     * @param parentUnit
     * @param page
     * @param limit
     * @return
     */
    public List<UnitBean> list(UnitBean parentUnit, int page, int limit){
        //TODO: verify if this is correct interpretation
        List<UnitBean> list = parentUnit.getSubunits().subList(page*limit,limit);
        //session.close();
        return list;
    }

    // returns child unit
    public UnitBean add(UnitBean currentUnit){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(currentUnit);
        session.getTransaction().commit();
        session.close();
        return currentUnit;
    }

    public UnitBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        UnitBean unitBean = session.get(UnitBean.class,id);
        session.close();
        return unitBean;
    }

    public void delete(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UnitBean unit = session.get(UnitBean.class, id);
        session.delete(unit);
        tx.commit();
        session.close();
    }

    public void update(int id, String unitName, String description, String photo) {
        if(id <=0)
            return;
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UnitBean unit = session.get(UnitBean.class, id);
        unit.setUnitName(unitName);
        unit.setDescription(description);
        unit.setPhoto(photo);
        tx.commit();
        session.close();
    }

    public void addUsers(int id, Set<UserBean> users) {
        if(id <=0)
            return;

        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UnitBean unit = session.get(UnitBean.class, id);

        users.forEach(user->{
            RightsBean bean = new RightsBean();
            bean.setRole(RoleEnum.ALL);
            bean.setUnit(unit);
            bean.setUser(user);
            RightsDAO.getInstance().add(bean);
        });

        tx.commit();
        session.close();
    }
}
