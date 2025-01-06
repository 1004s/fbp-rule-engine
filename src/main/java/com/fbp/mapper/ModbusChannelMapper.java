package com.fbp.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbp.entity.Channel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModbusChannelMapper {

    private final Map<Integer, Channel> channelMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String JSON_KEY_CHANNEL_ARRAY = "channels";
    private static final String JSON_KEY_CHANNEL = "channel";
    private static final String JSON_KEY_ADDRESS = "address";
    private static final String JSON_KEY_LOCATION = "location";
    private static final String JSON_CHANNEL_FILE_PATH = "src/main/resources/channel.json";

    public void readFileAndSerializeChannelObject() {
        try {
            JsonNode rootNode = objectMapper.readTree(new File(JSON_CHANNEL_FILE_PATH));
            JsonNode channelArray = rootNode.get(JSON_KEY_CHANNEL_ARRAY);

            for (JsonNode jsonNode : channelArray) {
                int channel = jsonNode.get(JSON_KEY_CHANNEL).asInt();
                int address = jsonNode.get(JSON_KEY_ADDRESS).asInt();
                String location = jsonNode.get(JSON_KEY_LOCATION).asText();

                Channel channelObject = new Channel(channel,address,location);

                channelMap.put(channel, channelObject);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Map<Integer, Channel> getChannelMap() {
        return channelMap;
    }

}
