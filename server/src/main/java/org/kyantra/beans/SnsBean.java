package org.kyantra.beans;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sns")
public class SnsBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Expose
    @Column(name = "topic")
    String topic;

    @Expose
    @Column(name = "topicARN")
    String topicARN;

    @Expose
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentSNSBean", orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<SnsSubscriptionBean> subscriptions;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_id")
    private RuleBean parentRule;

    public RuleBean getParentRule() {
        return parentRule;
    }

    public void setParentRule(RuleBean parentRule) {
        this.parentRule = parentRule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicARN() {
        return topicARN;
    }

    public void setTopicARN(String topicARN) {
        this.topicARN = topicARN;
    }

    public Set<SnsSubscriptionBean> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<SnsSubscriptionBean> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public SnsSubscriptionBean addSubscription(SnsSubscriptionBean snsSubscriptionBean) {
        this.subscriptions.add(snsSubscriptionBean);
        snsSubscriptionBean.setParentSNSBean(this);
        return snsSubscriptionBean;
    }

    public void removeSubscription(SnsSubscriptionBean snsSubscriptionBean) {
        this.subscriptions.remove(snsSubscriptionBean);
        snsSubscriptionBean.setParentSNSBean(null);
    }
}
