package org.kyantra.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        if(ex instanceof WebApplicationException){
            WebApplicationException exception = (WebApplicationException) ex;
            return exception.getResponse();
        }
        ex.printStackTrace();
        return Response.status(500)
                .entity(ex.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }


}