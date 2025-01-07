package com.fbp.nodes.concrete;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbp.message.MqttDataMessage;
import com.fbp.message.StatisticsMessage;
import com.fbp.nodes.basic.InNode;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class MqttReceiverNode extends InNode {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String brokerHost;
    private final int brokerPort;
    private final String clientId;
    private final Set<String> topics;
    private final JsonNode options;
    private Mqtt3AsyncClient mqttClient;

    public MqttReceiverNode(String id, String brokerHost, int brokerPort,
                            String clientId, Set<String> topics, JsonNode options) {
        super(id);
        this.brokerHost = brokerHost;
        this.brokerPort = brokerPort;
        this.clientId = clientId;
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
        log.debug("Mqtt 클라이언트 연결되었음.");
    }

    @Override
    protected void execute() {
        topics.forEach(topic ->
                mqttClient.subscribeWith()
                        .topicFilter(topic)
                        .callback(mqttClientCallback())
                        .send()
                        .whenComplete(subscribeCompleteAction())
        );
    }

    private BiConsumer<Mqtt3SubAck, Throwable> subscribeCompleteAction() {
        return (subAck, throwable) -> {
            if (throwable != null) {
                log.error("topic 구독 실패: {}", throwable.getMessage());
                addMessage(new StatisticsMessage(getId(), StatisticsMessage.StatisticsDataType.ERROR, throwable.getMessage()));
            } else {
                log.debug("topic 구독 성공");
            }
        };
    }

    private Consumer<Mqtt3Publish> mqttClientCallback() {
        return receivedMessage -> {
            try {
                String topic = receivedMessage.getTopic().toString();
                JsonNode payload = objectMapper.readTree(receivedMessage.getPayloadAsBytes());
                log.debug("Mqtt 메시지 수신 topic={}, payload={}", topic, payload);

                addMessage(new MqttDataMessage(topic, payload));
                addMessage(new StatisticsMessage(getId(), StatisticsMessage.StatisticsDataType.OUTPUT));
            } catch (IOException e) {
                addMessage(new StatisticsMessage(getId(), StatisticsMessage.StatisticsDataType.ERROR, e.getMessage()));
            }
        };
    }

    @Override
    protected void terminate() {
        this.mqttClient.disconnect();

        // 여기서 outputWire도 멈춰줘야함 !!
    }

}
