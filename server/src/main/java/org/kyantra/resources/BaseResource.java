package org.kyantra.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Session;
import org.kyantra.services.HibernateService;

public class BaseResource {

    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    static HibernateService service;

    public BaseResource(){
        if(service==null) {
            service = HibernateService.getInstance();
        }
    }

    public Session getSession(){
        return service.getSessionFactory().openSession();
    }
}
