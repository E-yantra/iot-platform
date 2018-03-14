package org.kyantra.dao;

import org.hibernate.Session;
import org.kyantra.beans.SnsBean;

public class SnsDAO extends BaseDAO {
    static SnsDAO instance = new SnsDAO();
    public static SnsDAO getInstance() { return instance; }

    public SnsBean add(SnsBean snsBean) {
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(snsBean);
        session.getTransaction().commit();
        session.close();
        return  snsBean;
    }

    public SnsBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        SnsBean snsBean = session.get(SnsBean.class, id);
        session.close();
        return snsBean;
    }

}
