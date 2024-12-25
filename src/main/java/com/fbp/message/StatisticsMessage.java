package com.fbp.message;

import java.time.LocalDateTime;
import java.util.UUID;

public class StatisticsMessage extends Message {

    public enum StatisticsDataType {
        INPUT,
        OUTPUT,
        ERROR
    }

    private final String senderNodeId;
    private final LocalDateTime createdAt;
    private final StatisticsDataType statisticsDataType;

    public StatisticsMessage(String id, String senderNodeId, LocalDateTime createdAt, StatisticsDataType statisticsDataType) {
        super(id);
        this.senderNodeId = senderNodeId;
        this.createdAt = createdAt;
        this.statisticsDataType = statisticsDataType;
    }

    public StatisticsMessage(String senderNodeId, LocalDateTime createdAt, StatisticsDataType statisticsDataType) {
        this(UUID.randomUUID().toString(), senderNodeId, createdAt, statisticsDataType);
    }

    public String getSenderNodeId() {
        return senderNodeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public StatisticsDataType getStatisticsDataType() {
        return statisticsDataType;
    }
}
