package org.kyantra.exception;

import com.google.gson.annotations.Expose;

public class ErrorFormat {

    @Expose
    private Integer status;

//    @Expose
//    private int code;

    @Expose
    private String message;

    @Expose
    private String link;

    @Expose
    private String developerMessage;

    // TODO: 5/28/18 May be try to convert it to builder pattern
    public ErrorFormat() {

    }

    public ErrorFormat(Integer status, String message, String link, String developerMessage) {
        this.status = status;
//        this.code = code;
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

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }

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
