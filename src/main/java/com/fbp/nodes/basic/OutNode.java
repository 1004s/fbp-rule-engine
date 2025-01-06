package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.wire.InputWire;

public abstract class OutNode extends Node {

    private final InputWire inputWire;

    public OutNode(String id) {
        super(id);
        this.inputWire = new InputWire();
    }

    @Override
    protected void startWire() {
        new Thread(inputWire).start();
    }

    protected Message takeMessage() throws InterruptedException {
        return inputWire.takeMessage();
    }

}
