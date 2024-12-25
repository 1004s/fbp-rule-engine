package com.fbp.funtional;

import com.fbp.pipe.Pipe;

import java.util.List;

public interface Wireable {
    void connect(Pipe pipe);
    void connect(String pipeId);
    void connectAll(List<Pipe> pipeList);
    void disconnect(Pipe pipe);
    void disconnect(String pipeId);
    void disconnectAll();
}
