package com.firewall.app.entity;

import com.firewall.app.constants.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class BlockedIPs {

    String id;
    String ipAddress;
    Direction direction;
}