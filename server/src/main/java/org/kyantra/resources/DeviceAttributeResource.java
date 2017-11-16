package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.dao.DeviceAttributeDAO;
import org.kyantra.dao.DeviceDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

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
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "deviceAttributes", subjectField = "parentId")
    @Session
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("type") String type,
                         @FormParam("def") String def){
        DeviceAttributeDAO.getInstance().update(id, name, type,def);
        DeviceAttributeBean bean = DeviceAttributeDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "deviceAttributes", subjectField = "parentId")
    @Session
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
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "deviceAttributes", subjectField = "parentId")
    @Session
    public String create(@FormParam("name") String name,
                         @FormParam("type") String type,
                         @FormParam("def") String def,
                         @FormParam("parentDeviceId") Integer parentDeviceId,
                         @FormParam("ownerUnitId") Integer ownerUnitId){
        try {
            String s = "Found something";
            //System.out.println(gson.toJson(bean));
            DeviceAttributeBean deviceAttribute = new DeviceAttributeBean();
            deviceAttribute.setName(name);
            deviceAttribute.setType(type);
            deviceAttribute.setDef(def);
            //TODO
            //deviceAttribute.setParent(DeviceDAO.getInstance().get(parentDeviceId));
            deviceAttribute.setOwnerUnit(UnitDAO.getInstance().get(ownerUnitId));

            DeviceAttributeBean deviceAttributeBean = DeviceAttributeDAO.getInstance().add(deviceAttribute);
            return gson.toJson(deviceAttributeBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
