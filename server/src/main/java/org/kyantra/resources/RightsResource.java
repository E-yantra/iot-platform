package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.RightsDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.dao.UserDAO;
import org.kyantra.exceptionhandling.DataNotFoundException;
import org.kyantra.exceptionhandling.ExceptionMessage;
import org.kyantra.helper.AuthorizationHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

// TODO: 5/25/18 Add proper authorization on it
@Path("right")
@Api(value="right")
public class RightsResource extends BaseResource {

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id) {
        RightsBean rightsBean = RightsDAO.getInstance().get(id);

        if (rightsBean == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        UnitBean targetUnit = rightsBean.getUnit();
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (!AuthorizationHelper.getInstance().checkAccess(userBean, targetUnit))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        return gson.toJson(rightsBean);
    }

    // TODO: 5/31/18 Test
    @POST
    @Path("update/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "right", subjectField = "parentId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String update(@PathParam("id") Integer id,
                         @FormParam("role") RoleEnum role) {
        //TODO: "new UnitBean()" should be user id?
        RightsBean rightsBean = RightsDAO.getInstance().get(id);

        if (rightsBean == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        UnitBean targetUnit = rightsBean.getUnit();
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (!AuthorizationHelper.getInstance().checkAccess(userBean, targetUnit))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        // remove UnitBean (second) parameter in update
        // allow to change type of role of a user on a unit if the user doing this has right to the unit
        rightsBean = RightsDAO.getInstance().update(id, role);
        return gson.toJson(rightsBean);
    }


    @DELETE
    @Path("delete/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "right", subjectField = "parentId")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id) {
        RightsBean rightsBean = RightsDAO.getInstance().get(id);

        if (rightsBean == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        UnitBean targetUnit = rightsBean.getUnit();
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (!AuthorizationHelper.getInstance().checkAccess(userBean, targetUnit))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        try {
            RightsDAO.getInstance().delete(id);
            return "{}";
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }


    @POST
    @Path("create")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "right", subjectField = "parentId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String create(@FormParam("unitId") Integer unitId,
                         @FormParam("userId") Integer userId,
                         @FormParam("Role") RoleEnum role) {

        UnitBean targetUnit = UnitDAO.getInstance().get(unitId);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetUnit == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (!AuthorizationHelper.getInstance().checkAccess(userBean, targetUnit))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        try {
            RightsBean rights = new RightsBean();
            rights.setRole(role);
            // different from above user
            rights.setUser(UserDAO.getInstance().get(userId));
            rights.setUnit(targetUnit);
            RightsBean right = RightsDAO.getInstance().add(rights);

            return gson.toJson(right);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
