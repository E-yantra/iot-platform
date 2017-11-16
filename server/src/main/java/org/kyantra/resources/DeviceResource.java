package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.dao.DeviceDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
@Path("/device")
@Api(value="device")
public class DeviceResource extends BaseResource {
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        DeviceBean bean = DeviceDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @POST
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("description") String description){
        DeviceDAO.getInstance().update(id, name, description);
        DeviceBean bean = DeviceDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    public String delete(@PathParam("id") Integer id){
        try {
            DeviceDAO.getInstance().delete(id);
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
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    @Session
    public String create(@FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("parentThingId") Integer parentThingId,
                         @FormParam("ownerUnitId") Integer ownerUnitId){
        try {
            String s = "Found something";
            //System.out.println(gson.toJson(bean));
            DeviceBean device = new DeviceBean();
            device.setName(name);
            device.setDescription(description);
            //TODO
            //device.setParent(ThingDAO.getInstance().get(parentThingId));
            device.setOwnerUnit(UnitDAO.getInstance().get(ownerUnitId));

            DeviceBean deviceBean = DeviceDAO.getInstance().add(device);
            return gson.toJson(deviceBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
