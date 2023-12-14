package com.firewall.app.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Counter {

    private int countValue;

    public Counter() {
        this.countValue = 0;
    }

    public int getIncrAndCnt() {
        this.countValue += 1;
        return countValue;
    }
}
