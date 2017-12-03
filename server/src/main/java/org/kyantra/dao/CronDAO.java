package org.kyantra.dao;

import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents;
import com.amazonaws.services.cloudwatchevents.model.DeleteRuleRequest;
import com.amazonaws.services.cloudwatchevents.model.PutRuleRequest;
import com.amazonaws.services.cloudwatchevents.model.PutRuleResult;
import com.amazonaws.services.cloudwatchevents.model.PutTargetsRequest;
import com.amazonaws.services.cloudwatchevents.model.RemoveTargetsRequest;
import com.amazonaws.services.cloudwatchevents.model.RuleState;
import com.amazonaws.services.cloudwatchevents.model.Target;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.AddPermissionRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.ConfigBean;
import org.kyantra.beans.CronBean;
import org.kyantra.utils.AwsIotHelper;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
                    .withDescription("Iot Generated Rule")
                    .withScheduleExpression("cron(" + bean.getCronExpression().trim() + ")")
                    .withState(RuleState.ENABLED);

            PutRuleResult response = cwe.putRule(request);
            ConfigBean configBean = ConfigDAO.getInstance().get("lambdaFunctionArn");
            String targetData = "{}";
            Map<String,Object> map = new HashMap<>();
            map.put("thingName","thing"+bean.getParentThing().getId());
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

            AWSLambda lambda = AwsIotHelper.getAWSLambdaClient();
            String[] functionArn = configBean.getValue().split(":");
            lambda.addPermission(new AddPermissionRequest()
                    .withFunctionName(functionArn[functionArn.length-1])
                    .withStatementId(UUID.randomUUID().toString())
                    .withPrincipal("events.amazonaws.com")
                    .withSourceArn(response.getRuleArn())
            .withAction("lambda:InvokeFunction"));

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
        cronBean.setCloudwatchResource(ruleArn);
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

        try {

            RemoveTargetsRequest targetsRequest = new RemoveTargetsRequest();
            targetsRequest.withIds("target_"+id);
            targetsRequest.setRule("cronRule" + id);
            cwe.removeTargets(targetsRequest);
            DeleteRuleRequest ruleRequest = new DeleteRuleRequest();
            ruleRequest.withName("cronRule" + id);
            cwe.deleteRule(ruleRequest);
        }catch (Throwable t){
            t.printStackTrace();
        }

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

    public Set<CronBean> getByThingId(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        String ql = "from CronBean where parentThing_Id="+id;
        Query query = session.createQuery(ql);
        List<CronBean> list = query.getResultList();
        session.close();
        return new HashSet<>(list);
    }
}
