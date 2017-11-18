package org.kyantra.resources;

import org.glassfish.jersey.server.mvc.Template;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.UnitDAO;
import org.kyantra.interfaces.Session;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Map<String, Object> index() throws URISyntaxException {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","home");
        UserBean userBean = (UserBean) getSecurityContext().getUserPrincipal();
        Set<UnitBean> unitBeanList = userBean.getRights().stream().map(RightsBean::getUnit).collect(Collectors.toSet());
        map.put("units",unitBeanList);
        map.put("gson",gson.toJson(unitBeanList));
        setCommonData(map);
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
    @Path("/units/list")
    @Template(name = "/units/list.ftl")
    @Session
    public Map<String, Object> listUnits() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","unit");
        setCommonData(map);
        return map;
    }

    @GET
    @Path("/units/create")
    @Template(name = "/units/create.ftl")
    @Session
    public Map<String, Object> createUnit() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","unit");
        setCommonData(map);
        return map;
    }

    @GET
    @Path("/things/list")
    @Template(name = "/thing/list.ftl")
    @Session
    public Map<String, Object> listThings() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","thing");
        setCommonData(map);
        return map;
    }

    @GET
    @Path("/things/create")
    @Template(name = "/thing/create.ftl")
    @Session
    public Map<String, Object> createThing() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","thing");
        setCommonData(map);
        return map;
    }

    @GET
    @Path("/rights/create")
    @Template(name = "/rights/create.ftl")
    @Session
    public Map<String, Object> createRight() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","right");
        setCommonData(map);
        return map;
    }

    @GET
    @Path("/rights/list")
    @Template(name = "/rights/list.ftl")
    @Session
    public Map<String, Object> listRight() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","right");
        setCommonData(map);
        return map;
    }
    @GET
    @Path("/users/create")
    @Template(name = "/users/create.ftl")
    @Session
    public Map<String, Object> createUsers() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","user");
        setCommonData(map);
        return map;
    }

    @GET
    @Path("/users/list")
    @Template(name = "/users/list.ftl")
    @Session
    public Map<String, Object> listusers() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","user");
        setCommonData(map);
        return map;
    }

    @GET
    @Path("/logout")
    @Session
    @Template(name = "index.ftl")
    public Map<String, Object> logout(@Context HttpServletRequest request) throws URISyntaxException {
        final Map<String, Object> map = new HashMap<String, Object>();
        throw new WebApplicationException(Response.temporaryRedirect(new URI("/login")).cookie(new NewCookie("authorization","")).build());
    }

    @GET
    @Path("/unauthorized")
    @Template(name = "/auth/unauthorized.ftl")
    @Session
    public Map<String, Object> unauthorized() throws URISyntaxException{
        final Map<String, Object> map = new HashMap<String, Object>();
        throw new WebApplicationException(Response.temporaryRedirect(new URI("/login")).cookie(new NewCookie("authorization","")).build());
    }

    private void setCommonData(Map<String, Object> map){
        map.put("user",getSecurityContext().getUserPrincipal());
    }


    @GET
    @Path("/units/get/{id}")
    @Template(name = "/units/get.ftl")
    @Session
    public Map<String, Object> getUnit(@PathParam("id") Integer id) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active","unit");
        UnitBean unit = UnitDAO.getInstance().get(id);
        map.put("unit",unit);
        setCommonData(map);
        return map;
    }

}
