package org.kyantra.exception;

public class ExceptionMessage {
    public final static String FORBIDDEN = "You don't have access to this resource.";
    public final static String NOT_FOUND = "The resource in question doesn't exist.";
    public final static String TEMP_REDIRECT = "You are being redirected to login page.";

    public static class DeveloperMessage {
        public final static String FORBIDDEN = "You don't have rights to access the resources.";
        public final static String NOT_FOUND = "The resource with provided id doesn't exist in the database.";
        public final static String TEMP_REDIRECT = "You are being redirected to login page because of no or invalid token.";
    }

    public static class Link {
        public final static String FORBIDDEN = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/403";
        public final static String NOT_FOUND = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404";
        public final static String TEMP_REDIRECT = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/307";
    }
}
