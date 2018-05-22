package org.kyantra.helper;

import org.kyantra.beans.UnitBean;
import org.kyantra.dao.UnitDAO;

import java.util.HashSet;
import java.util.Set;

public class UnitHelper {

    private static UnitHelper instance = new UnitHelper();

    private UnitHelper() {}

    public static UnitHelper getInstance() {
        return instance;
    }

    public Boolean checkAccess(UnitBean userUnit, UnitBean targetUnit) {
        Set<UnitBean> targetAncestors = getAllParents(targetUnit);
        if (targetAncestors.contains(userUnit))
            return true;
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
