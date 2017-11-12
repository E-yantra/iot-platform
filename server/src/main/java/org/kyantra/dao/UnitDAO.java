package org.kyantra.dao;

import org.hibernate.Session;
import org.kyantra.beans.UnitBean;

public class UnitDAO extends BaseDAO{

    private static UnitDAO instance = new UnitDAO();

    public static UnitDAO getInstance(){
        return instance;
    }

    public UnitBean get(Integer id){
        Session session = getService().getSessionFactory().openSession();
        UnitBean unit = session.get(UnitBean.class,id);
        session.close();
        return unit;
    }
}
