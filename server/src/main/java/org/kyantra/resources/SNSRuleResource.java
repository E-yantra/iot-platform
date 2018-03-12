package org.kyantra.resources;

import org.kyantra.beans.SNSBean;
import org.kyantra.dao.SnsDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sns")
public class SNSRuleResource {

    @GET
    @Path("/create/{topic}")
    @Produces(MediaType.TEXT_PLAIN)
    public String createSNSTopic(@PathParam("topic") String topic) {
        SNSBean snsBean = new SNSBean();
        snsBean.setTopic(topic);
        SnsDAO.getInstance().add(snsBean);
        return "success";
    }


}
