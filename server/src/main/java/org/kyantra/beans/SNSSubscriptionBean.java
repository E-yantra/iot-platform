package org.kyantra.beans;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "snsSubscription")
public class SNSSubscriptionBean {
    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "type")
    @Expose
    String type;

    @Column(name = "value")
    @Expose
    String value;

    @ManyToOne(fetch = FetchType.EAGER)
    private SNSBean parentSNSBean;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SNSBean getParentSNSBean() {
        return parentSNSBean;
    }

    public void setParentSNSBean(SNSBean parentSNSBean) {
        this.parentSNSBean = parentSNSBean;
    }
}
