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

import javax.ws.rs.*;
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
    @Session
    @Secure(roles = {RoleEnum.ALL, RoleEnum.WRITE, RoleEnum.READ})
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id) throws AccessDeniedException {
        UnitBean unitBean = UnitDAO.getInstance().get(id);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (!UnitHelper.getInstance().checkAccess(userBean, unitBean))
            throw new AccessDeniedException();

        return gson.toJson(unitBean);
    }

    @GET
    @Path("list/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@PathParam("page") Integer page) {
        Principal principal = getSecurityContext().getUserPrincipal();
        UserBean currentUser = (UserBean) principal;
        List<UnitBean> users = UnitDAO.getInstance().list(page,limit);
        return gson.toJson(users);
    }


    @PUT
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
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(id);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
            UnitDAO.getInstance().update(id, name, description, photo);
            return gson.toJson(unitBean);
        }
        else {
            throw new AccessDeniedException();
        }
    }

    @DELETE
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id) throws AccessDeniedException {
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(id);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
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

        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(parentUnitId);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
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
        } else {
            throw new AccessDeniedException();
        }
    }


    @POST
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE})
    @Path("addusers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUsers(@PathParam("id") Integer id, Set<UserBean> users) throws AccessDeniedException{
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(id);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
            UnitDAO.getInstance().addUsers(id, users);
            return gson.toJson(unitBean);
        } else {
            throw new AccessDeniedException();
        }
    }

    @GET
    @Session
    @Secure(roles= {RoleEnum.READ})
    @Path("rights/{id}/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserRights(@PathParam("id") Integer unitId,
                                @PathParam("userId") Integer userId) throws AccessDeniedException {
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(unitId);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
            Set<RightsBean> rights = RightsDAO.getInstance().getRightsByUser(userBean);
            Set<UnitBean> allBeans = new HashSet<>();
            rights.forEach(r -> {
                allBeans.addAll(UnitHelper.getInstance().getAllParents(r.getUnit()));
            });
            return gson.toJson(rights.stream().filter(r -> allBeans.contains(r.getUnit())).collect(Collectors.toSet()));
        }
        else {
            throw new AccessDeniedException();
        }
    }

    @GET
    @Session
    @Secure(roles = {RoleEnum.READ})
    @Path("users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAuthorizedUsers(@PathParam("id") Integer unitId) throws AccessDeniedException {
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(unitId);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
            Set<RightsBean> rights = RightsDAO.getInstance().getRightsByUnit(unitBean);
            while (unitBean.getParent() != null) {
                unitBean = unitBean.getParent();
                rights.addAll(RightsDAO.getInstance().getRightsByUnit(unitBean));
            }
            return gson.toJson(rights);
        }
        else {
            throw new AccessDeniedException();
        }
    }

    @GET
    @Session
    @Secure(roles = {RoleEnum.READ})
    @Path("subunits/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSubunits(@PathParam("id") Integer unitId) throws AccessDeniedException {
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(unitId);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
            return gson.toJson(unitBean.getSubunits());
        }
        else {
            throw new AccessDeniedException();
        }
    }

    @GET
    @Session
    @Path("parents/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllParents(@PathParam("id") Integer unitId) throws AccessDeniedException {
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean unitBean = UnitDAO.getInstance().get(unitId);

        if (UnitHelper.getInstance().checkAccess(userBean, unitBean)) {
            Set<UnitBean> allParents = UnitHelper.getInstance().getAllParents(unitBean);
            return gson.toJson(allParents);
        }
        else {
            throw new AccessDeniedException();
        }
    }
}
