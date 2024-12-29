package com.fbp.nodes.basic;

import com.fbp.message.Message;
import com.fbp.pipe.Pipe;
import com.fbp.wire.InputWire;

import java.util.Collection;

public abstract class InOutNode extends Node {
    protected InputWire inputWire;
    protected InputWire outputInputWire;
    protected InOutNode(String id) {
        super(id);
    }

    protected void putMessage(Message message) {
        Collection<Pipe> pipes = outputInputWire.getAllPipes();
        for (Pipe pipe : pipes) {
            pipe.offer(message);
        }
    }

    protected Message getMessage() {



        return null;
    }

}
