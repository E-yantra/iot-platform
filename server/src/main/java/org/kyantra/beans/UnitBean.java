package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A unit is a business unit. A unit as more units as its children or things.
 * For example College X is a unit which will have Farm 1 and Farm 2 as its sub units.
 */
@Entity
@Table(name = "units")
public class UnitBean {

    @Id
    @Expose
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name = "unit_name", unique = true)
    String unitName;

    @Column(name = "parent")
    UnitBean parent;

    @Column(name = "description")
    String description;

    @Column(name = "photo")
    String photo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public UnitBean getParent() {
        return parent;
    }

    public void setParent(UnitBean parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
