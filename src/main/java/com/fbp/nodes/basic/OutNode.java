package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.wire.InputWire;

public abstract class OutNode extends Node {

    private final InputWire inputWire;
    private final Thread inputWireThread;

    public OutNode(String id) {
        super(id);
        this.inputWire = new InputWire();
        this.inputWireThread = new Thread(inputWire);
    }

    @Override
    protected void startWire() {
        inputWireThread.start();
    }

    @Override
    protected void stopWire() {
        inputWireThread.interrupt();
    }

    protected Message takeMessage() throws InterruptedException {
        return inputWire.takeMessage();
    }

}
