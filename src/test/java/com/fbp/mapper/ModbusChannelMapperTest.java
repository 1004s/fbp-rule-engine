package com.fbp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModbusChannelMapperTest {

    private ModbusChannelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ModbusChannelMapper();
    }

    @Test
    @DisplayName("Json파일 역직렬화 확인")
    void readFileAndDeserializeChannelObject_Success() {

    }

    private File createTempJsonFile(String content) {
        try {
            File tempFile = new File("src/test/resources/channel.json");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(content);
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file", e);
        }
    }


}