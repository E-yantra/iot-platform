package org.kyantra.resources;

import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HomeResource extends BaseResource {

    @GET
    @Path("/")
    @Template(name = "/index.ftl")
    public Map<String, Object> index() {
        final Map<String, Object> map = new HashMap<String, Object>();
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
    @Path("/units")
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
    @Template(name = "/things/list.ftl")
    public Map<String, Object> listThings() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @GET
    @Path("/things/create")
    @Template(name = "/things/create.ftl")
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

}
