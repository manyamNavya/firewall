package com.firewall.app.model;

import com.firewall.app.constants.HostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockIPRequest {

    String ipAddress;
    HostType hostType;
}
