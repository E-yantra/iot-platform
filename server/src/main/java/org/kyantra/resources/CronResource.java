package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.CronBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.CronDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.exceptionhandling.AccessDeniedException;
import org.kyantra.helper.AuthorizationHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Created by Lenovo on 12-11-2017.
 */

@Path("/cron")
@Api(value="cron")
public class CronResource extends BaseResource {

    @GET
    @Path("get/{id}")
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id) throws AccessDeniedException {
        CronBean bean = CronDAO.getInstance().get(id);
        ThingBean targetThing = bean.getParentThing();
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            return gson.toJson(bean);
        }
        else throw new AccessDeniedException();
    }


    @GET
    @Path("thing/{id}")
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    public String getByThing(@PathParam("id") Integer id) throws AccessDeniedException {
        ThingBean targetThing = ThingDAO.getInstance().get(id);
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();

        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            Set<CronBean> bean = CronDAO.getInstance().getByThingId(id);
            return gson.toJson(bean);
        }
        else throw new AccessDeniedException();
    }


    @DELETE
    @Path("delete/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "deviceAttributes", subjectField = "parentId")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id) throws AccessDeniedException {
        CronBean bean = CronDAO.getInstance().get(id);
        ThingBean targetThing = bean.getParentThing();
        UserBean user = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            try {
                CronDAO.getInstance().delete(id);
                return "{}";
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return "{}";
        }
        else throw new AccessDeniedException();
    }


    @POST
    @Path("create")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String create(
            @FormParam("thingId") Integer thingId,
            @FormParam("name") String cronName,
            @FormParam("cronExpression") String cronExpression,
            @FormParam("desiredState") String desiredState) throws AccessDeniedException {
        ThingBean targetThing = ThingDAO.getInstance().get(thingId);
        UserBean user = (UserBean) getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(user, targetThing)) {
            try {
                CronBean bean = new CronBean();
                bean.setCronName(cronName);
                bean.setCronExpression(cronExpression);
                bean.setDesiredState(desiredState);
                bean.setParentThing(ThingDAO.getInstance().get(thingId));
                bean = CronDAO.getInstance().add(bean);
                return gson.toJson(bean);

            } catch (Throwable t) {
                t.printStackTrace();
            }
            return "{\"success\":false}";
        }
        else throw new AccessDeniedException();
    }
}
