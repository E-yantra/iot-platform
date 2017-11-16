package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "deviceAttributes")
public class DeviceAttributeBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name")
    @Expose
    String name;

    @Column(name="type")
    @Expose
    String type;

    @Column(name="def")
    @Expose
    String def;

    @OneToOne
    UnitBean ownerUnit;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public UnitBean getOwnerUnit() {
        return ownerUnit;
    }

    public void setOwnerUnit(UnitBean ownerUnit) {
        this.ownerUnit = ownerUnit;
    }
}
