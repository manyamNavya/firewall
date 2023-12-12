package com.firewall.app.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LimitRequests {

    String ipAddress;
    Integer requests;
    Integer minutes;
}
