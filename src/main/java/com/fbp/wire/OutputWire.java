package com.fbp.wire;

import com.fbp.message.Message;
import com.fbp.message.StatisticsMessage;
import com.fbp.pipe.Pipe;

import java.util.*;

public class OutputWire implements Runnable {

    private final Map<String, Pipe> pipeMap = new HashMap<>();
    private final ReadyQueue readyQueue;
    private Collection<Pipe> pipes;
    private Pipe statisticsPipe;

    public OutputWire() {
        this.readyQueue = new ReadyQueue();
        pipes = getPipes();
    }

    public OutputWire(int capacity) {
        this.readyQueue = new ReadyQueue(capacity);
        pipes = getPipes();
    }

    public void addMessage(Message message) {
        readyQueue.add(message);
    }

    public void addPipe(String pipeName, Pipe pipe) {
        pipeMap.put(pipeName, pipe);
        pipes = getPipes();
    }

    public void setStatisticsPipe(Pipe pipe) {
        this.statisticsPipe = pipe;
    }

    public void removePipe(String pipeName) {
        pipeMap.remove(pipeName);
        pipes = getPipes();
    }

    public Collection<Pipe> getPipes() {
        return pipeMap.values();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Message message = readyQueue.take();
                if (message instanceof StatisticsMessage) {
                    statisticsPipe.offer(message);
                    continue;
                }

                for (Pipe pipe : pipes) {
                    pipe.offer(message.copy());
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "Output Wire의 ReadyQueue에 메시지 주입 인터럽트 발생!");
            }
        }
    }

    /*
    pipe가 추가되거나 삭제 될 때 파이프리스트를 최신화 하는 방법
    우선 add, remove 할 때 마다 필드(파이프리스트)를 업데이트 하는 중
     */
}
