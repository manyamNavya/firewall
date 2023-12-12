package com.firewall.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stats {

    Integer blockedSourceIps;
    Integer blockedDestIps;
    Integer blockedSourcePorts;
    Integer blockedDestPorts;
    Integer blockedProtocols;
    Integer limitedIps;
}
