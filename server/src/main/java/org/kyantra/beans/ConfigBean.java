package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="configset")
public class ConfigBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Expose
    @Column(name = "confKey" ,unique = true)
    String confKey;

    @Expose
    @Column(name = "confValue")
    String confValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return confKey;
    }

    public void setKey(String key) {
        this.confKey = key;
    }

    public String getValue() {
        return confValue;
    }

    public void setValue(String value) {
        this.confValue = value;
    }
}
