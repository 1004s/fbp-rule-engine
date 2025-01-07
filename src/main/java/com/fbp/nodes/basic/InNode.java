package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.wire.OutputWire;

public abstract class InNode extends Node {

    private final OutputWire outputWire;
    private final Thread outputWireThread;

    @Override
    protected void startWire() {
        outputWireThread.start();
    }

    @Override
    protected void stopWire() {
        outputWireThread.interrupt();
    }

    protected InNode(String id) {
        super(id);
        outputWire = new OutputWire();
        outputWireThread = new Thread(outputWire);
    }

    protected void addMessage(Message message) {
        outputWire.addMessage(message);
    }
}
