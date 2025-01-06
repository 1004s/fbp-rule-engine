package com.fbp.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbp.entity.Offset;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModbusOffsetMapper {

    private final Map<Integer, Offset> offsetMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String JSON_KEY_OFFSET_ARRAY = "offsets";
    private static final String JSON_KEY_OFFSET = "offset";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_TYPE = "type";
    private static final String JSON_KEY_UNIT = "unit";
    private static final String JSON_KEY_SIZE = "size";
    private static final String JSON_KEY_SCALE = "scale";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_OFFSET_FILE_PATH = "src/main/resources/offset.json";

    public void readFileAndSerializeOffsetObject() {
        // json 파일 읽어서 offset 객체 만들기
        try {
            JsonNode rootNode = objectMapper.readTree(new File(JSON_OFFSET_FILE_PATH));
            JsonNode offsetArray = rootNode.get(JSON_KEY_OFFSET_ARRAY);

            for (JsonNode jsonNode : offsetArray) {
                int offset = jsonNode.get(JSON_KEY_OFFSET).asInt();
                String name = jsonNode.get(JSON_KEY_NAME).asText();
                String type = jsonNode.get(JSON_KEY_TYPE).asText();
                String unit = jsonNode.get(JSON_KEY_UNIT).asText();
                int size = jsonNode.get(JSON_KEY_SIZE).asInt();
                int scale = jsonNode.get(JSON_KEY_SCALE).asInt();
                String desc = jsonNode.get(JSON_KEY_DESCRIPTION).asText();

                Offset offsetObject = new Offset(offset,name,type,unit,size,scale,desc);
                offsetMap.put(offset, offsetObject);
            }

            System.out.println(offsetMap);  // 지우기
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Map<Integer, Offset> getOffsetMap() {
        return offsetMap;
    }
}
