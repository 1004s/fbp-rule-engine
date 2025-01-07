package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.wire.InputWire;
import com.fbp.wire.OutputWire;


public abstract class InOutNode extends Node {
    private final InputWire inputWire;
    private final OutputWire outputWire;
    private final Thread inputWireThread;
    private final Thread outputWireThread;

    protected InOutNode(String id) {
        super(id);
        this.inputWire = new InputWire();
        this.outputWire = new OutputWire();
        this.inputWireThread = new Thread(inputWire);
        this.outputWireThread = new Thread(outputWire);
    }

    @Override
    protected void startWire() {
        inputWireThread.start();
        outputWireThread.start();
    }

    @Override
    protected void stopWire() {
        inputWireThread.interrupt();
        outputWireThread.interrupt();
    }

    protected void addMessage(Message message) {
        outputWire.addMessage(message);
    }

    protected Message takeMessage() throws InterruptedException {
        return inputWire.takeMessage();
    }

}
