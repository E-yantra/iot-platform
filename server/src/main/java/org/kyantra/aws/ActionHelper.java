package org.kyantra.aws;

import com.amazonaws.services.iot.model.Action;
import com.amazonaws.services.iot.model.LambdaAction;
import com.amazonaws.services.iot.model.MessageFormat;
import com.amazonaws.services.iot.model.SnsAction;
import org.kyantra.beans.SnsBean;
import org.kyantra.dao.ConfigDAO;
import sun.security.krb5.Config;

// singleton
public class ActionHelper {

    private static String[] actionTypes = {"SNS", "DDB"};

    private static ActionHelper instance;

    private ActionHelper() {

    }

    public static ActionHelper getInstance() {
        if(instance == null) {
            instance = new ActionHelper();
        }
        return instance;
    }

    public Action createSnsAction(SnsBean snsBean) {
        // 1. create SnsAction
        SnsAction snsAction = new SnsAction();
        snsAction.withTargetArn(snsBean.getTopicARN())
                .withMessageFormat(MessageFormat.RAW)
                .withRoleArn(ConfigDAO.getInstance().get("iotRoleArn").getValue());

        // 2. create Action
        Action action = new Action().withSns(snsAction);
        return action;
    }

    public Action createLambdaAction(String functionArn) {
        // 1. create LambdaAction
        LambdaAction lambdaAction = new LambdaAction();
        lambdaAction.withFunctionArn(functionArn);

        // 2. create Action
        Action action = new Action().withLambda(lambdaAction);
        return action;
    }

    public String[] getActionTypes() {
        return actionTypes;
    }
}
