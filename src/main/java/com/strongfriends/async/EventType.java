package com.strongfriends.async;

public enum EventType {
    COMMENT(0),
    LIKE(1),
    DISLIKE(2),
    MESSAGE(3),
    POST(4);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

