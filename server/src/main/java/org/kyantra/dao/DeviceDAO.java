package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UnitBean;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
public class DeviceDAO extends BaseDAO {
    static DeviceDAO instance = new DeviceDAO();
    public static DeviceDAO getInstance(){ return instance; }

    public DeviceBean add(DeviceBean bean, UnitBean currentUnit){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        bean.setOwnerUnit(currentUnit);
        session.save(bean);
        session.getTransaction().commit();
        session.close();
        return bean;
    }

    /**
     * Returns list of all devices, page by page
     * @param page
     * @param limit
     * @return
     */
    public List<DeviceBean> list(int page, int limit){
        Session session = mService.getSessionFactory().openSession();
        String ql = "from DeviceBean";
        Query query = session.createQuery(ql);
        query.setFirstResult(page*limit);
        query.setMaxResults(limit);
        List<DeviceBean> list = query.getResultList();
        session.close();
        return list;
    }

    /**
     * Returns list of all devices under parent thing.
     * @param parentThing
     * @param page
     * @param limit
     * @return
     */
    public List<DeviceBean> list(ThingBean parentThing, int page, int limit){
        //TODO: verify if this is correct interpretation
        List<DeviceBean> list = parentThing.getDevices().subList(page*limit,limit);
        //session.close();
        return list;
    }

    public DeviceBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        DeviceBean deviceBean = session.get(DeviceBean.class,id);
        session.close();
        return deviceBean;
    }

    public void delete(Integer id){
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        DeviceBean device = session.get(DeviceBean.class, id);
        session.delete(device);
        tx.commit();
        session.close();
    }

    public void update(int id, String name, String description, List<DeviceAttributeBean> deviceAttributes){
        if(id <=0)
            return;
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        DeviceBean device = session.get(DeviceBean.class, id);
        device.setName(name);
        device.setDescription(description);
        device.setDeviceAttributes(deviceAttributes);
        tx.commit();
        session.close();
    }
}
