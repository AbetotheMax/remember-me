package com.example.marc.rememberme.feature;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc on 2/27/2018.
 */

enum Suit {

    CLUBS(0),
    SPADES(1),
    DIAMONDS(2),
    HEARTS(3);

    private int value;
    private static Map map = new HashMap<>();

    Suit(int value) {

        this.value = value;

    }

    static {

        for(Suit suit : Suit.values()) {

            map.put(suit.value, suit);

        }

    }

    public static Suit valueOf(int suit) {

        return (Suit) map.get(suit);

    }

    public int getValue() {

        return value;

    }

}
