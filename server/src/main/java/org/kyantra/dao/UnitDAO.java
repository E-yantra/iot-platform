package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.UnitBean;

import java.util.List;

public class UnitDAO extends BaseDAO{
    static UnitDAO instance = new UnitDAO();
    public static UnitDAO getInstance(){ return instance; }


    public UnitBean add(UnitBean bean){
        Session session = getService().getSessionFactory().openSession();
        session.save(bean);
        session.close();
        return bean;
    }

    public UnitBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        UnitBean unitBean = session.get(UnitBean.class,id);
        session.close();
        return unitBean;
    }

    public void delete(Integer id){
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UnitBean unit = session.get(UnitBean.class, id);
        session.delete(unit);
        tx.commit();
        session.close();
    }

    public void update(int id, String unitName, String description, String photo, UnitBean parent, List<UnitBean> subUnits){
        if(id <=0)
            return;
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UnitBean unit = session.get(UnitBean.class, id);
        unit.setUnitName(unitName);
        unit.setDescription(description);
        unit.setPhoto(photo);
        unit.setParent(parent);
        unit.setSubunits(subUnits);
        tx.commit();
        session.close();
    }
}
