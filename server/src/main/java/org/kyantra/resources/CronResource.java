package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.CronBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.dao.CronDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Lenovo on 12-11-2017.
 */
@Path("/cron")
@Api(value="cron")
public class CronResource extends BaseResource {





    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    @Session
    public String create(@FormParam("cronExpression") String cronExpression,
                         @FormParam("desiredState") String desiredState){

        try {
            CronBean bean = new CronBean();
            bean.setCronExpression(cronExpression);
            bean.setDesiredState(desiredState);
            bean = CronDAO.getInstance().add(bean);
            return gson.toJson(bean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }



}
