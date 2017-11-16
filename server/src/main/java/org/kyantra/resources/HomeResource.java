package org.kyantra.resources;

import org.glassfish.jersey.server.mvc.Template;
import org.kyantra.interfaces.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HomeResource extends BaseResource {


    @GET
    @Path("/session/status")
    @Session
    public String setSession(){
        return gson.toJson(getSecurityContext().getUserPrincipal());
    }


    @GET
    @Path("/")
    @Template(name = "/index.ftl")
    @Session
    public Map<String, Object> index() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("user",getSecurityContext().getUserPrincipal());
        return map;
    }

    @GET
    @Path("/signup")
    @Template(name = "/auth/signup.ftl")
    public Map<String, Object> signup() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/login")
    @Template(name = "/auth/login.ftl")
    public Map<String, Object> login() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }


    @GET
    @Path("/dashboard")
    @Template(name = "/dashboard.ftl")
    public Map<String, Object> dashboard() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/units/list")
    @Template(name = "/units/list.ftl")
    public Map<String, Object> listUnits() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/units/create")
    @Template(name = "/units/create.ftl")
    public Map<String, Object> createUnit() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/things/list")
    @Template(name = "/thing/list.ftl")
    public Map<String, Object> listThings() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/things/create")
    @Template(name = "/thing/create.ftl")
    public Map<String, Object> createThing() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/rights/create")
    @Template(name = "/rights/create.ftl")
    public Map<String, Object> createRight() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/rights/list")
    @Template(name = "/rights/list.ftl")
    public Map<String, Object> listRight() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }
    @GET
    @Path("/users/create")
    @Template(name = "/users/create.ftl")
    public Map<String, Object> createUsers() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/users/list")
    @Template(name = "/users/list.ftl")
    public Map<String, Object> listusers() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

}
