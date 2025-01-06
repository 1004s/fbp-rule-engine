package com.fbp.nodes.basic;

public abstract class Node implements Runnable {

    private final String id;
    protected String name;

    protected Node(String id) {
        this.id = id;
    }

    protected abstract void startWire();

    protected abstract void initialize();

    protected abstract void execute();

    protected abstract void terminate();

   public void kill() {
        Thread.currentThread().interrupt(); // 어느 스레드가 종료 되는지 테스트 좀
    }

    @Override
    public void run() {
        startWire();
        initialize();
        execute();
        terminate();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
