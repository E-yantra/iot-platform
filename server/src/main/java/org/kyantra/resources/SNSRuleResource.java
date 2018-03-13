package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.SNSBean;
import org.kyantra.beans.SNSSubscriptionBean;
import org.kyantra.dao.SNSSubscriptionDAO;
import org.kyantra.dao.SnsDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sns")
@Api(value="sns")
public class SNSRuleResource extends BaseResource{

    @GET
    @Path("/create/{topic}")
    @Produces(MediaType.TEXT_PLAIN)
    public String createSNSTopic(@PathParam("topic") String topic) {
        SNSBean snsBean = new SNSBean();
        snsBean.setTopic(topic);
        SnsDAO.getInstance().add(snsBean);
        return "success";
    }

    @POST
    @Path("/subscribe/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String subscribeSNSTopic(@PathParam("id") Integer parentId,
                                    @FormParam("type") String type,
                                    @FormParam("value") String value) {
        SNSSubscriptionBean snsSubscriptionBean = new SNSSubscriptionBean();
        snsSubscriptionBean.setType(type);
        snsSubscriptionBean.setValue(value);
        snsSubscriptionBean.setParentSNSBean(SnsDAO.getInstance().get(parentId));
        SNSSubscriptionDAO.getInstance().add(snsSubscriptionBean);
        return "success";
    }

}
