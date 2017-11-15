package org.kyantra.beans;


import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany
    private List<DeviceBean> devices;

    @OneToOne
    private UnitBean ownerUnit;

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

    public List<DeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceBean> devices) {
        this.devices = devices;
    }

    public UnitBean getOwnerUnit() {
        return ownerUnit;
    }

    public void setOwnerUnit(UnitBean ownerUnit) {
        this.ownerUnit = ownerUnit;
    }
}
