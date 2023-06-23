package ch.frankel.blog.sessions;

import java.io.Serializable;

public class Counter implements Serializable {

    private int value = 0;

    public int getValue() {
        return value;
    }

    public void incrementValue() {
        value++;
    }
}
