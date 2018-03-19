package org.kyantra.dao;

import org.dom4j.rule.Rule;
import org.hibernate.Session;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.ThingBean;

public class RuleDAO extends BaseDAO {
    private static RuleDAO instance = new RuleDAO();

    public static RuleDAO getInstance() {
        return instance;
    }

    public RuleBean add(RuleBean bean) {

        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();

        ThingBean thingBean = ThingDAO.getInstance().get(bean.getParentThing().getId());
        RuleBean ruleBean  = thingBean.addRule(bean);

        session.saveOrUpdate(thingBean);
        session.getTransaction().commit();
        session.close();
        return ruleBean;
    }

    public RuleBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        RuleBean ruleBean = session.get(RuleBean.class,id);
        session.close();
        return ruleBean;
    }

}
