package org.kyantra.filters;

import org.kyantra.beans.RightsBean;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.RightsDAO;
import org.kyantra.dao.UserDAO;
import org.kyantra.interfaces.Secure;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
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
import java.util.Set;
import java.util.stream.Collectors;

@Secure
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    private static final String REALM = "unit";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        String authorizationCookie = requestContext.getCookies().getOrDefault("authorization", new Cookie("token","")).getValue().toString();

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader) && !isTokenBasedAuthentication(authorizationCookie)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        try {
            String token = authorizationHeader==null?authorizationCookie:authorizationHeader;
            validateToken(token);

            UserBean userBean = UserDAO.getInstance().getByToken(token);
            Class<?> resourceClass = resourceInfo.getResourceClass(); //UnitResource.class
            List<RoleEnum> classRoles = extractRoles(resourceClass);
            Method resourceMethod = resourceInfo.getResourceMethod();
            List<RoleEnum> methodRoles = extractRoles(resourceMethod);

            if (methodRoles.isEmpty()) {
                checkPermissions(userBean,classRoles,requestContext);
            } else {
                checkPermissions(userBean,methodRoles,requestContext);
            }

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
                    return AUTHENTICATION_SCHEME;
                }
            });

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private void checkPermissions(UserBean userBean, List<RoleEnum> expectedRoles, ContainerRequestContext requestContent) throws Exception {

        if(!expectedRoles.isEmpty()) {
            Set<RightsBean> rights = RightsDAO.getInstance().getRightsByUser(userBean);
//            Set<RoleEnum> roles = rights.stream().map(RightsBean::getRole).collect(Collectors.toSet());
//            for(RoleEnum r:roles){
//                if(expectedRoles.contains(r)){
//                    return;
//                }
//            }



        } else {
            return;
        }

        // TODO Remove this later
        // throw new Exception("Not possible");
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        return authorizationHeader != null && !authorizationHeader.toLowerCase().isEmpty();

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
        UserBean user = UserDAO.getInstance().getByToken(token);
        if(user==null){
            throw new Exception("Unable to authenticate");
        }
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
