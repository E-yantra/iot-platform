package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.UnitDAO;
import org.kyantra.dao.UserDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Lenovo on 12-11-2017.
 */
@Path("/unit")
@Api(value="unit")
public class UnitResource extends BaseResource {

    int limit = 10;

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        UnitBean unitBean = UnitDAO.getInstance().get(id);
        return gson.toJson(unitBean);
    }

    @GET
    @Path("list/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@PathParam("page") Integer page){
        Principal principal = getSecurityContext().getUserPrincipal();
        UserBean currentUser = (UserBean) principal;
        List<UnitBean> users = UnitDAO.getInstance().list(page,limit);
        return gson.toJson(users);
    }

    @POST
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("photo") String photo){
        //TODO: can parent unit be changed?
        UnitDAO.getInstance().update(id,name,description,photo);
        UnitBean unitBean = UnitDAO.getInstance().get(id);
        return gson.toJson(unitBean);
    }

    @DELETE
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id){
        try {
            UnitDAO.getInstance().delete(id);
            return "{}";
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Session
    public String create(@FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("photo") String photo,
                         @DefaultValue("0") @FormParam("parentUnitId") Integer parentUnitId ){
        //TODO: Root unit will have parent unit = NULL
        // scenario to be handled
        try {
            String s = "Found something";
            //System.out.println(gson.toJson(childUnit));
            UnitBean unit = new UnitBean();
            unit.setUnitName(name);
            unit.setDescription(description);
            unit.setPhoto(photo);

            //root unit will not have any parent unit
            if (parentUnitId!=0){
                unit.setParent(UnitDAO.getInstance().get(parentUnitId));
            }

            unit = UnitDAO.getInstance().add(unit);
            return gson.toJson(unit);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }

    @POST
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE})
    @Path("addusers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUsers(@PathParam("id") Integer id, Set<UserBean> users){
        UnitDAO.getInstance().addUsers(id,users);
        UnitBean unitBean = UnitDAO.getInstance().get(id);
        return gson.toJson(unitBean);
    }

    @GET
    @Secure(roles= {RoleEnum.READ})
    @Path("rights/{id}/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserRights(@PathParam("id") Integer unitId, @PathParam("userId") Integer userId){
        UserBean userBean = UserDAO.getInstance().get(userId);
        Set<RightsBean> rights = userBean.getRights();
        //TODO Siddhesh
        /**
         * if the unitId is a child unit of some unit where user has rights
         * then you need to recursively get those rights
         * else userBean.getRights would be empty.
         */
        return gson.toJson(rights.stream().filter(r->r.getUnit().getId()==unitId).collect(Collectors.toSet()));
    }

    @GET
    @Secure(roles = {RoleEnum.READ})
    @Path("users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAuthorizedUsers(@PathParam("id") Integer unitId){
        UserBean currentUser = (UserBean) getSecurityContext().getUserPrincipal();
        Set<UserBean> users = new HashSet<>();
        users.add(currentUser);
        return gson.toJson(users);

    }
}
