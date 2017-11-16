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

/**
 * A right represents authorization of a user.
 * It is of the form unit_id:role.
 * Roles are currently static and only read only and admin (full priviledges).
 */
@Entity
@Table(name = "rights")
public class RightsBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    // TODO: Should this be UserBean?? Read/write/All right permissions are for the user
    // there is already table Unit_users for association of user to units.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id")
    @Expose
    UnitBean unit;

    @Enumerated(EnumType.STRING)
    @Expose
    RoleEnum role;

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
