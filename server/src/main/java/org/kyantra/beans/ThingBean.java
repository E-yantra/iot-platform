package org.kyantra.beans;


import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
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

    @Column(name = "certificate_dir")
    String certificateDir;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentThing", orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @Expose
    private Set<DeviceBean> devices;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "parentThing")
    private Set<CronBean> crons;

//    @OneToOne(fetch = FetchType.EAGER)
//    @Expose
//    private UnitBean parentUnit;
    @ManyToOne(fetch = FetchType.EAGER)
    @Expose
    private UnitBean parentUnit;


    public Set<CronBean> getCrons() {
        return crons;
    }

    public void setCrons(Set<CronBean> crons) {
        this.crons = crons;
    }

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

    public void setCertificateDir(String directory) {
        this.certificateDir = directory;
    }

    public String getCertificateDir() {
        return certificateDir;
    }

    public DeviceBean addDevice(DeviceBean deviceBean) {
        this.devices.add(deviceBean);
        deviceBean.setParentThing(this);
        return deviceBean;
    }

    public void removeDevice(DeviceBean deviceBean) {
        this.devices.remove(deviceBean);
        deviceBean.setParentThing(null);
    }

}
