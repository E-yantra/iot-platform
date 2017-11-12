package org.kyantra.filters;

import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.UserDAO;
import org.kyantra.interfaces.Secure;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Secure
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    private static final String REALM = "unit";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        /*
        String token = authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim(); */
        String token = "dummyToken";
        try {

            validateToken(token);
            UserBean userBean = UserDAO.getInstance().getByToken(token);

            Class<?> resourceClass = resourceInfo.getResourceClass();
            List<RoleEnum> classRoles = extractRoles(resourceClass);


            Method resourceMethod = resourceInfo.getResourceMethod();
            List<RoleEnum> methodRoles = extractRoles(resourceMethod);
            //Which url parameter represents the Unit id. it can be id, parent_id etc.
            String subjectField  = extractSubjectField(resourceMethod);

            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles,null,requestContext);
            } else {
                checkPermissions(methodRoles,subjectField,requestContext);
            }

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {

                    return () -> userBean.getId()+"";
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
                    return AUTHENTICATION_SCHEME;
                }
            });

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    //TODO actually fetch user roles by loo
    private void checkPermissions(List<RoleEnum> expectedRoles, String subjectField, ContainerRequestContext requestContent) throws Exception{

        if(expectedRoles.contains(RoleEnum.ALL)){
            throw new Exception();
        }
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        /*
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");*/
        return true;

    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE,
                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
    }

    private void validateToken(String token) throws Exception {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid

        //TODO find the SessionBean with this token and find the user.
    }

    // Extract the roles from the annotated element
    private List<RoleEnum> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secure secured = annotatedElement.getAnnotation(Secure.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                RoleEnum[] allowedRoles = secured.roles();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private String extractSubjectField(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return null;
        } else {
            Secure secured = annotatedElement.getAnnotation(Secure.class);
            return secured.subjectField();
        }
    }
}
