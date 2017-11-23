package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.ConfigBean;

import javax.persistence.Query;
import java.util.List;

public class ConfigDAO extends BaseDAO {

    private static ConfigDAO instance = new ConfigDAO();
    public static ConfigDAO getInstance() { return instance; }

    public ConfigBean get(String key){
        Session session = getService().getSessionFactory().openSession();
        String ql = "from ConfigBean where confKey='"+key+"'";
        Query query = session.createQuery(ql);
        List<ConfigBean> list = query.getResultList();
        session.close();
        if(list.size()==0) return null;
        return list.get(0);
    }

    public void set(String key, String value){

        ConfigBean configBean = get(key);
        if(configBean==null){
            configBean = new ConfigBean();
            configBean.setKey(key);
            configBean.setValue(value);
            Session session = getService().getSessionFactory().openSession();
            session.save(configBean);
            session.close();

        }else {
            Session session = getService().getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            configBean = session.get(ConfigBean.class, configBean.getId());
            configBean.setValue(value);
            tx.commit();
            session.close();
        }
    }
}
