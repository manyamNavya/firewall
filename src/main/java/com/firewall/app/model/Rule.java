package com.firewall.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rule {

    String ruleNo;
    String packets;
    String bytes;
    String target;
    String protocol;
    String opt;
    String in;
    String out;
    String source;
    String destination;
    String additionalData;
}
