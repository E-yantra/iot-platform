package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.aws.ActionHelper;

import javax.ws.rs.*;

@Path("/rule")
@Api(value = "rule")
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
    public String getActionTypes() {
        return gson.toJson(ActionHelper.getInstance().getActionTypes());
    }
}
