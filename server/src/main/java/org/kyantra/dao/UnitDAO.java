package org.kyantra.dao;

import org.hibernate.Session;
import org.kyantra.beans.UnitBean;

public class UnitDAO extends BaseDAO{

    public void addUnit(UnitBean bean){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(bean);
        session.getTransaction().commit();
        session.close();
    }
}
