package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * A right represents authorization of a user.
 * It is of the form unit_id:role.
 * Roles are currently static and only read only and admin (full priviledges).
 */
@Entity
@Table(name = "rights", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "unit_id"})})
public class RightsBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "unit_id")
    @Expose
    UnitBean unit;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @Expose
    UserBean user;

    @Enumerated(EnumType.STRING)
    @Expose
    RoleEnum role;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public UnitBean getUnit() {
        return unit;
    }

    public void setUnit(UnitBean unit) {
        this.unit = unit;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
