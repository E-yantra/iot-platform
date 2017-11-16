package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.ThingDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    public String update(@PathParam("id") Integer id, ThingBean bean){
        ThingDAO.getInstance().update(id, bean.getName(), bean.getDescription(), bean.getDevices());
        bean = ThingDAO.getInstance().get(bean.getId());
        return gson.toJson(bean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
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
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON) //unit_id
    public String create(ThingBean bean){
        try {
            String s = "Found something";
            System.out.println(gson.toJson(bean));
            ThingBean thingBean = ThingDAO.getInstance().add(bean, bean.getParentUnit());
            return gson.toJson(thingBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
