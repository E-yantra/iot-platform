package org.kyantra.beans;


import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "things")
public class ThingBean {

    @Id
    @Expose
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name")
    @Expose
    String name;

    @Column(name = "description")
    @Expose
    String description;

    @Column(name = "ip")
    String ip;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "parentThing")
    @Expose
    private Set<DeviceBean> devices;

    @OneToOne(fetch = FetchType.EAGER)
    @Expose
    private UnitBean parentUnit;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Set<DeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceBean> devices) {
        this.devices = devices;
    }

    public UnitBean getParentUnit() {
        return parentUnit;
    }

    public void setParentUnit(UnitBean parentUnit) {
        this.parentUnit = parentUnit;
    }
}
