package com.firewall.app.entity;

import com.firewall.app.constants.Action;
import com.firewall.app.constants.Direction;
import com.firewall.app.constants.Protocols;
import com.firewall.app.constants.RuleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class FirewallRule {

    @MongoId
    private String id;
    private String name;
    private RuleStatus status;
    private String ipAddress;
    private Direction direction;
    private int port;
    private Protocols protocol;
    private String enable;
    private Action action;
}