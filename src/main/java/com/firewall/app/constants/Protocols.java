package com.firewall.app.constants;

public enum Protocols {

    UDP("udp"),
    TCP("tcp");

    private String protocol;

    Protocols(String protocol) {
        this.protocol = protocol;
    }
    public String getProtocol() {

        return this.protocol;
    }
}
