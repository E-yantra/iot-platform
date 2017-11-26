package org.kyantra.resources;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.beans.ShadowBean;
import org.kyantra.beans.ThingBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.DeviceAttributeDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.AwsIotHelper;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/pubsub")
public class PubSubResource extends BaseResource {

    static AWSIotMqttClient client;
    static Map<String,Deque<String>> messages  = new ConcurrentHashMap<>();

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

    @POST
    @Path("publish")
    @Session
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String publish(@FormParam("topic") String topic, @FormParam("payload") String payload) throws AWSIotException {
        client.publish(topic,payload);
        return gson.toJson("{}");
    }

    @GET
    @Path("shadow/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getThingShadow(@PathParam("id") Integer thingId) throws AWSIotException {
        ThingBean thingBean = ThingDAO.getInstance().get(thingId);
        String shadowName = "thing"+thingBean.getId();
        AWSIotMqttClient client = AwsIotHelper.getMQTT();
        AWSIotDevice device = new AWSIotDevice(shadowName);
        client.attach(device);
        client.connect();
        String state = device.get();
        client.disconnect();
        return state;
    }


    @POST
    @Path("value/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String setValue(@FormParam("value") String value, @PathParam("id") Integer id) throws AWSIotException {
        DeviceAttributeBean att = DeviceAttributeDAO.getInstance().get(id);
        ShadowBean bean = new ShadowBean();
        bean.setThingBean(att.getParentDevice().getParentThing());
        if(att.getType().equals("Boolean")) {
            bean.setDesired(att, att.getId() + "", value.equals("1")?1:0);
        }
        else if(att.getType().equals("Double")) {
            bean.setDesired(att, att.getId() + "", Double.parseDouble(value));
        }else{
            bean.setDesired(att, att.getId() + "", value);
        }

        client.publish(bean.getUpdateTopic(),gson.toJson(bean.getMap()));
        return  gson.toJson(bean.getMap());

    }

    @POST
    @Path("messages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getMessages(@FormParam("topic") String topic){
        String resp =  gson.toJson(messages.get(topic+"/get"));
        messages.get(topic).clear();
        return resp;
    }
}
