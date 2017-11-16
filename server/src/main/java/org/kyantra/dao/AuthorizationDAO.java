package org.kyantra.dao;

import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;

public class AuthorizationDAO extends BaseDAO{

    private static AuthorizationDAO instance = new AuthorizationDAO();

    public static AuthorizationDAO getInstance(){ return instance; }

    private AuthorizationDAO(){

    }

    public boolean ownsUnit(UserBean bean, UnitBean unitBean){
        return true;
    }

    public boolean ownsThing(UserBean bean, ThingBean thingBean){
        return true;
    }

    public boolean ownsDevice(UserBean bean, DeviceBean deviceBean){
        return true;
    }

    public boolean ownsDeviceAttributes(UserBean bean, DeviceAttributeBean deviceAttributeBean){
        return true;
    }

}
