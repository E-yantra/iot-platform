package org.kyantra.resources;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iotdata.AWSIotData;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import org.kyantra.beans.*;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.DeviceAttributeDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.exceptionhandling.DataNotFoundException;
import org.kyantra.exceptionhandling.ExceptionMessage;
import org.kyantra.helper.AuthorizationHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.AwsIotHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/pubsub")
public class PubSubResource extends BaseResource {

    static AWSIotMqttClient client;
    static Map<String,Deque<String>> messages  = new ConcurrentHashMap<>();

    // TODO: 6/6/18 Some issues with pubsub resource
    public PubSubResource() throws AWSIotException {
        super();
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        String endpoint = ConfigDAO.getInstance().get("endpoint").getValue();

        synchronized (messages) {
            if (client == null) {
                client = new AWSIotMqttClient(endpoint, "server", awsKey, awsSecret);
                client.connect();
            }
        }
    }

    // TODO: 5/31/18 What is this?
    // TODO: 6/6/18 Commenting because of probable security issues
    // A user could try to access thing that he doesn't have access via our platform
    // by typing topic as that things mqtt address
//    @POST
//    @Path("publish")
//    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL})
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String publish(@FormParam("topic") String topic,
//                          @FormParam("payload") String payload) throws AWSIotException {
//        client.publish(topic, payload);
//        return gson.toJson("{}");
//    }


    @GET
    @Path("shadow/{id}")
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getThingShadow(@PathParam("id") Integer thingId) {
        ThingBean thingBean = ThingDAO.getInstance().get(thingId);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (thingBean == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (!AuthorizationHelper.getInstance().checkAccess(userBean, thingBean))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        String shadowName = "thing" + thingBean.getId();

        AWSIotData client1 = AwsIotHelper.getIotDataClient();
        GetThingShadowResult result = client1.getThingShadow(new GetThingShadowRequest()
                    .withThingName(shadowName));
        byte[] bytes = new byte[result.getPayload().remaining()];
        result.getPayload().get(bytes);
        String resultString = new String(bytes);
        client1.shutdown();

        return resultString;
    }


    // TODO: 6/6/18 Owner unit is always null; Using parent device as quick fix
    @POST
    @Path("value/{id}")
    @Secure(roles = {RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String setValue(@FormParam("value") String value, @PathParam("id") Integer id) throws AWSIotException {

        DeviceAttributeBean att = DeviceAttributeDAO.getInstance().get(id);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (att == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        //
        if (!AuthorizationHelper.getInstance().checkAccess(userBean, att))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        ShadowBean bean = new ShadowBean();
        bean.setThingBean(att.getParentDevice().getParentThing());

        if(att.getType().equals("Boolean")) {
            bean.setDesired(att, att.getId() + "", value.equals("1")?1:0);
        }
        else if(att.getType().equals("Double")) {
            bean.setDesired(att, att.getId() + "", Double.parseDouble(value));
        }
        else if(att.getType().equals("Integer")) {
            bean.setDesired(att, att.getId() + "", Integer.parseInt(value));
        }
        else {
            bean.setDesired(att, att.getId() + "", value);
        }

        client.publish(bean.getUpdateTopic(),gson.toJson(bean.getMap()));
        return  gson.toJson(bean.getMap());
    }


//    // TODO: 5/31/18 What is this?
    // TODO: 6/6/18 Commenting because of probable security issues
    // A user could try to access thing that he doesn't have access via our platform
    // by typing topic as that things mqtt address
//    @POST
//    @Path("messages")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String getMessages(@FormParam("topic") String topic) {
//        String resp =  gson.toJson(messages.get(topic+"/get"));
//        messages.get(topic).clear();
//        return resp;
//    }
}
