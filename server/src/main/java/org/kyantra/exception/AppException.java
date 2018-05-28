package org.kyantra.exception;

import com.sun.jndi.cosnaming.ExceptionMapper;
import org.glassfish.jersey.jaxb.internal.XmlJaxbElementProvider;

// http://www.codingpedia.org/ama/error-handling-in-rest-api-with-jersey/
public class AppException extends Exception {

    private Integer status;
    private int code;
    private String message;
    private String link;
    private String developerMessage;

    // TODO: 5/28/18 May be try to convert it to builder pattern
    public AppException(Integer status, int code, String message, String link, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.link = link;
        this.developerMessage = developerMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }
}
