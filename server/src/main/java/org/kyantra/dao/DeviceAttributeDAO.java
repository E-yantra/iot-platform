package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.DeviceAttributeBean;

import java.util.List;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
public class DeviceAttributeDAO extends BaseDAO {
    static DeviceAttributeDAO instance = new DeviceAttributeDAO();
    public static DeviceAttributeDAO getInstance(){ return instance; }

    public DeviceAttributeBean add(DeviceAttributeBean bean){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(bean);
        session.getTransaction().commit();
        session.close();
        return bean;
    }

    public DeviceAttributeBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        DeviceAttributeBean deviceAttributeBean = session.get(DeviceAttributeBean.class,id);
        session.close();
        return deviceAttributeBean;
    }

    public void delete(Integer id){
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        DeviceAttributeBean deviceAttribute = session.get(DeviceAttributeBean.class, id);
        session.delete(deviceAttribute);
        tx.commit();
        session.close();
    }

    public void update(int id, String name, String type, String def){
        if(id <=0)
            return;
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        DeviceAttributeBean deviceAttribute = session.get(DeviceAttributeBean.class, id);
        deviceAttribute.setName(name);
        deviceAttribute.setType(type);
        deviceAttribute.setDef(def);
        tx.commit();
        session.close();
    }
}
