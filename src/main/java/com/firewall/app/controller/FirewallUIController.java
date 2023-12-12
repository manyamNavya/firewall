package com.firewall.app.controller;

import com.firewall.app.constants.HostType;
import com.firewall.app.constants.Protocols;
import com.firewall.app.service.FirewallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/")
public class FirewallUIController {

    @Autowired
    FirewallService firewallService;

    @GetMapping("/")
    public String dashboard(Model model) {

        return "dashboard";
    }

    @GetMapping("/rules/new/")
    public String blocker(Model model) {

        model.addAttribute("protocols", Protocols.values());
        model.addAttribute("hostType", HostType.values());
        return "rule-blocker";
    }

    @GetMapping("/rules/")
    public String rules(Model model) {
        try {
            model.addAttribute("rules", firewallService.getRules());
        } catch(Exception e) {
            model.addAttribute("rules", new ArrayList<String[]>());
        }
        return "rules";
    }
}
