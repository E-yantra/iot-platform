package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.glassfish.jersey.server.ContainerRequest;
import org.hibernate.Session;
import org.kyantra.beans.CredentialBean;
import org.kyantra.beans.SessionBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.SessionDAO;
import org.kyantra.dao.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Path("auth")
@Api("auth")
public class AuthResource extends BaseResource {

    @Context
    HttpServletRequest request;

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String authenticate(@FormParam("email") String email,
                               @FormParam("password") String password,
                               @Context HttpServletRequest request,
                               @Context ContainerRequest containerRequest){

        try {

            CredentialBean credentialBean = new CredentialBean();
            credentialBean.setEmail(email);
            credentialBean.setPassword(password);

            // Based on
            // https://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
            if (credentialBean.getEmail() != null
                    && credentialBean.getPassword() != null) {
                //do db based auth and if it is true then Create a new SessionBean for this user.
                //Generate new String token using UUID
                SessionBean sessionBean = new SessionBean();
                UserBean userBean = UserDAO.getInstance().getByEmail(credentialBean.getEmail());

                if (userBean!=null && userBean.getPassword().equals(credentialBean.getPassword())) {

                    sessionBean.setUser(userBean);
                    sessionBean.setToken(UUID.randomUUID().toString());
                    SessionBean resultBean = SessionDAO.getInstance().add(sessionBean);
                    return gson.toJson(resultBean);
                }

            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        Map<String,String> map = new HashMap<>();
        return gson.toJson(map); //suggests failed authentication.
    }


    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String signup(@FormParam("name") String name,
                         @FormParam("email") String email,
                               @FormParam("password") String password,
                               @Context HttpServletRequest request,
                               @Context ContainerRequest containerRequest) throws Exception {

        UserBean userBean = UserDAO.getInstance().getByEmail(email.toLowerCase());
        if(userBean!=null){
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("error","User already exists");
            return gson.toJson(map);
        }
        userBean = new UserBean();
        userBean.setName(name);
        userBean.setPassword(password);
        userBean.setEmail(email);
        UserDAO.getInstance().add(userBean);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return gson.toJson(map); //suggests failed authentication.
    }
}
