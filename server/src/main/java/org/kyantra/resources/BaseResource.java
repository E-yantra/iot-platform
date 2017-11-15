package org.kyantra.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Session;
import org.kyantra.services.HibernateService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseResource {


    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    static HibernateService service;

    @Context
    HttpServletRequest request;



    @Context
    SecurityContext sc;

    public BaseResource(){
        if(service==null) {
            service = HibernateService.getInstance();
        }
    }

    public Session getSession(){
        return service.getSessionFactory().openSession();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public SecurityContext getSecurityContext() {
        return sc;
    }
}
