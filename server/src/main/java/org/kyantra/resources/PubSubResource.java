package org.kyantra.resources;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTopic;
import org.kyantra.beans.DeviceAttributeBean;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.dao.DeviceAttributeDAO;
import org.kyantra.interfaces.Session;

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
import java.util.concurrent.ConcurrentLinkedDeque;

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

    @POST
    @Path("subscribe")
    @Session
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String subscribe(@FormParam("topic") String topic) throws AWSIotException {
        AWSIotTopic topicObj = new AWSIotTopic(topic){

            @Override
            public void onMessage(AWSIotMessage message) {
                super.onMessage(message);
                messages.putIfAbsent(topic, new ConcurrentLinkedDeque<>());
                messages.get(topic).add(message.getStringPayload());
                if(messages.get(topic).size()>100){
                    messages.get(topic).clear();
                }
            }
        };
        client.unsubscribe(topic);
        client.subscribe(topicObj,3000);
        return gson.toJson("{}");
    }

    @GET
    @Path("subscribe/{id}")
    @Session
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String subscribe(@PathParam("id") Integer id) throws AWSIotException {
        DeviceAttributeBean att = DeviceAttributeDAO.getInstance().get(id);
        String topic = DeviceAttributeDAO.getInstance().getTopic(att)+"/get";
        AWSIotTopic topicObj = new AWSIotTopic(topic){

            @Override
            public void onMessage(AWSIotMessage message) {
                super.onMessage(message);
                messages.putIfAbsent(topic, new ConcurrentLinkedDeque<>());
                messages.get(topic).add(message.getStringPayload());
                if(messages.get(topic).size()>100){
                    messages.get(topic).clear();
                }
            }
        };
        client.unsubscribe(topic);
        client.subscribe(topicObj,3000);
        System.out.println("Subscribed to "+topic);
        return gson.toJson("{}");
    }

    @GET
    @Path("value/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getValue(@PathParam("id") Integer id){
        DeviceAttributeBean att = DeviceAttributeDAO.getInstance().get(id);
        String topic = DeviceAttributeDAO.getInstance().getTopic(att)+"/get";
        messages.putIfAbsent(topic,new ConcurrentLinkedDeque<>());
        if(messages.isEmpty()){
            return "";
        }else{
            return messages.get(topic).removeLast();
        }


    }

    @POST
    @Path("value/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String setValue(@FormParam("value") String value, @PathParam("id") Integer id) throws AWSIotException {
        DeviceAttributeBean att = DeviceAttributeDAO.getInstance().get(id);
        String topic = DeviceAttributeDAO.getInstance().getTopic(att)+"/set";
        client.publish(topic,value);
        return  "";

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
