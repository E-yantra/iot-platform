package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.UserDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;

@Path("/user")
@Api(value="user")
public class UserResource extends BaseResource{

    int limit = 10;

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.READ})
    public String get(@PathParam("id") Integer id){
        UserBean userBean = UserDAO.getInstance().get(id);

        Principal principal = getSecurityContext().getUserPrincipal();
        UserBean currentUser = (UserBean) principal;
        //TODO code to check if currentUser has permission to read this user.


        return gson.toJson(userBean);
    }

    @GET
    @Path("list/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@PathParam("page") Integer page){
        Principal principal = getSecurityContext().getUserPrincipal();
        UserBean currentUser = (UserBean) principal;
        List<UserBean> users = UserDAO.getInstance().list(page,limit);
        return gson.toJson(users);
    }

    @POST
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "user", subjectField = "userId")
    @Session
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("email") String email,
                         @FormParam("password") String password){
        name = name.trim();
        email = email.trim();
        password = password.trim();
        //return {} meaning no update if any of the fields are blank
        //Check if id is present in the returned json on client-side to check
        // if update was made
        if(name.equals("") || email.equals("") || password.equals(""))
            return "{}";
        UserDAO.getInstance().update(id,name,email,password);
        UserBean userBean = UserDAO.getInstance().get(id);
        return gson.toJson(userBean);
    }

    @DELETE
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "user", subjectField = "userId")
    @Session
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id){
        try {
            UserDAO.getInstance().delete(id);
            return "{}";
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }

    @POST
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "user", subjectField = "userId")
    @Session
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String create(@FormParam("name") String name,
                         @FormParam("email") String email,
                         @FormParam("password") String password){
        try {

            UserBean u = UserDAO.getInstance().getByEmail(email);
            if(u!=null){
                return gson.toJson(u);
            }

            String s = "Found something";
            UserBean user = new UserBean();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            UserBean userBean = UserDAO.getInstance().add(user);

            return gson.toJson(userBean);
        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
