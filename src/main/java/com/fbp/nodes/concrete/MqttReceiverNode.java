package com.fbp.nodes.concrete;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbp.message.MqttDataMessage;
import com.fbp.message.StatisticsMessage;
import com.fbp.nodes.basic.InNode;
import org.eclipse.paho.client.mqttv3.*;

public class MqttReceiverNode extends InNode {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String brokerAddress; // tcp://192.168.70.203:1883
    private final String clientId;
    private final String[] topics;
    private JsonNode options;
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    public MqttReceiverNode(String id, String brokerAddress, String clientId, String[] topics,
                            JsonNode options) {
        super(id);
        this.brokerAddress = brokerAddress;
        this.clientId = clientId;
        if (topics.length > 50) {
            StatisticsMessage errorMessage = new StatisticsMessage(getId(), StatisticsMessage.StatisticsDataType.ERROR,
                    "topic은 최대 50개만 가능, 현재 topic은 " + topics.length + "개");
            addMessage(errorMessage);
            throw new IllegalArgumentException("topic은 최대 50개만 가능");
        }
        this.topics = topics;
        this.options = options;
        try {
            mqttClient = new MqttClient(brokerAddress, clientId);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        mqttConnectOptions = new MqttConnectOptions();
    }

    @Override
    protected void initialize() {
        setCustomConnectOptions();
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

                // error log

                StatisticsMessage errorMessage = new StatisticsMessage(getId(),
                        StatisticsMessage.StatisticsDataType.ERROR, "Mqtt client connection lost!!");
                addMessage(errorMessage);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                StatisticsMessage inputMessage = new StatisticsMessage(getId(),
                        StatisticsMessage.StatisticsDataType.INPUT);
                addMessage(inputMessage);

                JsonNode payload = objectMapper.readTree(message.getPayload());
                MqttDataMessage dataMessage = new MqttDataMessage(topic, payload);
                addMessage(dataMessage);
                StatisticsMessage outputMessage = new StatisticsMessage(getId(),
                        StatisticsMessage.StatisticsDataType.OUTPUT);
                addMessage(outputMessage);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        try {
            mqttClient.connect(mqttConnectOptions);
        } catch (MqttException e) {
            StatisticsMessage errorMessage = new StatisticsMessage(getId(),
                    StatisticsMessage.StatisticsDataType.ERROR, "Mqtt client connection failed!!");
            addMessage(errorMessage);
            throw new RuntimeException(e);
        }

        try {
            mqttClient.subscribe(topics);
        } catch (MqttException e) {
            StatisticsMessage errorMessage = new StatisticsMessage(getId(),
                    StatisticsMessage.StatisticsDataType.ERROR, "Mqtt client subscribe failed!!");
            addMessage(errorMessage);
            throw new RuntimeException(e);
        }
    }

    private void setCustomConnectOptions() {
        if (options.get("cleanSession") != null && !options.get("cleanSession").booleanValue()) {
            mqttConnectOptions.setCleanSession(false);
        }
        if (options.get("userName") != null) {
            mqttConnectOptions.setUserName(options.get("userName").textValue());
        }
        if (options.get("password") != null) {
            mqttConnectOptions.setPassword(options.get("password").textValue().toCharArray());
        }
        if (options.get("will") != null) {
            JsonNode will = options.get("will");
            try {
                String topic = will.get("topic").textValue();
                byte[] payload = will.get("payload").textValue().getBytes();
                int qos = will.get("qos").intValue();
                boolean retained = will.get("retained").booleanValue();

                mqttConnectOptions.setWill(topic, payload, qos, retained);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("설정파일 형식이 잘못되었습니다. topic, payload, qos, retained 모두 포함해야 함");
            }
        }
        if (options.get("connectionTimeout") != null) {
            mqttConnectOptions.setConnectionTimeout(options.get("connectionTimeout").intValue());
        }
        if (options.get("keepAliveInterval") != null) {
            mqttConnectOptions.setKeepAliveInterval(options.get("keepAliveInterval").intValue());
        }
        if (options.get("automaticReconnect") != null) {
            mqttConnectOptions.setAutomaticReconnect(options.get("automaticReconnect").booleanValue());
        }
    }

    @Override
    protected void execute() {
        //
    }

    @Override
    protected void terminate() {
        try {
            this.mqttClient.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        // 여기서 outputWire도 멈춰줘야함 !!
    }
}
