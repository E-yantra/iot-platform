package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UnitDAO extends BaseDAO{
    static UnitDAO instance = new UnitDAO();
    public static UnitDAO getInstance(){ return instance; }


    public List<UnitBean> list(int page, int limit){
        Session session = getService().getSessionFactory().openSession();
        String ql = "from UnitBean";
        Query query = session.createQuery(ql);
        query.setFirstResult(page*limit);
        query.setMaxResults(limit);
        List<UnitBean> list = query.getResultList();
        session.close();
        return list;
    }

    //returns child unit
    public UnitBean add(UnitBean currentUnit, UnitBean childUnit){
        Session session = getService().getSessionFactory().openSession();

        //root unit will not have any parent unit
        if (currentUnit!=null){
            childUnit.setParent(currentUnit);
        }
        session.save(childUnit);
        session.close();
        return childUnit;
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

    public void addUsers(int id, Set<UserBean> users){
        if(id <=0)
            return;

        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UnitBean unit = session.get(UnitBean.class, id);

        //TODO: assign default rights of all suers as ALL
//        for (int index=0; index < users.size(); index++){
//
//        }

        unit.setUsers(users);
        tx.commit();
        session.close();
    }
}
