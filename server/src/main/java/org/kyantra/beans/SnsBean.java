package org.kyantra.beans;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kyantra.utils.Constant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "sns")
public class SnsBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Expose
    @NotNull
    @Column(name = "topic")
    String topic;

    @Expose
    @Column(name = "topicARN")
    String topicARN;

    // subject of the notification
    @Expose
    @Column(name = "subject")
    String subject = Constant.SNS_SUBJECT;

    // message to send along with data to sns subscribers
    @Expose
    @Column(name = "message")
    String message = Constant.SNS_MESSAGE;

    // interval in minutes which will be checked to re-report with the message
    @Expose
    @Column(name = "`interval`")
    Integer interval = Constant.SNS_INTERVAL;

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

    public String getSubject() {

        return subject;
    }

    public void setSubject(String subject) {
        if(!subject.equals(""))
            this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if(!message.equals(""))
            this.message = message;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        if(interval.intValue() >= Constant.SNS_INTERVAL)
            this.interval = interval;
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
