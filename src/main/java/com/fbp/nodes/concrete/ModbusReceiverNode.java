package com.fbp.nodes.concrete;

import com.fbp.entity.Channel;
import com.fbp.entity.Offset;
import com.fbp.mapper.ModbusChannelMapper;
import com.fbp.mapper.ModbusOffsetMapper;
import com.fbp.message.ModbusDataMessage;
import com.fbp.nodes.basic.InNode;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadInputRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadInputRegistersResponse;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModbusReceiverNode extends InNode {

    /*
    나중에 서버주소나 포트 변경될 상황이 생기면 final 빼고 setter 만들기
     */
    private final String serverAddress = "192.168.70.203";   // 서버 IP
    private ModbusMaster modbusMaster;
    private ModbusChannelMapper channelMapper;
    private ModbusOffsetMapper offsetMapper;
    private Map<Integer, Channel> channelMap; // ket : channelID | value : Channel 객체
    private Map<Integer, Offset> offsetMap; // key : offsetID | value : Offset 객체
    private Map<Integer, ReadInputRegistersRequest> requestMap; // K : offsetID(실제 offset 과 동일) | value : RIRR 객체

    protected ModbusReceiverNode(String id) {
        super(id);
        channelMapper = new ModbusChannelMapper();
        offsetMapper = new ModbusOffsetMapper();
        requestMap = new HashMap<>();
    }

    @Override
    protected void initialize() {
        // channel, offset file 읽기
        channelMapper.readFileAndSerializeChannelObject();
        offsetMapper.readFileAndSerializeOffsetObject();

        // Modbus channelMap 가져오기
        channelMap = channelMapper.getChannelMap();
        offsetMap = offsetMapper.getOffsetMap();

        // channelMap과 offsetMap 내용을 이용해서 ReadInputRegisterRequest 객체 만들고 리스트에 저장
        for (Channel channel : channelMap.values()) {
            int channelStartAddress = channel.getChannelStartAddress();
            for (Offset offset : offsetMap.values()) {
                int offsetInt = offset.getOffset();
                int quantity = offset.getSize();

                ReadInputRegistersRequest request = new ReadInputRegistersRequest();
                try {
                    request.setServerAddress(1);
                    request.setStartAddress(channelStartAddress + offsetInt);
                    request.setQuantity(quantity);
                } catch (ModbusNumberException e) {
                    throw new RuntimeException(e);
                }
                requestMap.put(offsetInt, request);
            }
        }

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
        while (!Thread.currentThread().isInterrupted()) {
            // Modbus Master 객체를 이용해서 요청, 응답 받기
            try {
                if (!modbusMaster.isConnected()) {
                    modbusMaster.connect();
                }

                for (Integer id : requestMap.keySet()) {     // requestMap.keySet() : offset ID : request ID
                    ReadInputRegistersRequest request = requestMap.get(id);
                    Offset offset = offsetMap.get(id);

                    int quantity = offset.getSize();


                    ReadInputRegistersResponse response = (ReadInputRegistersResponse) modbusMaster.processRequest(request);
                    int value;
                    if (quantity == 1) {
                        value = response.getHoldingRegisters().get(0);
                    } else {
                        // Big-endian 방식
                        value = (response.getHoldingRegisters().get(0) << 16) | response.getHoldingRegisters().get(1);
                    }
                    System.out.println(new ModbusDataMessage(offset.getOffset(), offset.getName(), offset.getUnit(), offset.getScale(), value));
                    addMessage(new ModbusDataMessage(offset.getOffset(), offset.getName(), offset.getUnit(), offset.getScale(), value));

                    Thread.sleep(5000);
                }

            } catch (ModbusIOException | ModbusProtocolException | InterruptedException e) {
                throw new RuntimeException(e);
            }
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
