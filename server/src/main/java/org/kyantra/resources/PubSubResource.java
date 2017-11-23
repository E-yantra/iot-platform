package org.kyantra.resources;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTopic;
import org.kyantra.dao.ConfigDAO;
import org.kyantra.interfaces.Session;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/pubsub")
public class PubSubResource extends BaseResource {

    static AWSIotMqttClient client;
    static Map<String,List<String>> messages;

    public PubSubResource() throws AWSIotException {
        super();
        String awsKey = ConfigDAO.getInstance().get("awsKey").getValue();
        String awsSecret = ConfigDAO.getInstance().get("awsSecret").getValue();
        String endpoint = ConfigDAO.getInstance().get("endpoint").getValue();

        if (client==null) {
            messages = new ConcurrentHashMap<>();
            client = new AWSIotMqttClient(endpoint, "server", awsKey, awsSecret);
            client.connect();
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
                messages.putIfAbsent(topic, new ArrayList<>());
                messages.get(topic).add(message.getStringPayload());
                System.out.println(gson.toJson(messages));
            }
        };
        client.unsubscribe(topic);
        client.subscribe(topicObj,3000);
        return gson.toJson("{}");
    }

    @POST
    @Path("messages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getMessages(@FormParam("topic") String topic){

        String resp =  gson.toJson(messages.get(topic));
        messages.clear();
        return resp;
    }
}
