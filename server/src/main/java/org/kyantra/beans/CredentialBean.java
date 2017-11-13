package org.kyantra.beans;

/**
 * This is not a entity bean and hence should not be stored as table etc.
 * This bean hold authentication information which can be of two types.
 * 1. email password
 * 2. A specially generated token
 */
public class CredentialBean {

    String email;
    String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
