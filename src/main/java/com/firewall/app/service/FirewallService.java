package com.firewall.app.service;

import com.firewall.app.constants.HostType;
import com.firewall.app.constants.Protocols;
import com.firewall.app.model.LimitRequests;
import com.firewall.app.model.Log;
import com.firewall.app.model.Rule;
import com.firewall.app.model.Stats;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class FirewallService {

    /**
     *
     * @param ipAddress
     * @param hostType
     * @return String - status of Rule Insertion to IPTables
     * @throws IOException
     * @throws InterruptedException
     */
    public String blockIP(String ipAddress, HostType hostType) throws IOException, InterruptedException {

        if(hostType.equals(HostType.SOURCE)) {
            executeIPTablesCommand("iptables -A INPUT -s " + ipAddress + " -j LOG --log-prefix FW_BLOCKED_SRC_IP_" + ipAddress + " ");
            return executeIPTablesCommand("iptables -A INPUT -s " + ipAddress + " -j DROP -m comment --comment BlockedIP");
        }
        executeIPTablesCommand("iptables -A INPUT -d " + ipAddress + " -j LOG --log-prefix FW_BLOCKED_DST_IP_" + ipAddress + " ");
        return executeIPTablesCommand("iptables -A INPUT -d " + ipAddress + " -j DROP");
    }

    /**
     *
     * @param portNumber
     * @param hostType
     * @param protocol
     * @return String - status of Rule Insertion to IPTables
     * @throws IOException
     * @throws InterruptedException
     */
    public String blockPort(Integer portNumber, HostType hostType, Protocols protocol) throws IOException, InterruptedException {

        if(hostType.equals(HostType.SOURCE)) {

            executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " --sport " + portNumber + " -j LOG --log-prefix FW_BLOCKED_SRC_PORT_" + portNumber + " ");
            return executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " --sport " + portNumber + " -j DROP");
        }
        executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " --dport " + portNumber + " -j LOG --log-prefix FW_BLOCKED_DST_PORT_" + portNumber + " ");
        return executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " --dport " + portNumber + " -j DROP");
    }

    /**
     *
     * @param protocol
     * @return String - status of Rule Insertion to IPTables
     * @throws IOException
     * @throws InterruptedException
     */
    public String blockProtocol(Protocols protocol) throws IOException, InterruptedException {

        executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " -j LOG --log-prefix FW_BLOCKED_PROTOCOL_" + protocol.getProtocol() + " ");
        return executeIPTablesCommand("iptables -A INPUT -p " + protocol.getProtocol() + " -j DROP");
    }

    /**
     *
     * @param limitRequests
     * @return String - status of Rule Insertion to IPTables
     * @throws IOException
     * @throws InterruptedException
     */
    public String limitRequests(LimitRequests limitRequests) throws IOException, InterruptedException {
        String sourceIP = limitRequests.getIpAddress();
        Integer requests = limitRequests.getRequests();
        Integer minutes = limitRequests.getMinutes();
        String ruleName = "RATE_LIMIT_" + limitRequests.getIpAddress();
        executeIPTablesCommand("iptables -A INPUT -s " + sourceIP + " -j LOG --log-prefix FW_BLOCKED_RATE_LIMIT" + sourceIP);
        executeIPTablesCommand("iptables -A INPUT -s " + sourceIP + " -m limit --limit " + requests + "/min -j ACCEPT -m comment --comment " + ruleName);
        return "Rule Added Successfully !";
    }

    /**
     *
     * @return List of Logs recorded using journalctl
     */
    public Map<String, List<Log>> getLogs() {

        List<Log> blockedTraffic = new ArrayList<>();
        List<Log> incomingTraffic = new ArrayList<>();
        List<Log> outgoingTraffic = new ArrayList<>();
        Map<String, List<Log>> logs = new TreeMap<>();
        try {
            Process process = Runtime.getRuntime().exec("cat /var/log/iptables.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if(! line.contains("--log-prefix 'FW_BLOCKED_")) {
                    if(line.contains("FW_BLOCKED_INPUT")) {

                        incomingTraffic.add(parseLog(line));
                    } else if(line.contains("FW_BLOCKED_OUTPUT")) {

                        outgoingTraffic.add(parseLog(line));
                    } else {

                        blockedTraffic.add(parseLog(line));
                    }
                }
            }
            logs.put("INPUT", getSubList(incomingTraffic));
            logs.put("OUTPUT", getSubList(outgoingTraffic));
            logs.put("BLOCKED", getSubList(blockedTraffic));
            System.out.println("command exited with status : " + process.waitFor());
            return logs;
        } catch (Exception e) {
            return logs;
        }
    }

    /**
     *
     * @param command
     * @return Execution Status
     * @throws IOException
     * @throws InterruptedException
     */
    private String executeIPTablesCommand(String command) throws IOException, InterruptedException {
        return executeCommand(command);
    }

    /**
     *
     * @param command
     * @return Execution status
     * @throws IOException
     * @throws InterruptedException
     */
    private String executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Error executing command: " + command);
        }
        return "Rule Added Successfully !";
    }

    /**
     *
     * @return Statistics of Inserted Rules
     */
    public Stats getStatistics() {

        Stats statistics = new Stats(0, 0, 0, 0, 0, 0);
        try {
            List<Rule> rules = getRules();
            for(Rule rule : rules) {
                if(rule.getTarget().trim().equals("DROP")) {
                    if (!rule.getSource().trim().equals("anywhere")) {
                        statistics.setBlockedSourceIps(statistics.getBlockedSourceIps() + 1);
                    }
                    if(!rule.getDestination().trim().equals("anywhere")) {
                        statistics.setBlockedDestIps(statistics.getBlockedDestIps() + 1);
                    }
                    if (rule.getAdditionalData().contains("spt:")) {
                        statistics.setBlockedSourcePorts(statistics.getBlockedSourcePorts() + 1);
                    }
                    if (rule.getAdditionalData().contains("dpt:")) {
                        statistics.setBlockedDestPorts(statistics.getBlockedDestPorts() + 1);
                    }
                    if (! rule.getProtocol().trim().equals("all")) {
                        statistics.setBlockedProtocols(statistics.getBlockedProtocols() + 1);
                    }
                } else if(rule.getTarget().trim().contains("ACCEPT")) {
                    if (rule.getAdditionalData().trim().contains("limit:")) {
                        statistics.setLimitedIps(statistics.getLimitedIps() + 1);
                    }
                }
            }
            return statistics;
        } catch(Exception e) {
            return statistics;
        }
    }

    /**
     *
     * @return List of Rules inserted in IPtables
     * @throws Exception
     */
    public List<Rule> getRules() throws Exception {

        List<Rule> rules = new ArrayList<>();
        String[] data;
        Rule rule;
        Process process = Runtime.getRuntime().exec("iptables -L -v --line-numbers");

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.contains("Chain FORWARD (policy ACCEPT)")) {
                break;
            }
            if(line.contains("Chain INPUT (policy ACCEPT)") || line.contains("target")) {
                continue;
            }
            data = line.split("\\s+");
            if(data.length >= 10) {
                rule = new Rule(data[0],
                        data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], "");
                if(data.length > 10) {
                    List<String> addData = new ArrayList<>();
                    for(int i = 10; i < data.length; i ++) {
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

    public void checkAndAddDefaultRules(String type) {

        try {
            executeCommand("iptables -C " + type + " -j LOG --log-prefix 'FW_BLOCKED_" + type + " '");
        } catch (Exception e) {
            try {
                executeCommand("iptables -A " + type + " -j LOG --log-prefix 'FW_BLOCKED_" + type + " '");
            } catch (Exception ex) {
                //TODO: To be Logged - ex.getMessage();
            }
        }
    }

    private Log parseLog(String logData) {

        String[] data = logData.split("\\s+");
        Log log = new Log();
        log.setTimestamp(data[0].trim());
        for(String logChunck : data) {
            if(logChunck.contains("FW_BLOCKED")) {
                log.setReason(logChunck.trim());
            } else if(logChunck.contains("SRC=")) {
                log.setSource(logChunck.trim().replace("SRC=", ""));
            } else if(logChunck.contains("DST=")) {
                log.setDestination(logChunck.trim().replace("DST=", ""));
            } else if(logChunck.contains("PROTO=")) {
                log.setProtocol(logChunck.trim().replace("PROTO=", ""));
            }
        }
        return log;
    }

    private List<Log> getSubList(List<Log> logs) {
        int size = logs.size() - 1;
        if(size >= 49) {

            return logs.subList(size - 49, size);
        }
        return logs;
    }
}