package org.kyantra.resources;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.*;
import io.swagger.annotations.Api;
import org.glassfish.hk2.api.messaging.Topic;
import org.kyantra.beans.ThingBean;
import org.kyantra.dao.ThingDAO;
import org.kyantra.utils.AwsIotHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/rule")
@Api(value = "rule")
public class RuleResource extends BaseResource {

    @Path("/ddb")
    public DDBRuleResource getDDBRule() {
        return new DDBRuleResource();
    }

    @Path("/sns")
    public SNSRuleResource getSNSRule() {
        return new SNSRuleResource();
    }
}
