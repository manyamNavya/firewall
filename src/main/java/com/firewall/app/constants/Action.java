package com.firewall.app.constants;

import java.util.Map;
import java.util.TreeMap;

public enum Action {

    BLOCK("block"),
    ALLOW("allow");

    private String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public static Map<Action, String> getActions() {

        Map<Action, String> actions = new TreeMap<>();
        for(Action action : Action.values()) {
            actions.put(action, action.getAction());
        }
        return actions;
    }
}
