package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.UserDAO;
import org.kyantra.exceptionhandling.DataNotFoundException;
import org.kyantra.exceptionhandling.ExceptionMessage;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;

@Path("/user")
@Api(value="user")
public class UserResource extends BaseResource {

    int limit = 10;

    @GET
    @Path("get/{id}")
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id)   {
        UserBean targetUser = UserDAO.getInstance().get(id);
        UserBean currentUser = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetUser == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (!currentUser.equals(targetUser))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        return gson.toJson(targetUser);
    }


    // TODO: 5/25/18 Need authorization here?
    @GET
    @Path("list/page/{page}")
    @Session
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@PathParam("page") Integer page){
        Principal principal = getSecurityContext().getUserPrincipal();
        UserBean currentUser = (UserBean) principal;
        List<UserBean> users = UserDAO.getInstance().list(page,limit);
        return gson.toJson(users);
    }


    @PUT
    @Path("update/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "user", subjectField = "userId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("email") String email,
                         @FormParam("password") String password)   {
        name = name.trim();
        email = email.trim();
        password = password.trim();
        //return {} meaning no update if any of the fields are blank
        //Check if id is present in the returned json on client-side to check
        // if update was made
        if(name.equals("") || email.equals("") || password.equals(""))
            return "{}";
        UserBean targetUser = UserDAO.getInstance().get(id);
        UserBean currentUser = (UserBean)getSecurityContext().getUserPrincipal();

        if (targetUser == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (!currentUser.equals(targetUser))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        UserDAO.getInstance().update(id, name, email, password);
        UserBean userBean = UserDAO.getInstance().get(id);
        return gson.toJson(userBean);
    }


    @DELETE
    @Path("delete/{id}")
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "user", subjectField = "userId")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id) {
        // TODO: 5/24/18 Don't delete user only remove rights
        try {
            UserDAO.getInstance().delete(id);
            return "{}";
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }


    // TODO: 5/24/18 This method lets a person signup and create a user but no unit is assigned to him/her
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
            if(u!=null) {
                return gson.toJson(u);
            }

            String s = "Found something";
            UserBean user = new UserBean();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            UserBean userBean = UserDAO.getInstance().add(user);

            return gson.toJson(userBean);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }
}