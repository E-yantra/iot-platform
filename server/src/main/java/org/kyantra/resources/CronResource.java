package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.CronBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.dao.CronDAO;
import org.kyantra.dao.ThingDAO;
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
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        CronBean bean = CronDAO.getInstance().get(id);
        return gson.toJson(bean);
    }


    @GET
    @Path("thing/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByThing(@PathParam("id") Integer id){
        Set<CronBean> bean = CronDAO.getInstance().getByThingId(id);
        return gson.toJson(bean);
    }


    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "deviceAttributes", subjectField = "parentId")
    @Session
    public String delete(@PathParam("id") Integer id){
        try {
            CronDAO.getInstance().delete(id);
            return "{}";
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Session
    public String create(
            @FormParam("thingId") Integer thingId,
            @FormParam("cronExpression") String cronExpression,
            @FormParam("desiredState") String desiredState){

        try {
            CronBean bean = new CronBean();
            bean.setCronExpression(cronExpression);
            bean.setDesiredState(desiredState);
            bean.setParentThing(ThingDAO.getInstance().get(thingId));
            bean = CronDAO.getInstance().add(bean);
            return gson.toJson(bean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }



}
