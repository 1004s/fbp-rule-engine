package com.fbp.pipe;

import com.fbp.message.Message;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pipe {

    private BlockingQueue<Message> queue;

    public Pipe() {
        // ArrayBlockingQueue 대신
        this.queue = new LinkedBlockingQueue<>();
    }

    public void offer(Message message) {
        boolean result = queue.offer(message);
        System.out.println("메시지 input 결과 : " + result);
    }

    public Message poll() {
        return queue.poll();
    }

    /*
    우선 offer와 poll 메서드 안에서 예외나 대기 시키는게 아닌
    큐에 값을 넣으면 true, 꽉 차서 못넣으면 false
    큐에 값을 꺼내면 Message, 비어있다면 null
    위의 걸로 해놨고 추후에 예외를 발생시키거나 대기시켜야 하는 상황이
    필요하면 변경하는 걸로.

    peek() 같은 메서드가 필요할까?
     */

}
