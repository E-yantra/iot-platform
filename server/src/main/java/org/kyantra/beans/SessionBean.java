package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "session")
public class SessionBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "token" ,unique = true)
    @Expose
    String token;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @Expose
    UserBean user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
