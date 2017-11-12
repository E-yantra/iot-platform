package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.ThingBean;

import java.util.List;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
public class ThingDAO extends BaseDAO{
    static ThingDAO instance = new ThingDAO();
    public static ThingDAO getInstance(){ return instance; }

    public ThingBean add(ThingBean bean){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(bean);
        session.getTransaction().commit();
        session.close();
        return bean;
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
