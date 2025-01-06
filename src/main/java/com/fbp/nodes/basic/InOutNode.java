package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.wire.InputWire;
import com.fbp.wire.OutputWire;


public abstract class InOutNode extends Node {
    private final InputWire inputWire;
    private final OutputWire outputWire;

    public InOutNode(String id) {
        super(id);
        this.inputWire = new InputWire();
        this.outputWire = new OutputWire();
    }

    @Override
    protected void startWire() {
        new Thread(inputWire).start();
        new Thread(outputWire).start();
    }

    protected void addMessage(Message message) {
        outputWire.addMessage(message);
    }

    protected Message takeMessage() throws InterruptedException {
        return inputWire.takeMessage();
    }

}
