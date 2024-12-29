package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.wire.OutputWire;

public abstract class InNode extends Node {

    private final OutputWire outputWire;

    protected InNode(String id) {
        super(id);
        outputWire = new OutputWire();
    }

    protected void addMessage(Message message) {
        outputWire.addMessage(message);
    }
}
