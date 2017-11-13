package org.kyantra.resources;

import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HomeResource extends BaseResource {

    @GET
    @Path("/")
    @Template(name = "/index.ftl")
    public Map<String, Object> index() {
        final Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

}
