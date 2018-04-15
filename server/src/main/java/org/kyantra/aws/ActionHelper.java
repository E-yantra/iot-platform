package org.kyantra.aws;

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

    public String[] getActionTypes() {
        return actionTypes;
    }
}
