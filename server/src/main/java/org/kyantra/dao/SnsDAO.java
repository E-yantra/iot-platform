package org.kyantra.dao;

import org.hibernate.Session;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.SnsBean;
import org.kyantra.beans.ThingBean;

public class SnsDAO extends BaseDAO {
    static SnsDAO instance = new SnsDAO();
    public static SnsDAO getInstance() { return instance; }

    public SnsBean add(SnsBean bean) {
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();

        RuleBean ruleBean = RuleDAO.getInstance().get(bean.getParentRule().getId());
        SnsBean snsBean = ruleBean.addSNSAction(bean);

        session.saveOrUpdate(ruleBean);
        session.getTransaction().commit();
        session.close();
        return snsBean;
    }

    public SnsBean get(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        SnsBean snsBean = session.get(SnsBean.class, id);
        session.close();
        return snsBean;
    }
}
