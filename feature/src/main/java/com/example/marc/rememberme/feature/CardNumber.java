package com.example.marc.rememberme.feature;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc on 2/27/2018.
 */

public enum CardNumber {

    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    private int value;
    private static Map map = new HashMap<>();

    CardNumber(int value) {

        this.value = value;

    }

    static {

        for(CardNumber cardNumber : CardNumber.values()) {

            map.put(cardNumber.value, cardNumber);

        }

    }

    public static CardNumber valueOf(int cardNumber) {

        return (CardNumber) map.get(cardNumber);

    }

    public int getValue() {

        return value;

    }


}
