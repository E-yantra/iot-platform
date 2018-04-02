package org.kyantra.resources;

import org.kyantra.beans.SnsBean;
import org.kyantra.dao.SnsDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sns")
public class SnsResource extends BaseResource {

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSnsBean(@PathParam("id") Integer id) {
        SnsBean snsBean = SnsDAO.getInstance().get(id);
        return gson.toJson(snsBean);
    }
}
