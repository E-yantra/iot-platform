package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.security.Principal;

/**
 * All users in the system are represented as this bean.
 */

@Entity
@Table(name = "users")
public class UserBean implements Principal {

    @Id
    @Expose
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @Column(name="name")
    @Expose
    String name;

    @NotNull
    @Column(name="email" ,unique = true)
    @Expose
    String email;

    @NotNull
    @Column(name="password")
    String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
