package org.kyantra.helper;

import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.RightsDAO;

import java.util.HashSet;
import java.util.Set;

public class UnitHelper {

    private static UnitHelper instance = new UnitHelper();

    private UnitHelper() {}

    public static UnitHelper getInstance() {
        return instance;
    }

    public Set<UnitBean> getAllParents(UnitBean unitBean) {
        /*
with recursive ancestors as ( select id, unit_name, parent_id from units where id = 100 union all select u.id, u.unit_name, u.parent_id  from units u, ancestors ag where u.id = ag.parent_id ) select * from ancestors;
 */
        Set<UnitBean> unitBeans = new HashSet<>();
        unitBeans.add(unitBean);

        while (unitBean.getParent()!=null) {
            // unitBean holds parent of unitBean in the next step
            unitBean = unitBean.getParent();
            unitBeans.add(unitBean);
        }

        return unitBeans;
    }
}
