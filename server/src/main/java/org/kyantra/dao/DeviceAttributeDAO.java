package org.kyantra.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UnitBean;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Returns list of all device attributes, page by page
     * @param page
     * @param limit
     * @return
     */
    public List<DeviceAttributeBean> list(int page, int limit){
        Session session = mService.getSessionFactory().openSession();
        String ql = "from DeviceAttributeBean";
        Query query = session.createQuery(ql);
        query.setFirstResult(page*limit);
        query.setMaxResults(limit);
        List<DeviceAttributeBean> list = query.getResultList();
        session.close();
        return list;
    }

    /**
     * Returns list of all device attributes under a parent device
     * @param parent
     * @param page
     * @param limit
     * @return
     */
    public List<DeviceAttributeBean> list(DeviceBean parent, int page, int limit){
        //TODO: verify if this is correct interpretation
        List<DeviceAttributeBean> list = parent.getDeviceAttributes().subList(page*limit,limit);
        //session.close();
        return list;
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

    public String getTopic(DeviceAttributeBean att) {
        List<String> list = new ArrayList<>();
        list.add(sanitize(att.getName())+":"+att.getId());
        list.add(sanitize(att.getParentDevice().getName())+":"+(att.getParentDevice().getId()));
        list.add(sanitize(att.getParentDevice().getParentThing().getName())+":"+att.getParentDevice().getParentThing().getId());
        UnitBean unitBean = att.getParentDevice().getParentThing().getParentUnit();
        while (unitBean!=null){
            list.add(sanitize(unitBean.getUnitName())+":"+unitBean.getId());
            unitBean = unitBean.getParent();
        }
        Collections.reverse(list);
        String topic = StringUtils.join(list,"/");
        return topic;
    }

    private String sanitize(String string){
        return string.replaceAll("[^a-zA-Z0-9]", "");
    }
}
