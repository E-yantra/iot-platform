package org.kyantra.resources;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.CreateThingRequest;
import com.amazonaws.services.iot.model.CreateThingResult;
import io.swagger.annotations.Api;
import org.kyantra.beans.*;
import org.kyantra.dao.AuthorizationDAO;
import org.kyantra.dao.DeviceDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.AwsIotHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
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
                         @FormParam("description") String description) throws AccessDeniedException{
        //TODO: required?
        if (AuthorizationDAO.getInstance().ownsDevice((UserBean)getSecurityContext().getUserPrincipal(),DeviceDAO.getInstance().get(id))) {
            DeviceDAO.getInstance().update(id, name, description);
            DeviceBean bean = DeviceDAO.getInstance().get(id);
            return gson.toJson(bean);
        }        else {
            throw new AccessDeniedException();
        }
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    public String delete(@PathParam("id") Integer id) throws AccessDeniedException{
        if (AuthorizationDAO.getInstance().ownsDevice((UserBean)getSecurityContext().getUserPrincipal(),DeviceDAO.getInstance().get(id))) {
            try {
                DeviceDAO.getInstance().delete(id);
                return "{}";
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return "{}";
        }else {
            throw new AccessDeniedException();
        }
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
                         @FormParam("ownerUnitId") Integer ownerUnitId) throws AccessDeniedException{
        if (AuthorizationDAO.getInstance().ownsThing((UserBean)getSecurityContext().getUserPrincipal(),ThingDAO.getInstance().get(parentThingId))) {
            try {
                DeviceBean device = new DeviceBean();
                device.setName(name);
                device.setDescription(description);
                device.setOwnerUnit(UnitDAO.getInstance().get(ownerUnitId));
                device.setParentThing(ThingDAO.getInstance().get(parentThingId));

                DeviceBean deviceBean = DeviceDAO.getInstance().add(device);
                return gson.toJson(deviceBean);

            } catch (Throwable t) {
                t.printStackTrace();
            }
            return "{\"success\":false}";
        }else {
            throw new AccessDeniedException();
        }
    }

    @GET
    @Path("generate/{id}")
    @Session
    @Produces(MediaType.TEXT_PLAIN)
    public String generate(@PathParam("id")Integer thingId){
//        ThingBean thing = ThingDAO.getInstance().get(thingId);
//        Set<DeviceBean> devices = thing.getDevices();
//
//        ShadowBean shadowBean = new ShadowBean();
//        shadowBean.setThingBean(thing);
//        StringBuilder sb = new StringBuilder();
//        sb.append("clientId:");
//        sb.append("thing"+ thing.getId());
//        sb.append("\n");
//        sb.append("Subscribe to Delta Topic : "+shadowBean.getDeltaTopic());
//        sb.append("\n");
//        sb.append("Publish to reporting Topic : "+shadowBean.getUpdateTopic());
//        sb.append("\n");
//
//        for(DeviceBean deviceBean:devices){
//            List<DeviceAttributeBean> atts = deviceBean.getDeviceAttributes();
//            sb.append("For Device "+deviceBean.getName());
//            sb.append("\n");
//            sb.append("Use the following property names");
//            sb.append("\n\n");
//
//            for(DeviceAttributeBean att:atts){
//                sb.append("device"+deviceBean.getId()+"."+att.getId()+"\t"+att.getType()+" //"+deviceBean.getName()+" "+att.getName());
//                sb.append("\n");
//            }
//        }
//
//        return sb.toString();
        ThingBean thing = ThingDAO.getInstance().get(thingId);
        Set<DeviceBean> devices = thing.getDevices();

        ShadowBean shadowBean = new ShadowBean();
        shadowBean.setThingBean(thing);
        StringBuilder sb = new StringBuilder();
        sb.append("ThingID:");
        sb.append("thing"+ thing.getId());
        sb.append("\n");
        sb.append("Subscribe to Delta Topic : "+shadowBean.getDeltaTopic());
        sb.append("\n");
        sb.append("Publish to reporting Topic : "+shadowBean.getUpdateTopic());
        sb.append("\n");
        sb.append("{\n"+
                "   \"state\": {\n"+
                "       \"reported\": {\n");
        for(DeviceBean deviceBean:devices){
            List<DeviceAttributeBean> atts = deviceBean.getDeviceAttributes();

            for(DeviceAttributeBean att:atts){
                sb.append("         "+"\"device"+deviceBean.getId()+"."+att.getId()+"\": <"+att.getType()+"-"+att.getName()+">,\n");
            }
        }
        sb.append("     }\n");
        sb.append(" }\n");
        sb.append("}");
        return sb.toString();
    }
}
