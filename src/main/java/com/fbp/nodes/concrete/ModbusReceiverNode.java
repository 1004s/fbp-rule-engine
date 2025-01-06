package com.fbp.nodes.concrete;

import com.fbp.entity.Channel;
import com.fbp.mapper.ModbusChannelMapper;
import com.fbp.nodes.basic.InNode;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class ModbusReceiverNode extends InNode {

    /*
    나중에 서버주소나 포트 변경될 상황이 생기면 final 빼고 setter 만들기
     */
    private final String serverAddress = "192.168.70.203";   // 서버 IP
    private ModbusMaster modbusMaster;
    private ModbusChannelMapper modbusChannelMapper;
    private Map<Integer, Channel> channelMap;

    protected ModbusReceiverNode(String id) {
        super(id);
        modbusChannelMapper = new ModbusChannelMapper();
    }

    @Override
    protected void initialize() {
        // Modbus channelMap 가져오기
        channelMap = modbusChannelMapper.getChannelMap();

        // TcpParameters 이용해서 Modbus Master 객체 만들기
        TcpParameters tcpParameters = new TcpParameters();
        try {
            tcpParameters.setHost(InetAddress.getByName(serverAddress));
            tcpParameters.setKeepAlive(true);
            tcpParameters.setPort(Modbus.TCP_PORT);

            modbusMaster = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);     // 라이브러리가 자동으로 각 요청마다 트랜잭션 ID를 1씩 증가시킴, 개발자가 수동으로 트랜잭션 ID를 관리할 필요가 없어짐
                                                            // 클라이언트(Master)가 요청을 보낼때 식별자로 트랜잭션 ID를 사용, 서버(slave)는 응답할 때 동일한 트랜잭션 ID를 포함해서 보냄

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void execute() {
        // json 파일들 이용해서 요청 메시지 만들기
        // Modbus Master 객체를 이용해서 요청, 응답 받기
        try {
            if (!modbusMaster.isConnected()) {
                modbusMaster.connect();
            }
        } catch (ModbusIOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void terminate() {
        try {
            modbusMaster.disconnect();
        } catch (ModbusIOException e) {
            throw new RuntimeException(e);
        }
    }
}
