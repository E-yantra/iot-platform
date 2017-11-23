package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.ConfigBean;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.DeviceAttributeDAO;
import org.kyantra.dao.DeviceDAO;
import org.kyantra.dao.ThingDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


    @GET
    @Path("thing/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByThing(@PathParam("id") Integer id){
        Set<DeviceBean> beans = DeviceDAO.getInstance().getByThing(id);
        return gson.toJson(beans);
    }

    @POST
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    @Session
    public String create(@FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("parentThingId") Integer parentThingId,
                         @FormParam("ownerUnitId") Integer ownerUnitId){
        try {

            DeviceBean device = new DeviceBean();
            device.setName(name);
            device.setDescription(description);
            device.setOwnerUnit(UnitDAO.getInstance().get(ownerUnitId));
            device.setParentThing(ThingDAO.getInstance().get(parentThingId));

            DeviceBean deviceBean = DeviceDAO.getInstance().add(device);
            return gson.toJson(deviceBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }

    @GET
    @Path("generate/{id}")
    @Session
    @Produces(MediaType.APPLICATION_JSON)
    public String generate(@PathParam("id")Integer thingId){
        ThingBean thing = ThingDAO.getInstance().get(thingId);
        List<DeviceBean> devices = thing.getDevices();
        Map<String,String> publishTo = new HashMap<>();
        Map<String,String> subscribeFrom = new HashMap<>();

        devices.forEach(d->{
            List<DeviceAttributeBean> attributes = d.getDeviceAttributes();
            for (DeviceAttributeBean att:attributes){
                if(att.getActuator()){
                    subscribeFrom.put(d.getName()+"-"+att.getName(), DeviceAttributeDAO.getInstance().getTopic(att)+"/set");
                }
                publishTo.put(d.getName()+"-"+att.getName(), DeviceAttributeDAO.getInstance().getTopic(att)+"/get");
            }
        });
        Map<String,Object> ret = new HashMap<>();
        ret.put("publishTo",publishTo);
        ret.put("subscribeTo",subscribeFrom);
        ret.put("clientId","thing_"+thingId);
        ConfigBean configBean = ConfigDAO.getInstance().get("endPoint");
        ret.put("endPoint",configBean.getValue());
        return gson.toJson(ret);
    }
}
