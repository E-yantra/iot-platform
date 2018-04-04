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

@Entity
@Table(name = "cron")
public class CronBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name")
    @Expose
    String cronName;

    @Column(name = "cron_expression")
    @Expose
    String cronExpression;

    @Column(name = "shadow")
    @Expose
    String desiredState;

    @Column(name = "cloudwatch_resource")
    @Expose
    String cloudwatchResource;

    @OneToOne(fetch = FetchType.EAGER)
    ThingBean parentThing;

    public String getCronName() {
        return cronName;
    }

    public void setCronName(String cronName) {
        this.cronName = cronName;
    }

    public ThingBean getParentThing() {
        return parentThing;
    }

    public void setParentThing(ThingBean parentThing) {
        this.parentThing = parentThing;
    }

    public String getCloudwatchResource() {
        return cloudwatchResource;
    }

    public void setCloudwatchResource(String cloudwatchResource) {
        this.cloudwatchResource = cloudwatchResource;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(String desiredState) {
        this.desiredState = desiredState;
    }



}
