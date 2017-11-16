package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.ThingDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
@Path("/thing")
@Api(value="thing")
public class ThingResource extends BaseResource {

    int limit = 10;

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        ThingBean bean = ThingDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @GET
    @Path("list/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@PathParam("page") Integer page){
        Principal principal = getSecurityContext().getUserPrincipal();
        UserBean currentUser = (UserBean) principal;
        List<ThingBean> users = ThingDAO.getInstance().list(page,limit);
        return gson.toJson(users);
    }

    @POST
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "thing", subjectField = "parentId")
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("ip") String ip){
        //TODO: create/update will only add/edit current entity values and not its parent/children attributes
        ThingDAO.getInstance().update(id, name, description, ip);
        ThingBean bean = ThingDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "thing", subjectField = "parentId")
    public String delete(@PathParam("id") Integer id){
        try {
            ThingDAO.getInstance().delete(id);
            return "{}";
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }

    @POST
    @Session
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) //unit_id
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "thing", subjectField = "parentId")
    public String create(@FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("ip") String ip,
                         @FormParam("parentUnitId") Integer parentUnitId){
        try {
            String s = "Create thing";
            //System.out.println(gson.toJson(bean));
            ThingBean thing = new ThingBean();
            thing.setName(name);
            thing.setDescription(description);
            thing.setIp(ip);
            thing.setParentUnit(UnitDAO.getInstance().get(parentUnitId));

            ThingBean thingBean = ThingDAO.getInstance().add(thing);
            return gson.toJson(thingBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
