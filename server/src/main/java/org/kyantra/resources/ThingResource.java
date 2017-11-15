package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.ThingBean;
import org.kyantra.dao.ThingDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
@Path("/thing")
@Api(value="thing")
public class ThingResource extends BaseResource {
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        ThingBean bean = ThingDAO.getInstance().get(id);
        return gson.toJson(bean);
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
            ThingBean thingBean = ThingDAO.getInstance().add(bean);
            return gson.toJson(thingBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
