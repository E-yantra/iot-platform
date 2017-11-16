package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.UnitBean;
import org.kyantra.dao.DeviceAttributeDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
@Path("/attribute")
@Api(value="attribute")
public class DeviceAttributeResource extends BaseResource {
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        DeviceAttributeBean bean = DeviceAttributeDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @POST
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String update(@PathParam("id") Integer id, DeviceAttributeBean bean){
        DeviceAttributeDAO.getInstance().update(id, bean.getName(), bean.getType(), bean.getDef());
        bean = DeviceAttributeDAO.getInstance().get(bean.getId());
        return gson.toJson(bean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id){
        try {
            DeviceAttributeDAO.getInstance().delete(id);
            return "{}";
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String create(DeviceAttributeBean bean, UnitBean currentUnit){
        try {
            String s = "Found something";
            System.out.println(gson.toJson(bean));
            DeviceAttributeBean deviceAttributeBean = DeviceAttributeDAO.getInstance().add(bean,currentUnit);
            return gson.toJson(deviceAttributeBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
