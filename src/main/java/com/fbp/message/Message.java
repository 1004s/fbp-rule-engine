package com.fbp.message;

import java.util.UUID;

public abstract class Message {

    private final String id;

    protected Message() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public abstract Message copy();

}
