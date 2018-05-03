package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.aws.ActionHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/rule")
@Api(value = "/rule")
public class RuleResource extends BaseResource {

    @Path("/ddb")
    public DDBRuleResource getDDBRule() {
        return new DDBRuleResource();
    }

    @Path("/sns")
    public SnsRuleResource getSNSRule() {
        return new SnsRuleResource();
    }

    @GET
    @Path("/actions")
    @Produces(MediaType.APPLICATION_JSON)
    public String getActionTypes() {
        return gson.toJson(ActionHelper.getInstance().getActionTypes());
    }

}
