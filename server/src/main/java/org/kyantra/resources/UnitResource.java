package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.AuthorizationDAO;
import org.kyantra.dao.RightsDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.dao.UserDAO;
import org.kyantra.helper.UnitHelper;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
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

    @Context
    SecurityContext securityContext;

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id) {
        UserBean userBean = (UserBean)securityContext.getUserPrincipal();
        UnitHelper.getInstance().checkAccess();
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

    //
    @POST
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String update(@PathParam("id") Integer id,
                         @FormParam("unitName") String name,
                         @FormParam("description") String description,
                         @FormParam("photo") String photo) throws AccessDeniedException {
        //TODO: can parent unit be changed?
        if (AuthorizationDAO.getInstance().ownsUnit((UserBean)getSecurityContext().getUserPrincipal(),UnitDAO.getInstance().get(id))) {
            UnitDAO.getInstance().update(id, name, description, photo);
            UnitBean unitBean = UnitDAO.getInstance().get(id);
            return gson.toJson(unitBean);
        }
        else{
            throw new AccessDeniedException();
        }
    }

    @DELETE
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id) throws AccessDeniedException{
        if (AuthorizationDAO.getInstance().ownsUnit((UserBean) getSecurityContext().getUserPrincipal(), UnitDAO.getInstance().get(id))) {
            try {
                UnitDAO.getInstance().delete(id);
                return "{}";
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return "{}";
        } else {
            throw new AccessDeniedException();
        }
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Session
    public String create(@FormParam("unitName") String name,
                         @FormParam("description") String description,
                         @FormParam("photo") String photo,
                         @DefaultValue("0") @FormParam("parentUnitId") Integer parentUnitId) throws AccessDeniedException{
        if (AuthorizationDAO.getInstance().ownsUnit((UserBean)getSecurityContext().getUserPrincipal(),UnitDAO.getInstance().get(parentUnitId))) {
            try {
                String s = "Found something";
                //System.out.println(gson.toJson(childUnit));
                UnitBean unit = new UnitBean();
                unit.setUnitName(name);
                unit.setDescription(description);
                unit.setPhoto(photo);

                //root unit will not have any parent unit
                if (parentUnitId != 0) {
                    unit.setParent(UnitDAO.getInstance().get(parentUnitId));
                }

                unit = UnitDAO.getInstance().add(unit);
                return gson.toJson(unit);

            } catch (Throwable t) {
                t.printStackTrace();
            }
            return "{\"success\":false}";
        }else{
            throw new AccessDeniedException();
        }
    }

    //
    @POST
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE})
    @Path("addusers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUsers(@PathParam("id") Integer id, Set<UserBean> users) throws AccessDeniedException{
        if (AuthorizationDAO.getInstance().ownsUnit((UserBean)getSecurityContext().getUserPrincipal(),UnitDAO.getInstance().get(id))) {
            UnitDAO.getInstance().addUsers(id, users);
            UnitBean unitBean = UnitDAO.getInstance().get(id);
            return gson.toJson(unitBean);
        }else{
            throw new AccessDeniedException();
        }
    }

    @GET
    @Session
    @Secure(roles= {RoleEnum.READ})
    @Path("rights/{id}/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserRights(@PathParam("id") Integer unitId,
                                @PathParam("userId") Integer userId) {
        UserBean userBean = UserDAO.getInstance().get(userId);
        Set<RightsBean> rights = RightsDAO.getInstance().getRightsByUser(userBean);
        Set<UnitBean> allBeans = new HashSet<>();
        rights.forEach(r->{
            allBeans.addAll(UnitHelper.getInstance().getAllParents(r.getUnit()));
        });
        return gson.toJson(rights.stream().filter(r->allBeans.contains(r.getUnit())).collect(Collectors.toSet()));
    }

    @GET
    @Session
    @Secure(roles = {RoleEnum.READ})
    @Path("users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAuthorizedUsers(@PathParam("id") Integer unitId) {
        UnitBean unitBean = UnitDAO.getInstance().get(unitId);
        Set<RightsBean> rights = RightsDAO.getInstance().getRightsByUnit(unitBean);
        while(unitBean.getParent()!=null){
            unitBean = unitBean.getParent();
            rights.addAll(RightsDAO.getInstance().getRightsByUnit(unitBean));
        }
        return gson.toJson(rights);
    }

    @GET
    @Session
    @Secure(roles = {RoleEnum.READ})
    @Path("subunits/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSubunits(@PathParam("id") Integer unitId) {
        UnitBean unitBean = UnitDAO.getInstance().get(unitId);
        return gson.toJson(unitBean.getSubunits());
    }

    @GET
    @Session
    @Path("parents/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllParents(@PathParam("id") Integer unitId) {
        UnitBean unitBean = UnitDAO.getInstance().get(unitId);
        Set<UnitBean> allParents = UnitHelper.getInstance().getAllParents(unitBean);
        return gson.toJson(allParents);
    }
}
