package com.fbp.nodes.concrete;

import com.fbp.nodes.basic.InNode;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ModbusReceiverNode extends InNode {

    /*
    나중에 서버주소나 포트 변경될 상황이 생기면 final 빼고 setter 만들기
     */
    private final String serverAddress = "192.168.70.203";   // 서버 IP
    private TcpParameters tcpParameters;

    protected ModbusReceiverNode(String id) {
        super(id);
    }

    @Override
    protected void initialize() {
        // TcpParameters 이용해서 Modbus Master 객체 만들기
    }

    @Override
    protected void execute() {
        // json 파일들 이용해서 요청 메시지 만들기
        // Modbus Master 객체를 이용해서 요청, 응답 받기
    }

    @Override
    protected void terminate() {

    }
}
