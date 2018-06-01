package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.*;
import org.kyantra.dao.DeviceDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.exceptionhandling.AccessDeniedException;
import org.kyantra.helper.AuthorizationHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

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
    @Session
    @Secure(roles = {RoleEnum.ALL, RoleEnum.WRITE, RoleEnum.READ})
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id) throws AccessDeniedException {
        DeviceBean deviceBean = DeviceDAO.getInstance().get(id);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(userBean, deviceBean))
            return gson.toJson(deviceBean);
        else throw new AccessDeniedException();
    }


    @GET
    @Path("thing/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL, RoleEnum.WRITE, RoleEnum.READ})
    @Produces(MediaType.APPLICATION_JSON)
    public String getByThing(@PathParam("id") Integer id) throws AccessDeniedException {
        ThingBean thingBean = ThingDAO.getInstance().get(id);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(userBean, thingBean)) {
            Set<DeviceBean> beans = DeviceDAO.getInstance().getByThing(id);
            return gson.toJson(beans);
        }
        else throw new AccessDeniedException();
    }

    @PUT
    @Path("update/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("description") String description) throws AccessDeniedException {
        //TODO: required?
        DeviceBean deviceBean = DeviceDAO.getInstance().get(id);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(userBean, deviceBean)) {
            DeviceDAO.getInstance().update(id, name, description);
            DeviceBean bean = DeviceDAO.getInstance().get(id);
            return gson.toJson(bean);
        }
        else throw new AccessDeniedException();
    }


    @DELETE
    @Path("delete/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id) throws AccessDeniedException{
        DeviceBean deviceBean = DeviceDAO.getInstance().get(id);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(userBean, deviceBean)) {
            // TODO: 5/24/18 return proper response message instead of {}
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
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "device", subjectField = "parentId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String create(@FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("parentThingId") Integer parentThingId,
                         @FormParam("ownerUnitId") Integer ownerUnitId) throws AccessDeniedException {
        UnitBean targetUnit = UnitDAO.getInstance().get(ownerUnitId);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(userBean, targetUnit)) {
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
        }
        else throw new AccessDeniedException();
    }

    @GET
    @Path("generate/{id}")
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.TEXT_PLAIN)
    public String generate(@PathParam("id")Integer thingId) throws AccessDeniedException {
        ThingBean thing = ThingDAO.getInstance().get(thingId);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        if (AuthorizationHelper.getInstance().checkAccess(userBean, thing)) {
            Set<DeviceBean> devices = thing.getDevices();

            ShadowBean shadowBean = new ShadowBean();
            shadowBean.setThingBean(thing);
            StringBuilder sb = new StringBuilder();
            sb.append("ThingID:");
            sb.append("thing" + thing.getId());
            sb.append("\n");
            sb.append("Subscribe to Delta Topic : " + shadowBean.getDeltaTopic());
            sb.append("\n");
            sb.append("Publish to reporting Topic : " + shadowBean.getUpdateTopic());
            sb.append("\n");
            sb.append("{\n" +
                    "   \"state\": {\n" +
                    "       \"reported\": {\n");
            for (DeviceBean deviceBean : devices) {
                List<DeviceAttributeBean> atts = deviceBean.getDeviceAttributes();

                for (DeviceAttributeBean att : atts) {
                    sb.append("         " + "\"device" + deviceBean.getId() + "." + att.getId() + "\": <" + att.getType() + "-" + att.getName() + ">,\n");
                }
            }
            sb.append("     }\n");
            sb.append(" }\n");
            sb.append("}");
            return sb.toString();
        }
        else throw new AccessDeniedException();
    }
}
