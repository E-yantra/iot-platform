package org.kyantra.exceptionhandling;

public class ExceptionMessage {
    public final static String FORBIDDEN = "You don't have access to this resource.";
    public final static String DATA_NOT_FOUND = "The resource you are trying to access doesn't exist.";
    public final static String TEMP_REDIRECT = "You are being redirected to login page.";
    public final static String PERMANENTLY_MOVED = "You are being redirected to main page.";

    public static class DeveloperMessage {
        public final static String FORBIDDEN = "You don't have rights to access the resources.";
        public final static String DATA_NOT_FOUND = "The resource with provided identifier doesn't exist in the database.";
        public final static String TEMP_REDIRECT = "You are being redirected to login page because of no or invalid token.";
        public final static String PERMANENTLY_MOVED = "You are being redirected to home page mostly because you were already authenticated and tried loginning again.";
    }

    public static class Link {
        public final static String FORBIDDEN = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/403";
        public final static String DATA_NOT_FOUND = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404";
        public final static String TEMP_REDIRECT = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/307";
        public final static String PERMANENTLY_MOVED = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/301";
    }
}
