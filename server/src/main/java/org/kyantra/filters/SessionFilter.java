package org.kyantra.filters;

import org.kyantra.beans.UserBean;
import org.kyantra.dao.UserDAO;
import org.kyantra.exceptionhandling.ExceptionMessage;
import org.kyantra.interfaces.Session;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@Session
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SessionFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Context
    HttpServletRequest request;

    private boolean isSessionNeeded(AnnotatedElement annotatedElement) {

        if (annotatedElement == null) {
            return false;
        } else {
            Session secured = annotatedElement.getAnnotation(Session.class);
            if (secured != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method resourceMethod = resourceInfo.getResourceMethod();

        if (!isSessionNeeded(resourceMethod)) {
            return;
        }

        String authorizationCookie = requestContext.getCookies().getOrDefault("authorization", new Cookie("authorization", "")).getValue();
        if (!authorizationCookie.isEmpty()) {
            UserBean userBean = UserDAO.getInstance().getByToken(authorizationCookie);

            // If user provides a wrong auth token redirect him to login
            if (userBean == null) {
                try {
                    throw new RedirectionException(ExceptionMessage.TEMP_REDIRECT,
                            Response.Status.TEMPORARY_REDIRECT.getStatusCode(),
                            new URI("/login"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            // TODO: 5/4/18 Understand how and why below code works
            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {
                    return userBean;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "cookie";
                }

            });


        } else {
            // TODO: 5/24/18 Send a JSON response along with temporary redirect error
            try {
                throw new RedirectionException(ExceptionMessage.TEMP_REDIRECT,
                        Response.Status.TEMPORARY_REDIRECT.getStatusCode(),
                        new URI("/login"));
            }
            catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }
}
