package org.kyantra.dao;

import org.hibernate.Session;
import org.kyantra.beans.*;

import javax.persistence.Query;
import java.util.List;

public class AuthorizationDAO extends BaseDAO{

    private static AuthorizationDAO instance = new AuthorizationDAO();

    public static AuthorizationDAO getInstance(){ return instance; }

    private AuthorizationDAO(){

    }

    public boolean ownsUnit(UserBean user, UnitBean unit){

        try {
            Session session = getService().getSessionFactory().openSession();
            Query query = session.createQuery("from RightsBean where UserId = :userId and UnitId = :unitId");
            query.setParameter("userId", user.getId());
            query.setParameter("unitId", unit.getId());
            List<RightsBean> rights = query.getResultList();
            session.close();

            if (rights.size() > 0)
                return true;

            return false;
        }catch (Throwable t){
            return true;
        }
    }

    public boolean ownsThing(UserBean user, ThingBean thing){

        try {
            Session session = getService().getSessionFactory().openSession();
            Query query = session.createQuery("from RightsBean where UserId = :userId and UnitId = :unitId");
            query.setParameter("userId", user.getId());
            query.setParameter("unitId", thing.getParentUnit().getId());
            List<RightsBean> rights = query.getResultList();
            session.close();

            if (rights.size() > 0)
                return true;

            return false;
        }catch (Throwable t){
            return true;
        }
    }

    public boolean ownsDevice(UserBean user, DeviceBean device){

        try {
            Session session = getService().getSessionFactory().openSession();
            Query query = session.createQuery("from RightsBean where UserId = :userId and UnitId = :unitId");
            query.setParameter("userId", user.getId());
            query.setParameter("unitId", device.getOwnerUnit().getId());
            List<RightsBean> rights = query.getResultList();
            session.close();

            if (rights.size() > 0)
                return true;

            return false;
        }catch (Throwable t){
            return true;
        }
    }

    public boolean ownsDeviceAttributes(UserBean user, DeviceAttributeBean deviceAttribute){
        try {
            Session session = getService().getSessionFactory().openSession();
            Query query = session.createQuery("from RightsBean where UserId = :userId and UnitId = :unitId");
            query.setParameter("userId", user.getId());
            query.setParameter("unitId", deviceAttribute.getOwnerUnit().getId());
            List<RightsBean> rights = query.getResultList();
            session.close();

            if (rights.size() > 0)
                return true;

            return false;
        }catch (Throwable t){
            return true;
        }
    }
}
