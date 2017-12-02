package org.kyantra.dao;

import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents;
import com.amazonaws.services.cloudwatchevents.model.PutRuleRequest;
import com.amazonaws.services.cloudwatchevents.model.PutRuleResult;
import com.amazonaws.services.cloudwatchevents.model.PutTargetsRequest;
import com.amazonaws.services.cloudwatchevents.model.RuleState;
import com.amazonaws.services.cloudwatchevents.model.Target;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.ConfigBean;
import org.kyantra.beans.CronBean;
import org.kyantra.utils.AwsIotHelper;

import java.util.HashMap;
import java.util.Map;

public class CronDAO extends BaseDAO {

    private static CronDAO instance = new CronDAO();
    private AmazonCloudWatchEvents cwe;
    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private CronDAO(){
        cwe = AwsIotHelper.getAmazonCloudWatchEvents();

    }

    public static CronDAO getInstance(){
        return instance;
    }

    public CronBean add(CronBean bean){
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();
        session.save(bean);
        session.getTransaction().commit();
        session.close();


        try {
            PutRuleRequest request = new PutRuleRequest()
                    .withName("cronRule"+bean.getId())
                    .withScheduleExpression("cron(" + bean.getCronExpression() + ")")
                    .withState(RuleState.ENABLED);

            PutRuleResult response = cwe.putRule(request);
            ConfigBean configBean = ConfigDAO.getInstance().get("lambdaFunctionArn");
            String targetData = "{}";
            Map<String,Object> map = new HashMap<>();
            map.put("thingName","thing"+bean.getId());
            map.put("desired",bean.getDesiredState());
            targetData = gson.toJson(map);

            Target target = new Target()
                    .withArn(configBean.getValue())
                    .withInput(targetData)
                    .withId("target_"+bean.getId());

            PutTargetsRequest request2 = new PutTargetsRequest()
                    .withTargets(target)
                    .withRule("cronRule"+bean.getId());
            cwe.putTargets(request2);

            updateLambdaArn(bean.getId(),response.getRuleArn());
        }catch (Throwable t){
            t.printStackTrace();
            delete(bean.getId());
            return null;

        }
        return bean;
    }

    private void updateLambdaArn(Integer id, String ruleArn) {
        if(id <=0)
            return;
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        CronBean cronBean = session.get(CronBean.class, id);
        cronBean.setCronExpression(ruleArn);
        tx.commit();
        session.close();
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
