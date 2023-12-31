package com.firewall.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Log {

    String timestamp;
    String reason;
    String source;
    String destination;
    String protocol;
}
