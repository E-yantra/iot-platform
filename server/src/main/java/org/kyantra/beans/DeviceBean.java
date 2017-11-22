package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "devices")
public class DeviceBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name")
    @Expose
    String name;

    @Column(name="description")
    @Expose
    String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentDevice")
    @Expose
    private List<DeviceAttributeBean> deviceAttributes;

    @OneToOne
    private UnitBean ownerUnit;

    @OneToOne(fetch = FetchType.EAGER)
    private ThingBean parentThing;

    public ThingBean getParentThing() {
        return parentThing;
    }

    public void setParentThing(ThingBean parentThing) {
        this.parentThing = parentThing;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DeviceAttributeBean> getDeviceAttributes() {
        return deviceAttributes;
    }

    public void setDeviceAttributes(List<DeviceAttributeBean> deviceAttributes) {
        this.deviceAttributes = deviceAttributes;
    }

    public UnitBean getOwnerUnit() {
        return ownerUnit;
    }

    public void setOwnerUnit(UnitBean ownerUnit) {
        this.ownerUnit = ownerUnit;
    }
}
