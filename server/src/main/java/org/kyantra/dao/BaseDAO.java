package org.kyantra.dao;

import org.kyantra.services.HibernateService;

public class BaseDAO {

    HibernateService mService;

    public BaseDAO(){
        mService = HibernateService.getInstance();
    }

    public HibernateService getService(){
        return mService;
    }
}
