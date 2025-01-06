package com.fbp.nodes.concrete;

import com.fbp.nodes.basic.InOutNode;

public class ModbusDataScalingNode extends InOutNode {

    public ModbusDataScalingNode(String id) {
        super(id);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        while (!Thread.interrupted()) {

        }
    }

    @Override
    protected void terminate() {

    }
}
