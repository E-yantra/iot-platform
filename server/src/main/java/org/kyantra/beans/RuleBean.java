package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.ws.rs.Consumes;

@Entity
@Table(name = "rule")//, uniqueConstraints = @UniqueConstraint(columnNames={"data", "topic"}))
public class RuleBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Expose
    @Column(name = "`name`")//, unique = true)
    String name;

    @Expose
    @Column(name = "`description`")
    String description;

    //TODO: To have an association with the user who created this
//    @Expose
//    @Column(name="created_by")
//    String createdBy;

    @Expose
    @Column(name = "`data`")
    String data;

    @Expose
    @Column(name = "`topic`")
    String topic;

    @Expose
    @Column(name = "`condition`")
    String condition;

    @Expose
    @OneToOne(mappedBy = "parentRule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private SnsBean snsAction;

    @Expose
    @ManyToOne(fetch = FetchType.EAGER)
    private ThingBean parentThing;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }

    public ThingBean getParentThing() {
        return parentThing;
    }

    public void setParentThing(ThingBean parentThing) {
        this.parentThing = parentThing;
    }

    public SnsBean getSnsAction() {
        return snsAction;
    }

    public SnsBean addSNSAction(SnsBean snsAction) {
        //TODO: Perform checks for presence of other actions
        //For simplicity one rule has one action only; other fields are null
        snsAction.setParentRule(this);
        this.snsAction = snsAction;
        return snsAction;
    }

    public void removeSNSAction() {
        if(this.snsAction != null) {
            snsAction.setParentRule(null);
            this.snsAction = null;
        }
    }

}
