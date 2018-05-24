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

    public Boolean checkAccess(UserBean user, UnitBean targetUnit) {
        Set<UnitBean> userUnits = RightsDAO.getInstance().getUnitsByUser(user);
        Set<UnitBean> targetAncestors = getAllParents(targetUnit);

        for(UnitBean userUnit: userUnits) {
            // contains will check for equality using equals
            // if no equals is overridden it'll default to equal from Object class
            // which is same as '=='
            if (targetAncestors.contains(userUnit))
                return true;
        }

        return false;
    }

    public Set<UnitBean> getAllParents(UnitBean unitBean) {
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
