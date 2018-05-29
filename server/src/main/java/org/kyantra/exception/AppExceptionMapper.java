package org.kyantra.exception;

import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.net.URISyntaxException;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        Gson gson = new Gson();
        ErrorFormat message = new ErrorFormat();

        if (ex instanceof DataNotFoundException) {
            message.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            message.setMessage(ex.getMessage());
            message.setDeveloperMessage(ExceptionMessage.DeveloperMessage.NOT_FOUND);
            message.setLink(ExceptionMessage.Link.NOT_FOUND);
        }
        else if (ex instanceof ForbiddenException) {
            message.setStatus(Response.Status.FORBIDDEN.getStatusCode());
            message.setMessage(ex.getMessage());
            message.setDeveloperMessage(ExceptionMessage.DeveloperMessage.FORBIDDEN);
            message.setLink(ExceptionMessage.Link.FORBIDDEN);
        }
        else if (ex instanceof RedirectionException) {
            message.setStatus(Response.Status.TEMPORARY_REDIRECT.getStatusCode());
            message.setMessage(ex.getMessage());
            message.setDeveloperMessage(ExceptionMessage.DeveloperMessage.TEMP_REDIRECT);
            message.setLink(ExceptionMessage.Link.TEMP_REDIRECT);
            try {
                return Response.temporaryRedirect(new URI("/login"))
                        .cookie(new NewCookie("authorization", ""))
                        .status(message.getStatus())
                        .entity(gson.toJson(message))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else if (ex instanceof WebApplicationException) {
            message.setStatus(((WebApplicationException) ex).getResponse().getStatus());
            message.setMessage(ex.getMessage());
            message.setDeveloperMessage(ExceptionUtils.getStackTrace(ex));
        }
        else {
            message.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            message.setMessage(ex.getMessage());
            message.setDeveloperMessage(ExceptionUtils.getStackTrace(ex));
        }

        return Response.status(message.getStatus())
                .entity(gson.toJson(message))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}