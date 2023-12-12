package com.firewall.app.model;

import com.firewall.app.constants.HostType;
import com.firewall.app.constants.Protocols;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockPortRequest {

    Integer portNumber;
    HostType hostType;
    Protocols protocol;
}
