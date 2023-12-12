package com.firewall.app.constants;

import java.util.Map;
import java.util.TreeMap;

public enum Direction {

    INCOMING("in"),
    OUTGOING("out");

    private String direction;

    Direction(String direction) {

        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }

    public static Map<Direction, String> getDirections() {

        Map<Direction, String> directions = new TreeMap<>();
        for(Direction direction : Direction.values()) {
            directions.put(direction, direction.getDirection());
        }
        return directions;
    }
}
