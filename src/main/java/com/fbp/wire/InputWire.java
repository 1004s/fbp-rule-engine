package com.fbp.wire;

import com.fbp.message.Message;
import com.fbp.pipe.Pipe;

import java.util.*;

public class InputWire implements Runnable {

    private final Map<String, Pipe> pipeMap = new HashMap<>();
    private final ReadyQueue readyQueue;

    /*
    생성자 안쓰는거 지우기
     */
    public InputWire() {
        this.readyQueue = new ReadyQueue();
    }

    public InputWire(int capacity) {
        this.readyQueue = new ReadyQueue(capacity);
    }

    public void add(String pipeName, Pipe pipe) {
        pipeMap.put(pipeName, pipe);
    }

    public void remove(String pipeName) {
        pipeMap.remove(pipeName);
    }

    public Collection<Pipe> getAllPipes() {
        return pipeMap.values();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {

            List<Pipe> pipes = new ArrayList<>(getAllPipes());

            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                int size = pipe.size();
                for (int j = 0; j < size; j++) {
                    readyQueue.add(pipe.poll());
                }
            }
        }
    }

    /*
    고려사항
    - get(String pipeName) : 특정 pipe를 반환하는 메서드
     */

}
