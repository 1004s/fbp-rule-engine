package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.pipe.Pipe;
import com.fbp.wire.InputWire;

import java.util.Collection;

public abstract class InNode extends Node {

    protected InputWire outputInputWire;

    protected InNode(String id) {
        super(id);
    }

    protected void putMessage(Message message) {

        Collection<Pipe> pipes = outputInputWire.getAllPipes();
        for (Pipe pipe : pipes) {
            pipe.offer(message.copy());
        }
    }
}
