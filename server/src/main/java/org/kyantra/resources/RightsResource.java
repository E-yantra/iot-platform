package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.dao.RightsDAO;
import org.kyantra.dao.UnitDAO;
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

@Path("right")
@Api(value="right")
public class RightsResource extends BaseResource{

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        RightsBean userBean = RightsDAO.getInstance().get(id);
        return gson.toJson(userBean);
    }

    @POST
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "right", subjectField = "parentId")
    public String update(@PathParam("id") Integer id,
                         @FormParam("role") RoleEnum role){
        //TODO: "new UnitBean()" should be user id?
        //Can a user have different rights on separate units, if he is owner of multiple units?
        RightsDAO.getInstance().update(id,new UnitBean(),role);
        RightsBean bean = RightsDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "right", subjectField = "parentId")
    public String delete(@PathParam("id") Integer id){
        try {
            RightsDAO.getInstance().delete(id);
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
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "right", subjectField = "parentId")
    @Session
    public String create(@FormParam("unitId") Integer unitId,
                         @FormParam("role") RoleEnum role){
        try {
            RightsBean rights = new RightsBean();
            rights.setRole(role);
            rights.setUnit(UnitDAO.getInstance().get(unitId));
            RightsBean right = RightsDAO.getInstance().add(rights);

            return gson.toJson(right);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
