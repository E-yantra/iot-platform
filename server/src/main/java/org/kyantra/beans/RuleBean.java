package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.ws.rs.Consumes;

@Entity
@Table(name = "rule")
public class RuleBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Expose
    @Column(unique = true)
    String name;

    @Expose
    @Column(name = "description")
    String description;

    @Expose
    @Column(name = "data")
    String data;

    @Expose
    @Column(name = "topic")
    String topic;

    @Expose
    @OneToOne
    @Column(name = "sns_id")
    SnsBean snsBean;

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

    public SnsBean getSnsBean() {
        return snsBean;
    }

    public void setSnsBean(SnsBean snsBean) {
        this.snsBean = snsBean;
    }

}
