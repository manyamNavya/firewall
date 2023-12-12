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

    /**
     * Renders Admin dashboard
     */
    @GetMapping("/")
    public String dashboard(Model model) {

        model.addAttribute("statistics", firewallService.getStatistics());
        model.addAttribute("logs", firewallService.getLogs());
        return "dashboard";
    }

    /**
     * Renders Page to create New Rules
     */
    @GetMapping("/rules/new/")
    public String blocker(Model model) {

        model.addAttribute("protocols", Protocols.values());
        model.addAttribute("hostType", HostType.values());
        return "rule-blocker";
    }


    /**
     * Renders the page to view list of rules
     */
    @GetMapping("/rules/")
    public String rules(Model model) {
        try {
            model.addAttribute("rules", firewallService.getRules());
        } catch(Exception e) {
            model.addAttribute("rules", new ArrayList<String[]>());
        }
        return "rules";
    }


    /**
     * Renders a page to view Stats for all rules
     */
    @GetMapping("/rulesStats/")
    public String rulesStats(Model model) {
        try {
            model.addAttribute("rules", firewallService.getRules());
        } catch(Exception e) {
            model.addAttribute("rules", new ArrayList<String[]>());
        }
        return "rules-stats";
    }
}
