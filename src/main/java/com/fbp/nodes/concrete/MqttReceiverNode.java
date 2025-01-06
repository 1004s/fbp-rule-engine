package com.fbp.nodes.concrete;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbp.message.MqttDataMessage;
import com.fbp.message.StatisticsMessage;
import com.fbp.nodes.basic.InNode;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;

import java.util.Arrays;

public class MqttReceiverNode extends InNode {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String brokerHost;
    private final int brokerPort;
    private final String clientId;
    private final String[] topics;
    private JsonNode options;
    private Mqtt3AsyncClient mqttClient;

    public MqttReceiverNode(String id, String brokerHost, int brokerPort,
                            String clientId, String[] topics,
                            JsonNode options) {
        super(id);
        this.brokerHost = brokerHost;
        this.brokerPort = brokerPort;
        this.clientId = clientId;
        if (topics.length > 50) {
            StatisticsMessage errorMessage = new StatisticsMessage(getId(), StatisticsMessage.StatisticsDataType.ERROR,
                    "topic은 최대 50개만 가능, 현재 topic은 " + topics.length + "개");
            addMessage(errorMessage);
            throw new IllegalArgumentException("topic은 최대 50개만 가능");
        }
        this.topics = topics;
        this.options = options;
    }

    @Override
    protected void initialize() {
        mqttClient = Mqtt3Client.builder()
                .identifier(clientId)
                .serverHost(brokerHost)
                .serverPort(brokerPort)
                .buildAsync();
        connectWithCustomConnectOptions();
    }

    private void connectWithCustomConnectOptions() {
        boolean cleanSession = true;
        int keepAlive = 60;
        if (options.get("cleanSession") != null && !options.get("cleanSession").booleanValue()) {
            cleanSession = false;
        }
        if (options.get("keepAliveInterval") != null) {
            keepAlive = options.get("keepAliveInterval").intValue();
        }
        if (options.get("will") != null) {
            JsonNode will = options.get("will");
            try {
                String topic = will.get("topic").textValue();
                byte[] payload = will.get("payload").textValue().getBytes();
                String qos = will.get("qos").textValue();
                boolean retained = will.get("retained").booleanValue();

                mqttClient.connectWith()
                        .cleanSession(cleanSession)
                        .willPublish()
                            .topic(topic)
                            .payload(payload)
                            .qos(MqttQos.valueOf(qos))
                            .retain(retained).applyWillPublish()
                        .keepAlive(keepAlive)
                        .send();
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("설정파일 형식이 잘못되었습니다. topic, payload, qos, retained 모두 포함해야 함");
            }
        } else {
            mqttClient.connectWith()
                    .cleanSession(cleanSession)
                    .keepAlive(keepAlive)
                    .send();
        }
    }

    @Override
    protected void execute() {
        Arrays.stream(topics).forEach(topic ->
                mqttClient.subscribeWith()
                        .topicFilter(topic)
                        .callback(mqttMessage -> {
                            try {
                                String receivedTopic = mqttMessage.getTopic().toString();
                                try {
                                    JsonNode payload = objectMapper.readTree(mqttMessage.getPayloadAsBytes());
                                    // TODO : log
                                    addMessage(new MqttDataMessage(receivedTopic, payload));
                                    addMessage(new StatisticsMessage(getId(),
                                            StatisticsMessage.StatisticsDataType.OUTPUT));
                                } catch (JsonParseException e) {
                                    addMessage(new StatisticsMessage(getId(),
                                            StatisticsMessage.StatisticsDataType.ERROR,
                                            e.getMessage()));
                                }
                            } catch (Exception e) {
                                System.err.println("Error processing message: " + e.getMessage());
                            }
                        })
                        .send()
                        .whenComplete((subAck, throwable) -> {
                            if (throwable != null) {
                                // TODO log
                                addMessage(new StatisticsMessage(getId(), StatisticsMessage.StatisticsDataType.ERROR,
                                        throwable.getMessage()));
                            } else {
                                // TODO log (topic 구독 성공)
                            }
                        })
        );
    }

    @Override
    protected void terminate() {
        this.mqttClient.disconnect();

        // 여기서 outputWire도 멈춰줘야함 !!
    }

}
