package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "deviceAttributes" ,uniqueConstraints = @UniqueConstraint(columnNames={"name", "parentDevice_id"}))
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
    @Column(name="actuator")
    @Expose
    Boolean actuator = Boolean.FALSE;
    @OneToOne
    UnitBean ownerUnit;
    @OneToOne(fetch = FetchType.EAGER)
    DeviceBean parentDevice;

    public Boolean getActuator() {
        if(actuator==null)
            return Boolean.FALSE;
        return actuator;
    }

    public void setActuator(Boolean actuator) {
        this.actuator = actuator;
    }

    public DeviceBean getParentDevice() {
        return parentDevice;
    }

    public void setParentDevice(DeviceBean parentDevice) {
        this.parentDevice = parentDevice;
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
