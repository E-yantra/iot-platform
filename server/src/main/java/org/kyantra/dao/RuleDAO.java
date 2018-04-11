package org.kyantra.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kyantra.beans.DeviceBean;
import org.kyantra.beans.RuleBean;
import org.kyantra.beans.ThingBean;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public RuleBean update(RuleBean ruleBean) {
        Session session = getService().getSessionFactory().openSession();
        session.beginTransaction();

        session.update(ruleBean);

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

    public RuleBean getByName(String name) {
        Session session = getService().getSessionFactory().openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RuleBean> criteria = builder.createQuery(RuleBean.class);

        Root<RuleBean> root = criteria.from(RuleBean.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("name"), name));

        Query query = session.createQuery(criteria);
        RuleBean ruleBean = (RuleBean) query.getSingleResult();

        session.close();
        return ruleBean;
    }

    public void delete(Integer id) {
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        RuleBean ruleBean = session.get(RuleBean.class, id);
        ruleBean.getParentThing().removeRule(ruleBean);

        tx.commit();
        session.close();
    }

    public void deleteByName(String name) {
        Session session = getService().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RuleBean> criteria = builder.createQuery(RuleBean.class);

        Root<RuleBean> root = criteria.from(RuleBean.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("name"), name));

        Query query = session.createQuery(criteria);
        RuleBean ruleBean = (RuleBean) query.getSingleResult();

        ruleBean.getParentThing().removeRule(ruleBean);

        tx.commit();
        session.close();
    }

    public Set<RuleBean> getByThing(Integer thingId) {
        Session session = getService().getSessionFactory().openSession();

        String ql = "from RuleBean where parentThing_Id="+thingId;

        Query query = session.createQuery(ql);
        List<RuleBean> list = query.getResultList();

        session.close();
        return new HashSet<>(list);
    }

}
