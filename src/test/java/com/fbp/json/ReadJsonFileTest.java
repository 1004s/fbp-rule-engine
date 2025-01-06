package com.fbp.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbp.mapper.ModbusChannelMapper;
import com.fbp.mapper.ModbusOffsetMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ReadJsonFileTest {

    @Test
    @DisplayName("channel.json 파일 읽는지 확인")
    void readChannelFile() {
        //given
        ModbusChannelMapper mapper = new ModbusChannelMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        mapper.readFileAndSerializeChannelObject();
        //when
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/channel.json"));

            // then
            assertAll(
                    // JSON 파일이 비어있지 않은지 확인
                    () -> assertNotNull(rootNode, "JSON 파일이 비어있지 않아야 합니다"),

                    // channels 필드 존재 확인
                    () -> assertTrue(rootNode.has("channels"), "channels 필드가 존재해야 합니다"),

                    // channels가 배열인지 확인
                    () -> assertTrue(rootNode.get("channels").isArray(), "channels는 배열이어야 합니다"),

                    // channels 배열이 비어있지 않은지 확인
                    () -> assertTrue(rootNode.get("channels").size() > 0, "channels 배열은 비어있지 않아야 합니다")
            );
        } catch (IOException e) {
            fail("JSON 파일 읽기 실패: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("offset.json 파일 읽는지 확인")
    void readOffsetFile() {
        ModbusOffsetMapper mapper = new ModbusOffsetMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        mapper.readFileAndSerializeOffsetObject();


    }

}