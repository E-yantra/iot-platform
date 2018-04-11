package org.kyantra.beans;


import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "things")
public class ThingBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Expose
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentThing", orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<DeviceBean> devices;

    @Expose
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentThing")
    private Set<CronBean> crons;

    @Expose
    @ManyToOne(fetch = FetchType.EAGER)
    private UnitBean parentUnit;

    // required by storage rule
    @Expose
    public Boolean storageEnabled;

    @Expose
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentThing", orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<RuleBean> rules;

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

    public Set<RuleBean> getRules() {
        return rules;
    }

    public void setRules(Set<RuleBean> rules) {
        this.rules = rules;
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

    // required by storage rule
    public Boolean getStorageEnabled() {
        return storageEnabled;
    }

    public void setStorageEnabled(Boolean enable) {
        this.storageEnabled = enable;
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

    public RuleBean addRule(RuleBean ruleBean) {
        this.rules.add(ruleBean);
        ruleBean.setParentThing(this);
        return ruleBean;
    }

    public void removeRule(RuleBean ruleBean) {
        this.rules.remove(ruleBean);
        ruleBean.setParentThing(null);
    }

}
