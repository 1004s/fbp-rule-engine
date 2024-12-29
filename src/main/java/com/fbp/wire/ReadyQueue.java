package com.fbp.wire;

import com.fbp.message.Message;

import java.util.concurrent.LinkedBlockingQueue;

public class ReadyQueue {

    private static final int DEFAULT_CAPACITY = 1_000_000;
    private final LinkedBlockingQueue<Message> queue;

    public ReadyQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ReadyQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public void add(Message message) {
        queue.add(message);
    }

    public Message take() throws InterruptedException {
        return queue.take();
    }

}
