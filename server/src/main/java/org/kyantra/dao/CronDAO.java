package org.kyantra.dao;

import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents;
import com.amazonaws.services.cloudwatchevents.model.PutRuleRequest;
import com.amazonaws.services.cloudwatchevents.model.PutRuleResult;
import com.amazonaws.services.cloudwatchevents.model.RuleState;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.CronBean;
import org.kyantra.utils.AwsIotHelper;

public class CronDAO extends BaseDAO {

    private static CronDAO instance = new CronDAO();
    private AmazonCloudWatchEvents cwe;

    private CronDAO(){
        cwe = AwsIotHelper.getAmazonCloudWatchEvents();

    }

    public static CronDAO getInstance(){
        return instance;
    }

    public CronBean add(CronBean bean){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();


        PutRuleRequest request = new PutRuleRequest()
                .withName("cronRule")
                .withScheduleExpression("cron("+bean.getCronExpression()+")")
                .withState(RuleState.ENABLED);
        PutRuleResult response = cwe.putRule(request);
        bean.setCloudwatchResource(response.getRuleArn());
        session.save(bean);
        session.getTransaction().commit();
        session.close();

        return bean;
    }

    public CronBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        CronBean bean = session.get(CronBean.class,id);
        session.close();
        return bean;
    }

    public void delete(Integer id){
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        CronBean bean = session.get(CronBean.class, id);
        session.delete(bean);
        tx.commit();
        session.close();
    }

    public void update(int id, String cronExpression , String desiredState){
        if(id <=0)
            return;
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        CronBean cronBean = session.get(CronBean.class, id);
        cronBean.setCronExpression(cronExpression);
        cronBean.setDesiredState(desiredState);
        tx.commit();
        session.close();
    }
}
