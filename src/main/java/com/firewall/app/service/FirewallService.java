package com.firewall.app.service;

import com.firewall.app.constants.HostType;
import com.firewall.app.constants.Protocols;
import com.firewall.app.model.LimitRequests;
import com.firewall.app.model.Rule;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirewallService {

    public String blockIP(String ipAddress, HostType hostType) throws IOException, InterruptedException {

        if(hostType.equals(HostType.SOURCE)) {
            executeIPTablesCommand("iptables -A INPUT -s " + ipAddress + " -j DROP -m comment --comment BlockedIP");
            return executeIPTablesCommand("iptables -A INPUT -s " + ipAddress + "-j LOG --log-prefix FW_BLOCKED_SRC_IP_" + ipAddress + " ");
        }
        executeIPTablesCommand("iptables -A INPUT -d " + ipAddress + " -j DROP");
        return executeIPTablesCommand("iptables -A INPUT -d " + ipAddress + "-j LOG --log-prefix FW_BLOCKED_DST_IP_" + ipAddress + " ");
    }

    public String blockPort(Integer portNumber, HostType hostType) throws IOException, InterruptedException {

        if(hostType.equals(HostType.SOURCE)) {
            executeIPTablesCommand("iptables -A INPUT -p tcp --sport " + portNumber + " -j DROP");
            return executeIPTablesCommand("iptables -A INPUT -p tcp --sport " + portNumber + " -j LOG --log-prefix FW_BLOCKED_SRC_PORT_" + portNumber + " ");
        }
        executeIPTablesCommand("iptables -A INPUT -p tcp --dport " + portNumber + " -j DROP");
        return executeIPTablesCommand("iptables -A INPUT -p tcp --dport " + portNumber + " -j LOG --log-prefix FW_BLOCKED_DST_PORT_" + portNumber + " ");
    }

    public String blockProtocol(Protocols protocol) throws IOException, InterruptedException {

        executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " -j DROP");
        return executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " -j LOG --log-prefix FW_BLOCKED_PROTOCOL_" + protocol.getProtocol() + " ");
    }

    public String limitRequests(LimitRequests limitRequests) throws IOException, InterruptedException {
        String sourceIP = limitRequests.getIpAddress();
        Integer requests = limitRequests.getRequests();
        Integer minutes = limitRequests.getMinutes();
        String ruleName = "RATE_LIMIT_" + limitRequests.getIpAddress();
        executeIPTablesCommand("iptables -A INPUT -s " + sourceIP + " -m limit --limit " + requests + " /min -j ACCEPT -m comment --comment " + ruleName);
        executeIPTablesCommand("iptables -A INPUT -s " + sourceIP + " -j LOG --log-prefix FW_BLOCKED_RATE_LIMIT" + sourceIP);
        return "Rule Added Successfully !";
    }

    private String executeIPTablesCommand(String command) throws IOException, InterruptedException {
        // executeCommand("iptables -F"); // Flush existing rules
        return executeCommand(command);
    }

    private String executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Error executing command: " + command);
        }
        return "Rule Added Successfully !";
    }

    public List<Rule> getRules() throws Exception {

        List<Rule> rules = new ArrayList<>();
        String[] data;
        Rule rule;
        Process process = Runtime.getRuntime().exec("iptables -L");

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.contains("Chain FORWARD (policy ACCEPT)")) {
                break;
            }
            if(line.contains("Chain INPUT (policy ACCEPT)")) {
                continue;
            }
            data = line.split("\\s+");
            if(data.length >= 5) {
                rule = new Rule(data[0],
                        data[1], data[2], data[3], data[4], "");
                if(data.length > 5) {
                    List<String> addData = new ArrayList<>();
                    for(int i = 5; i < data.length; i ++) {
                        addData.add(data[i]);
                    }
                    rule.setAdditionalData(String.join(", ", addData));
                }
                rules.add(rule);
            }
        }

        int exitCode = process.waitFor();
        System.out.println("Command exited with code: " + exitCode);

        return rules;
    }

    public Map<String, Integer> getBlockedRequestsByRule() {

        Map<String, Integer> blockedRequestsByRule = new HashMap<>();

        try {
            // Run the command to get iptables logs
            Process process = Runtime.getRuntime().exec("sudo cat /var/log/iptables.log");

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming logs have the format "[BLOCKED] [RuleName] ..."
                String[] parts = line.split("\\s+");
                if (parts.length >= 3 && "[BLOCKED]".equals(parts[0])) {
                    String ruleName = parts[1];
                    blockedRequestsByRule.put(ruleName, blockedRequestsByRule.getOrDefault(ruleName, 0) + 1);
                }
            }

            // Wait for the command to finish and get the exit code
            int exitCode = process.waitFor();
            System.out.println("Command exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return blockedRequestsByRule;
    }
}