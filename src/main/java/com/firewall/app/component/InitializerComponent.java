package com.firewall.app.component;

import com.firewall.app.service.FirewallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitializerComponent {

    @Autowired
    FirewallService firewallService;

    @PostConstruct
    public void onStartup() {

        firewallService.checkAndAddDefaultRules("INPUT");
        firewallService.checkAndAddDefaultRules("OUTPUT");
    }
}
