package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UnitBean;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
public class ThingDAO extends BaseDAO{
    static ThingDAO instance = new ThingDAO();
    public static ThingDAO getInstance(){ return instance; }

    public ThingBean add(ThingBean bean, UnitBean currentUnit){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        bean.setParentUnit(currentUnit);
        session.save(bean);
        session.getTransaction().commit();
        session.close();
        return bean;
    }

    /**
     * Returns list of all things, page by page
     * @param page
     * @param limit
     * @return
     */
    public List<ThingBean> list(int page, int limit){
        Session session = getService().getSessionFactory().openSession();
        String ql = "from ThingBean";
        Query query = session.createQuery(ql);
        query.setFirstResult(page*limit);
        query.setMaxResults(limit);
        List<ThingBean> list = query.getResultList();
        session.close();
        return list;
    }

    /**
     * Returns list of all things under a parent Unit
     * @param parentUnit
     * @param page
     * @param limit
     * @return
     */
    public List<ThingBean> list(UnitBean parentUnit, int page, int limit){
        //TODO: verify if this is correct interpretation
        List<ThingBean> list = parentUnit.getThings().subList(page*limit,limit);
        //session.close();
        return list;
    }

    public ThingBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        ThingBean thingBean = session.get(ThingBean.class,id);
        session.close();
        return thingBean;
    }

    public void delete(Integer id){
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        ThingBean thing = session.get(ThingBean.class, id);
        session.delete(thing);
        tx.commit();
        session.close();
    }

    public void update(int id, String name, String description, List<DeviceBean> devices){
        if(id <=0)
            return;
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        ThingBean thing = session.get(ThingBean.class, id);
        thing.setName(name);
        thing.setDescription(description);
        thing.setDevices(devices);
        tx.commit();
        session.close();
    }
}
