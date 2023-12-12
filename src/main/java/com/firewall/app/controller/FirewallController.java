package com.firewall.app.controller;

import com.firewall.app.model.BlockIPRequest;
import com.firewall.app.model.BlockPortRequest;
import com.firewall.app.model.BlockProtocolRequest;
import com.firewall.app.model.LimitRequests;
import com.firewall.app.service.FirewallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firewall/")
public class FirewallController {

    @Autowired
    private FirewallService firewallService;

    /**
     *
     * @param blockIPRequest
     * @return
     */
    @PostMapping("/block-ip/")
    public ResponseEntity<String> blockIp(@RequestBody BlockIPRequest blockIPRequest) {

        try {
            return ResponseEntity.ok(
                    firewallService.blockIP(blockIPRequest.getIpAddress(), blockIPRequest.getHostType())
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("There is an Issue while blocking the IP !");
        }
    }

    /**
     *
     * @param blockPortRequest
     * @return
     */
    @PostMapping("/block-port/")
    public ResponseEntity<String> blockPort(@RequestBody BlockPortRequest blockPortRequest) {

        try {
            return ResponseEntity.ok(
                    firewallService.blockPort(blockPortRequest.getPortNumber(), blockPortRequest.getHostType(), blockPortRequest.getProtocol())
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("There is an Issue while blocking the Port !");
        }
    }

    /**
     *
     * @param blockProtocolRequest
     * @return
     */
    @PostMapping("/block-protocol/")
    public ResponseEntity<String> blockProtocol(@RequestBody BlockProtocolRequest blockProtocolRequest) {

        try {
            return ResponseEntity.ok(
                    firewallService.blockProtocol(blockProtocolRequest.getProtocol())
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("There is an Issue while blocking the Protocol !");
        }
    }

    /**
     *
     * @param limitRequests
     * @return
     */
    @PostMapping("/limit-requests/")
    public ResponseEntity<String> limitRequests(@RequestBody LimitRequests limitRequests) {

        try {
            return ResponseEntity.ok(
                    firewallService.limitRequests(limitRequests)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("There is an Issue while Limiting the Requests !");
        }
    }

    /**
     *
     * @return
     */
    @GetMapping("/getRules/")
    public ResponseEntity getRules() {

        try {
            return ResponseEntity.ok(
                    firewallService.getRules()
            );
        } catch(Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Issue while reading the Rules !");
        }
    }
}