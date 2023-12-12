package com.firewall.app.constants;

public enum RuleStatus {

    YES("yes"),
    NO("no");

    private String ruleStatus;

    RuleStatus(String status) {

        this.ruleStatus = status;
    }

    public String getStatus() {

        return this.ruleStatus;
    }
}
