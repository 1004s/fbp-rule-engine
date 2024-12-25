package com.fbp.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsMessageTest {

    @Test
    @DisplayName("메시지가 생성되면 모든 필드는 null이 아니어야 한다")
    void testStatisticsMessageWithAllArgsConstructor() {
        String id = "test-id";
        String senderNodeId = "node-123";
        LocalDateTime createdAt = LocalDateTime.now();
        StatisticsMessage.StatisticsDataType dataType = StatisticsMessage.StatisticsDataType.INPUT;

        StatisticsMessage message = new StatisticsMessage(id, senderNodeId, createdAt, dataType);

        assertEquals(id, message.getId());
        assertEquals(senderNodeId, message.getSenderNodeId());
        assertEquals(createdAt, message.getCreatedAt());
        assertEquals(dataType, message.getStatisticsDataType());
    }

    @Test
    void testStatisticsMessageWithPartialArgsConstructor() {
        String senderNodeId = "node-456";
        LocalDateTime createdAt = LocalDateTime.now();
        StatisticsMessage.StatisticsDataType dataType = StatisticsMessage.StatisticsDataType.ERROR;

        StatisticsMessage message = new StatisticsMessage(senderNodeId, createdAt, dataType);

        assertNotNull(message.getId());
        assertEquals(senderNodeId, message.getSenderNodeId());
        assertEquals(createdAt, message.getCreatedAt());
        assertEquals(dataType, message.getStatisticsDataType());
    }

    @Test
    @DisplayName("서로 다른 메시지는 UUID가 달라야 한다")
    void testStatisticsMessageUUIDGeneration() {
        String senderNodeId = "node-789";
        LocalDateTime createdAt = LocalDateTime.now();
        StatisticsMessage.StatisticsDataType dataType = StatisticsMessage.StatisticsDataType.OUTPUT;

        StatisticsMessage message1 = new StatisticsMessage(senderNodeId, createdAt, dataType);
        StatisticsMessage message2 = new StatisticsMessage(senderNodeId, createdAt, dataType);

        assertNotEquals(message1.getId(), message2.getId());
    }

    @Test
    void testStatisticsMessageEquality() {
        String id = "test-id";
        String senderNodeId = "node-111";
        LocalDateTime createdAt = LocalDateTime.now();
        StatisticsMessage.StatisticsDataType dataType = StatisticsMessage.StatisticsDataType.INPUT;

        StatisticsMessage message1 = new StatisticsMessage(id, senderNodeId, createdAt, dataType);
        StatisticsMessage message2 = new StatisticsMessage(id, senderNodeId, createdAt, dataType);

        assertEquals(message1.getId(), message2.getId());
        assertEquals(message1.getSenderNodeId(), message2.getSenderNodeId());
        assertEquals(message1.getCreatedAt(), message2.getCreatedAt());
        assertEquals(message1.getStatisticsDataType(), message2.getStatisticsDataType());
    }
}