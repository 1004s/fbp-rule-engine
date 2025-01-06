package com.fbp.message;

import com.fasterxml.jackson.databind.JsonNode;

public class MqttDataMessage extends Message {

    private final String topic;
    private final JsonNode jsonNode;

    public MqttDataMessage(String topic, JsonNode jsonNode) {
        super();
        this.topic = topic;
        this.jsonNode = jsonNode;
    }

    @Override
    public Message copy() {
        return new MqttDataMessage(topic, jsonNode);
    }
}
